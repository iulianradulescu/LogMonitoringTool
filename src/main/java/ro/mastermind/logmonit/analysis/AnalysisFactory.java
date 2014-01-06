/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.analysis;

import java.util.HashMap;
import java.util.Map;
import ro.mastermind.logmonit.exceptions.InvalidAnalysisConfigurationException;

/**
 * Factory class used for creating specific Analysis objects based on the request
 * @author radulescu
 */
public class AnalysisFactory {
    
    private Map<String, AnalysisFactoryCommand> commandMap = new HashMap<String, AnalysisFactoryCommand>( );
    
    private static interface AnalysisFactoryCommand {
	Analysis create (String[] parameters );
    }
    
    public AnalysisFactory( ) {
	commandMap.put("httpReq", new AnalysisFactoryCommand( ){

	    public Analysis create(String[] parameters) {
		return new HTTPRequestPerformance( parameters );
	    } 
	});
	
	commandMap.put("userAct", new AnalysisFactoryCommand( ){

	    public Analysis create(String[] parameters) {
		return new UserActivity( parameters );
	    } 
	});
	
	commandMap.put("sysLoad", new AnalysisFactoryCommand( ){

	    public Analysis create(String[] parameters) {
		return new SystemLoad( parameters );
	    } 
	});
    }
    
    public boolean isValidAnalysis( String name ) {
	return commandMap.containsKey( name );
    }
    
    /**
     * creates an associate Analysis object which will implement the Visitor pattern
     * @param name
     * @param parameters
     * @return 
     */
    public Analysis makeAnalysisObject( String name, String[] parameters ) {
	AnalysisFactoryCommand factory = commandMap.get( name );
	
	if ( factory == null ) {
	    throw new InvalidAnalysisConfigurationException(String.format("Analysis %s is not defined in the system", name));
	}
	
	return factory.create( parameters );
    }
}
