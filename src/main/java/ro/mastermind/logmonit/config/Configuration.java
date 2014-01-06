/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.config;

import java.io.File;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import ro.mastermind.logmonit.exceptions.InvalidConfigurationException;

/**
 * singleton class to hold application configuration
 * @author radulescu
 */
public class Configuration {
    
    //parser
    OptionParser parser;
    
    //actual option set
    OptionSet options;
    
    /** the directory where log files are stored*/
    private File logDirectory;
    
    /** path to a text file containing a list of commands to be executed, each command on a separate line*/
    private File commandFile;
    
    private Configuration( ) { 
	//set the expected options
	parser = new OptionParser( );
	//accepted options
	parser.accepts("d", "Path to directory where log files are stored").withRequiredArg().ofType( File.class );
	parser.accepts("c", "Path to file which contains the list of commands to be executed").withOptionalArg().ofType( File.class );
    }
    
    private static class ConfigurationHolder {
	private static Configuration INSTANCE = new Configuration( );
    }
    
    public static Configuration instance( ) {
	return ConfigurationHolder.INSTANCE;
    }
    
    public void parseArgs( String[] args ) {
	if ( args == null ) {
	    throw new InvalidConfigurationException("No arguments provided!");
	}
	try { 
	    OptionSet options = parser.parse( args );
	    
	    if ( options.has("d") ) {
		//get the log directory
		logDirectory = ( File )options.valueOf("d");
	    } else {
		//throw an error
		throw new InvalidConfigurationException("Option -d should be present!");
	    }
	    
	    if ( options.has("c") ) {
		//get the file 
		commandFile = ( File )options.valueOf("c");
	    }
	} catch ( OptionException exOE ) {
	    //error while parsing
	    throw new InvalidConfigurationException(String.format("Invalid command line arguments! Found %s", exOE.options()));
	}
    }
    
    public void printUsage( ) {
	StringBuilder builder = new StringBuilder("Usage:");
	builder.append("\n -Dlog.dir=<path_log_directory>");
	builder.append("\n -Dconfiguration.file=<path_to_configuration_file>");
    }
    
    public boolean isValidConfiguration( ) {
	return this.logDirectory != null;
    }
    
    public File getLogDirectory( ) {
	return this.logDirectory;
    }
    
    public File getCommandFile( ) {
	return this.commandFile;
    }
} 
