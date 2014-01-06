package ro.mastermind.logmonit.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ro.mastermind.logmonit.Utils;
import ro.mastermind.logmonit.analysis.Analysis;
import ro.mastermind.logmonit.enums.FileType;
import ro.mastermind.logmonit.filters.LogFilter;

/**
 * Identifies a file from which log lines were read
 * 
 * @author radulescu
 */
public class LogFile {
    
    /** name of the file*/
    private String filename;
    
    /** date of last modification */
    private Date lastModified;
    
    /** the server on which the log file was generated*/
    protected String server;
    
    /** type of the file*/
    private FileType type;
    
    /** list of log lines parsed*/
    private List<LogLine> lines = new ArrayList<LogLine>( );
    
    public LogFile( File file ) {
	this.filename = file.getName( );
	this.lastModified = new Date( file.lastModified( ) );
	this.type = FileType.forName( this.filename );
	
	this.server = Utils.getServerName( filename );
    }
    
    public boolean validate( LogFilter filter ) {
	return filter.accept( this );
    }
    
    public boolean validate( List<LogFilter> filters ) {
	for ( LogFilter filter : filters ) {
	    if ( !validate( filter ) ) {
		return false;
	    } 
	}
	
	return true;
    }
    
    public void analyze( Analysis analysis ) {
	analysis.analyze( this );
    }
    
    public void addLogLine( LogLine line ) {
	lines.add( line );
    }
    
    public String getServer( ) {
	return this.server;
    }
    
    public Date getLastModified( ) {
	return this.lastModified;
    }
    
    public List<LogLine> lines( ) { 
	return this.lines;
    }
    
    public String toString( ) {
	return new StringBuilder("")
		.append(String.format("[filename = %s, server = %s, lines = %d]",filename, server, lines.size( ) ) ).toString();
    }
}
