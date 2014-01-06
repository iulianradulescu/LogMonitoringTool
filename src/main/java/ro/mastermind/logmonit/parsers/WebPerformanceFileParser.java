package ro.mastermind.logmonit.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ro.mastermind.logmonit.enums.LogAttribute;
import ro.mastermind.logmonit.exceptions.LogParsingException;
import ro.mastermind.logmonit.model.LogLine;

/**
 *
 * @author radulescu
 */
public class WebPerformanceFileParser extends FileParser {

    String format = "^.+\\[path = (/.+)\\]\\[time = ([0-9]+)ms\\]$";

    @Override
    protected void parseSpecificPart(LogLine line, String part) {
	
	if ( !line.getClassname( ).contains("ProcessingTimeFilter") ) {
	    throw new LogParsingException("Line does not correspond to the pattern. It should be dropped!");
	}
	
	Pattern p = Pattern.compile( format );
	Matcher m = p.matcher( part );
	
	if ( m.matches( ) ) {
	    //the first group is the path while the second is the duration
	    line.setLogAttribute(LogAttribute.URL, m.group(1));
	    line.setLogAttribute(LogAttribute.DURATION, Integer.parseInt(m.group(2)));
	} else {
	    //should reject the line, since it does not match the pattern
	    throw new LogParsingException("Line does not correspond to the pattern. It should be dropped!");
	}
    }
    
}
