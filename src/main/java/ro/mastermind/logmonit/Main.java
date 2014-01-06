package ro.mastermind.logmonit;

import ro.mastermind.logmonit.operations.InputHandler;
import ro.mastermind.logmonit.config.Configuration;

/**
 * Main class
 *
 */
public class Main 
{
    public static void main( String[] args ) {
	
	Configuration.instance().parseArgs( args );
	
	//if is not a valid configuration, do not continue
	if ( !Configuration.instance().isValidConfiguration( ) ) {
	    System.out.println("FATAL: Invalid configguration. Application will exit!");
	    System.exit(1);
	}
	
	//process all inputs
        InputHandler.instance().handleAllInputs();
	System.out.println("INFO: Application terminates normally!");
    }
}
