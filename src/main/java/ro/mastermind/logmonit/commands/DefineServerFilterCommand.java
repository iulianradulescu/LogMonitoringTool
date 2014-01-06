/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import ro.mastermind.logmonit.operations.LogReportOperations;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;
import ro.mastermind.logmonit.filters.ServerFilter;

/**
 *
 * @author radulescu
 */
public class DefineServerFilterCommand extends UserCommand {

    private String server;
    
    public DefineServerFilterCommand(LogReportOperations ops, String[] parameters ) {
	super(ops, parameters );
    }

    @Override
    public void execute() {
	ops.addFilter( new ServerFilter( server ) );
    }

    @Override
    public void validate() {
	//it should be one and only one
	if ( parameters == null || parameters.length != 1 ) {
	    throw new InvalidCommandException(String.format("Invalid number of parameters. Expected 1 found %d", parameters == null ? 0 : parameters.length));
	}
	
	this.server = parameters[0];
    }
    
}
