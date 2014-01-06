/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.analysis;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import ro.mastermind.logmonit.enums.LogAttribute;
import ro.mastermind.logmonit.exceptions.InvalidAnalysisConfigurationException;
import ro.mastermind.logmonit.model.LogLine;

/**
 *
 * @author radulescu
 */
public class UserActivity extends Analysis {
    
    private String username;
    
    private Set<LogLine> results;

    public UserActivity(String[] parameters) {
	super(parameters);
	
	results = (Set<LogLine>) new TreeSet( new Comparator() {

	    public int compare(Object o1, Object o2) {
		if ( o1 == null || o2 == null ||
			!(o1 instanceof LogLine) || !(o2 instanceof LogLine)) {
		    //error
		}
		
		return ((LogLine) o1).getTimestamp( ).compareTo( ((LogLine)o2).getTimestamp( ) );
	    }
	});
    }

    public void analyze(LogLine line) {
	if ( username.equalsIgnoreCase(line.getUsername( ))) {
	    results.add( line );
	}
    }

    @Override
    public void printResults() {
	for( LogLine line : results ) {
	    System.out.println( line.getLogAttribute(LogAttribute.MESSAGE));
	}
    }

    @Override
    protected void processParameters(String[] parameters) {
	//there should be only one parameter;
	if ( parameters == null || parameters.length != 1 ) {
	    throw new InvalidAnalysisConfigurationException(String.format("Invalid number of paramters for SystemLoad analysis. Expected 0 but found %d!",
		    parameters == null ? 0 : parameters.length));
	}
	
	username = parameters[0];
    }
    
}
