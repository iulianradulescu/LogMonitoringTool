package ro.mastermind.logmonit.filters;

import java.util.List;
import ro.mastermind.logmonit.model.LogFile;
import ro.mastermind.logmonit.model.LogLine;

/**
 * Base class for defining and applying filters on log information (files and lines)
 * @author radulescu
 */
public abstract class LogFilter {
    
    public abstract boolean accept( LogFile file );
    public abstract boolean accept( LogLine line );
    
    /**
     * returns the DateRangeFilter (if any) from the list specified
     * @param filters
     * @return 
     */
    public static DateRangeFilter dateFilter( List<LogFilter> filters ) {
	for ( LogFilter filter: filters ) {
	    if ( filter instanceof DateRangeFilter ) {
		return ( DateRangeFilter )filter;
	    }
	}
	
	return null;
    }
}
