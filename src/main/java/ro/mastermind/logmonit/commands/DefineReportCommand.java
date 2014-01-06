/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import ro.mastermind.logmonit.operations.LogReportOperations;
import ro.mastermind.logmonit.enums.ReportType;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 * This command takes an input to define the 
 * @author radulescu
 */
public class DefineReportCommand extends UserCommand {

    ReportType option;
    
    public DefineReportCommand(LogReportOperations ops, String[] parameters ) {
	super(ops, parameters);
    }
    
    @Override
    public void execute() {
	ops.defineReport( option );
    }

    @Override
    public void validate() {
	//it is only one parameter, should be indicating the type of report; 
	if ( parameters == null || parameters.length > 1 ) {
	    throw new InvalidCommandException(String.format("Invalid number of parameters. Expected 1 found %d", parameters == null ? 0 : parameters.length));
	}
	
	//the parameter must be an int with an expected value
	try {
	    int value = Integer.valueOf( parameters[0] );
	    
	    if ( value < 0 || value  >= ReportType.values().length ) {
		throw new InvalidCommandException(String.format("Unexpected value. Expected value between 0 and %d, found %d", ReportType.values().length - 1, value));
	    }
	    
	    option = ReportType.values()[ value ];
	    
	} catch ( NumberFormatException ex  ) {
	    throw new InvalidCommandException(String.format("Invalid parameter type. Expected INT"));
	}
    }
    
    @Override
    public String toString( ) {
	StringBuilder buffer = new StringBuilder("Defineste un raport. Valori acceptate sunt:");
	
	ReportType[] values = ReportType.values( );
	for ( ReportType value : values ) {
	    buffer.append( String.format("%d - %s", value.ordinal(), value.name())).append(" | ");
	}
	
	buffer.append("\n");
	return buffer.toString();
    }
}
