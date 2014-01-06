/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import java.util.Set;
import ro.mastermind.logmonit.operations.LogReportOperations;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 * lists the command available
 * @author radulescu
 */
public class ListCommand extends UserCommand {

    public ListCommand(LogReportOperations ops, String[] parameters) {
	super(ops, parameters);
    }

    @Override
    public void validate() {
	//no parameters
	if ( parameters != null ) {
	    throw new InvalidCommandException("No parameters expected!");
	}
    }

    @Override
    public void execute() {
	UserCommandFactory factory = new UserCommandFactory( );
	Set<String> commands = factory.list();
	
	StringBuilder builder = new StringBuilder("Comenzi disponibile:");
	
	for( String command : commands ) { 
	    builder.append("\n\t").append(command);
	}
	
	writeOutput( builder.toString());
    }
    
}
