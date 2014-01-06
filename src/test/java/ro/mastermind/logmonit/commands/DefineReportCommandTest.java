/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import org.testng.annotations.Test;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 *
 * @author radulescu
 */
public class DefineReportCommandTest {
    
    private DefineReportCommand command;
    
    public DefineReportCommandTest() {
    }
    
    @Test
    public void testValidate_OK( ) {
	command = new DefineReportCommand( null, new String[] {"1"} );
	command.validate();
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidate_WrongNoOfParameters( ) {
	command = new DefineReportCommand( null, new String[] {"1","2"} );
	command.validate( );
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidate_WrongParameterValue( ) {
	command = new DefineReportCommand( null, new String[] {"2"} );
	command.validate( );
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidate_InvalidParameterType( ) {
	command = new DefineReportCommand( null, new String[] {"xxx"} );
	command.validate( );
    }
}
