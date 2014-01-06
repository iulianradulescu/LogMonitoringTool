package ro.mastermind.logmonit.filters;

import ro.mastermind.logmonit.model.LogFile;
import ro.mastermind.logmonit.model.LogLine;

/**
 *
 * @author radulescu
 */
public class ServerFilter extends LogFilter {
    
    private String server;

    public ServerFilter( String server ) {
	this.server = server;
    }
    
    @Override
    public boolean accept(LogFile file) {
	return server.equalsIgnoreCase( file.getServer( ) );
    }

    @Override
    public boolean accept(LogLine line) {
	return true; //always accept it, it has been validated at file level
    }
    
}
