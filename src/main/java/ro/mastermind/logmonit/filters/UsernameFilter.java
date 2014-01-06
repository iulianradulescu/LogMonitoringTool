package ro.mastermind.logmonit.filters;

import ro.mastermind.logmonit.model.LogFile;
import ro.mastermind.logmonit.model.LogLine;

/**
 *
 * @author radulescu
 */
public class UsernameFilter extends LogFilter {
    
    private String username;
    
    public UsernameFilter( String username ) {
	this.username = username;
    }

    @Override
    public boolean accept(LogFile file) {
	return true; //cannot be validated,it does not have the info
    }

    @Override
    public boolean accept(LogLine line) {
	return username.equalsIgnoreCase( line.getUsername());
    }
    
}
