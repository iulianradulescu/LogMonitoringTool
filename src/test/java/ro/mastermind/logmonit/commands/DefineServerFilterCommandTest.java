/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 *
 * @author radulescu
 */
public class DefineServerFilterCommandTest {
    
    public DefineServerFilterCommandTest() {
    }
    
    @Test
    public void testValidate_OK( ) {
	DefineServerFilterCommand command = new DefineServerFilterCommand( null, new String[] {"server"} );
	command.validate();
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidate_NOK( ) {
	DefineServerFilterCommand command = new DefineServerFilterCommand( null, new String[] { } );
	command.validate();
    }
}
