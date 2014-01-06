/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import ro.mastermind.logmonit.operations.LogReportOperations;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 * Command class that shows help for a specific command; it calls the methods help from the indicated command and display it
 * @author radulescu
 */
public class HelpCommand extends UserCommand {

    public HelpCommand(LogReportOperations ops, String[] parameters) {
	super(ops, parameters);
    }

    @Override
    public void validate() {
	//it should have one parameter; more than that throw an error
	if ( parameters == null || parameters.length > 1 ) {
	    throw new InvalidCommandException(String.format("Invalid number of parameters. Expected 1 found %d", parameters == null ? 0 : parameters.length));
	}
    }

    @Override
    public void execute() {
	UserCommandFactory factory = new UserCommandFactory();
	
	UserCommand command = factory.makeUserCommand(ops, parameters[0] );
	writeOutput( command.toString( ));
    }
    
}
