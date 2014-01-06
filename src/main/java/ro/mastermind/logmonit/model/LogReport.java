package ro.mastermind.logmonit.model;

import java.util.ArrayList;
import java.util.List;
import ro.mastermind.logmonit.analysis.Analysis;
import ro.mastermind.logmonit.enums.ReportType;
import ro.mastermind.logmonit.filters.LogFilter;

/**
 * Define a log report on which statistics or further processings can be performed
 * @author radulescu
 */
public class LogReport {
    
    /** files included in the report */
    private List<LogFile> files = new ArrayList<LogFile>( );
    
    /** type of the report*/
    private ReportType type;
    
    /** filters*/
    private List<LogFilter> filters;
    
    public LogReport( ReportType type, List<LogFilter> filters ) {
	this.type = type;
	this.filters = filters;
    }
    
    public void addLogFilter( LogFilter filter ) {
	if ( filters == null ) {
	    filters = new ArrayList<LogFilter>( );
	}
	
	filters.add(filter);
    }
    
    public void analyze( Analysis analysis ) {
	analysis.analyze( this );
    }
    
    public ReportType getType( ) {
	return this.type;
    }
    
    public List<LogFilter> getFilters( ) {
	return this.filters;
    }
    
    public synchronized void addFile( LogFile file ) {
	this.files.add( file );
    }
    
    public List<LogFile> files( ) {
	return this.files;
    }
    
    /**
     * print status of the report after a parsing
     */
    public String toString( ) {
	StringBuilder builder = new StringBuilder();
	builder.append("Number of files processed: ").append(files.size( ) );
	for ( LogFile file : files ) {
	    builder.append("\n").append(file.toString());
	}
	
	return builder.toString();
    }
}
