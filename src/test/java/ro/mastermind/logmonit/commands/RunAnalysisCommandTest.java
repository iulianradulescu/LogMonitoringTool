/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import org.testng.Assert;
import org.testng.annotations.Test;
import ro.mastermind.logmonit.analysis.HTTPRequestPerformance;
import ro.mastermind.logmonit.analysis.SystemLoad;
import ro.mastermind.logmonit.analysis.UserActivity;
import ro.mastermind.logmonit.exceptions.InvalidAnalysisConfigurationException;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 *
 * @author radulescu
 */
public class RunAnalysisCommandTest {
    
    private RunAnalysisCommand command;
    
    public RunAnalysisCommandTest() {
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidate_WrongNoOfParams( ) {
	command = new RunAnalysisCommand(null, new String[] {} );
	command.validate();
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidate_InvalidPattern_1( ) {
	command = new RunAnalysisCommand(null, new String[] {"httpReq"} );
	command.validate();
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidate_InvalidPattern_2( ) {
	command = new RunAnalysisCommand(null, new String[] {"httpReq("} );
	command.validate();
    }
    
    @Test
    public void testValidate_checkAnalysis_1( ) {
	command = new RunAnalysisCommand(null, new String[] {"httpReq(0,1000,5)"} );
	command.validate();
	
	//should be httpReq
	Assert.assertEquals( command.analysis().getClass(), HTTPRequestPerformance.class );
    }
    
    @Test(expectedExceptions = {InvalidAnalysisConfigurationException.class})
    public void testValidate_checkAnalysis_2( ) {
	command = new RunAnalysisCommand(null, new String[] {"httpREQ(0,1000,5)"} );
	command.validate();
	
	//should be httpReq
	Assert.assertEquals( command.analysis().getClass(), HTTPRequestPerformance.class );
	
	Assert.assertTrue(command.analysis().parameters().length == 3 );
    }
    
    @Test
    public void testValidate_checkAnalysis_3( ) {
	command = new RunAnalysisCommand(null, new String[] {"sysLoad( )"} );
	command.validate();
	
	//should be httpReq
	Assert.assertEquals( command.analysis().getClass(), SystemLoad.class );
	
	Assert.assertNull(command.analysis().parameters() );
    }
    
    @Test
    public void testValidate_checkAnalysis_4( ) {
	command = new RunAnalysisCommand(null, new String[] {"userAct(user)"} );
	command.validate();
	
	//should be httpReq
	Assert.assertEquals( command.analysis().getClass(), UserActivity.class );
	
	Assert.assertTrue(command.analysis().parameters().length == 1 );
    }
    
    
    
    
    
}
