package ro.mastermind.logmonit.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ro.mastermind.logmonit.enums.LogAttribute;
import ro.mastermind.logmonit.exceptions.LogParsingException;
import ro.mastermind.logmonit.exceptions.UnexpectedLogFileFormatException;
import ro.mastermind.logmonit.filters.DateRangeFilter;
import ro.mastermind.logmonit.filters.LogFilter;
import ro.mastermind.logmonit.model.LogFile;
import ro.mastermind.logmonit.model.LogLine;

/**
 * Base class for different parsing strategies
 *
 * @author radulescu
 */
public abstract class FileParser {

    /**
     * base format regexp extracts the timestamp, log level, username, and class name, leaving the rest as message to
     * further processed in derived classes
     */
    private final String baseFormat = "^([0-9]{1,2}\\s[A-Za-z]{3}\\s[0-9]{4}\\s[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3})\\s\\[([A-Z]+)\\]\\s\\[(.+)\\]\\s\\[([A-Za-z0-9_\\.]+)\\](.+)$";

    /**
     * implementation of Template Method. This method defines the algorithm of parsing a line, providing hook methods that can be further 
     * implemented in derived classes
     */
    protected LogLine parseLine( String lineString ) {
	//avoid empty lines
	try {
	    if ( lineString.trim().length() == 0 ) {
		return null;
	    }
	    //first get teh appropriate instance of LogLine
	    LogLine line = new LogLine( );

	    //parse the command part
	    String part = parseBaseFormat(line, lineString);

	    //parse the specific part
	    try {
		parseSpecificPart( line, part);
	    } catch ( LogParsingException exLPE ) {
		//the line does not match the pattern so it should be dropped
		return null;
	    }
	    
	    return line;
	} catch (UnexpectedLogFileFormatException exULFFE ) {
	    //just drop the line
	    return null;
	}
    }
    
    /** the implementation will parse the specific part from a log line, and populate accordingly the LogLine object received as parameter*/
    protected abstract void parseSpecificPart( LogLine line, String part );
    

    /**
     * parse a file while also applying the filters. The filters apply both to the file and the its content.
     *
     * @param file
     * @param filters
     * @return
     */
    public LogFile parse(File file, List<LogFilter> filters) {

	LogFile logFile = new LogFile(file);
	
	//get the date filter if any
	DateRangeFilter dateFilter = LogFilter.dateFilter( filters );

	if (logFile.validate(filters)) {
	    //if filters are validated, continue with parsing
	    //each file has a number of lines; read it line by line, generate a LogLine from it, and validate it;
	    //if valid add it to the LogFile class
	    BufferedReader reader = null;
	    try {
		reader = new BufferedReader(new FileReader(file));

		String line;
		while ((line = reader.readLine()) != null) {
		    LogLine logLine = parseLine(line);
		    
		    if ( logLine == null ) {
			continue;
		    }
		    
		    //OPTIMIZATION: we need to interrupt the file processing if the date filtering is no longer 
		    //matched (since logs are placed in order of the date in the file)
		    if ( dateFilter != null && dateFilter.stopFileProcessing( logLine.getTimestamp( ) ) ) {
			break;
		    }
		    
		    if ( logLine.validate( filters ) ) {
			logFile.addLogLine(logLine);
		    }
		}
	    } catch (FileNotFoundException exFNFE) {
		throw new LogParsingException(String.format("Log file %s was not found!",file.getPath( ) ));
	    } catch (IOException exIOE) {
		throw new LogParsingException(String.format("Log file %s could not be processed!",file.getPath( ) ));
	    } finally {
		if (reader != null) {
		    try {
			reader.close();
		    } catch (IOException exIOE) {
			//TODO
		    }
		}
	    }
	    
	    return logFile.lines().isEmpty() ? null : logFile;
	}

	return null;
    }

    /**
     * The method should be called by derived classes. the callers should provide the specific instance of LogLine and
     * the method will populate it. The method will return the rest of the message
     *
     * @param line
     * @param stringLine
     * @return
     */
    protected String parseBaseFormat(LogLine line, String stringLine) {
	Pattern p = Pattern.compile(baseFormat);
	Matcher m = p.matcher(stringLine);

	if (m.matches()) {
	    //if it matches, then get the groups in order: timestamp, loglevel, username, classname and finally the rest of the message
	    line.setLogAttribute(LogAttribute.MESSAGE, stringLine);
	    
	    //timestamp
	    line.setTimestamp( convertToDate( m.group(1) ) );
	    
	    //username
	    line.setUsername( m.group( 3 ) ); 
	    
	    //class name
	    line.setClassname( m.group( 4 ) );
	    
	    return m.group( 5 );
	}
	
	throw new UnexpectedLogFileFormatException("Log line does not respect the base format expected!");
    }

    /**
     * helper method which converts the string to a date object. The expected pattern is dd MMM yyyy HH:mm:ss,S
     */
    protected Date convertToDate(String date) {
	String formatString = "dd MMM yyyy HH:mm:ss,S";

	try {
	    SimpleDateFormat format = new SimpleDateFormat(formatString);
	    return format.parse( date );
	} catch ( ParseException exPE ) {
	    //unexpected string as date; invalid file format perhaps?
	    throw new UnexpectedLogFileFormatException(String.format("Unexpected string as date. Expected format is %s", formatString));
	}
    }
}
