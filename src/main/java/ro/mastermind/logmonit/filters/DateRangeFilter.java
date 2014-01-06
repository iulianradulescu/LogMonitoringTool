package ro.mastermind.logmonit.filters;

import java.util.Date;
import ro.mastermind.logmonit.model.LogFile;
import ro.mastermind.logmonit.model.LogLine;

/**
 *
 * @author radulescu
 */
public class DateRangeFilter extends LogFilter {
    
    private Date from;
    private Date to;
    
    public DateRangeFilter( Date from, Date to ) {
	this.from = from;
	this.to = to;
    }
    
    @Override
    public boolean accept(LogFile file) {
	//if the file has been modified in the past from from date, drop it
	if ( from != null && file.getLastModified( ).before( from ) ) {
	    return false;
	}
	
	return true;
    }

    @Override
    public boolean accept(LogLine line) {
	if ( (from != null && line.getTimestamp( ).before( from )) || 
		(to != null && line.getTimestamp( ).after( to ) ) ) {
	    return false;
	}
	
	return true;
    }
    
    public boolean stopFileProcessing( Date date ) {
	return to != null && date.after( to );
    }
    
}
