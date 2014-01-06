/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;
import ro.mastermind.logmonit.exceptions.InvalidAnalysisConfigurationException;
import ro.mastermind.logmonit.model.LogFile;
import ro.mastermind.logmonit.model.LogLine;

/**
 *
 * @author radulescu
 */
public class SystemLoad extends Analysis {

    //a map of servers and on each server, a list of unique users who have accessed the system
    private Map<String, Set<String>> results = new HashMap<String,Set<String>>( );
    
    private Set<String> users = new TreeSet<String>( );
    
    public SystemLoad(String[] parameters) {
	super(parameters);
    }

    public void analyze(LogLine line) {
	users.add( line.getUsername( ) );
    }
    
    @Override
    public void analyze(LogFile file) {
	super.analyze( file );
	//save the results and reset it;
	Set<String> serverUsers = results.get( file.getServer( ) );
	if ( serverUsers == null ) {
	    serverUsers = new TreeSet<String>( users );
	} else {
	    serverUsers.addAll( users );
	}
	
	users.clear();
	results.put( file.getServer(), serverUsers );
    }

    @Override
    public void printResults() {
	System.out.println(StringUtils.repeat("-", 60));
	//three columns in the header
	System.out.printf("%-25s \t %-25s\n", "Server","Unique users");
	System.out.println(StringUtils.repeat("-", 60));
	
	Set<String> totalUsers = new TreeSet<String>( );
	Set<String> servers = results.keySet();
	
	for ( String server : servers ) {
	    System.out.printf("%-25s \t %-25d\n", server, results.get( server ).size());
	    totalUsers.addAll( results.get( server ));
	}
	
	System.out.println(StringUtils.repeat("-", 60));
	//TOTAL
	System.out.printf("%-25s \t %-25d\n", "TOTAL", totalUsers.size());
	
	System.out.println(StringUtils.repeat("-", 60));
    }

    @Override
    protected void processParameters(String[] parameters) {
	//there should be no parameters;
	if ( parameters != null && parameters.length >= 1 ) {
	    throw new InvalidAnalysisConfigurationException(String.format("Invalid number of paramters for SystemLoad analysis. Expected 0 but found %d!",parameters.length));
	}
    }
    
}
