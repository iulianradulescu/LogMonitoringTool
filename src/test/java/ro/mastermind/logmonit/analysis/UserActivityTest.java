/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.analysis;

import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.Test;
import ro.mastermind.logmonit.exceptions.InvalidAnalysisConfigurationException;

/**
 *
 * @author radulescu
 */
public class UserActivityTest {
    
    private UserActivity instance;
    
    public UserActivityTest() {
    }
    
    @Test(expectedExceptions = {InvalidAnalysisConfigurationException.class})
    public void testProcessParameters_1( ) {
	instance = new UserActivity( new String[] {});
    }
    
    @Test(expectedExceptions = {InvalidAnalysisConfigurationException.class})
    public void testProcessParameters_2( ) {
	instance = new UserActivity( new String[] {"test", "test2"});
    }
    
    @Test
    public void testProcessParameters_3( ) {
	instance = new UserActivity( new String[] {"user"});
	Assert.assertEquals(Whitebox.getInternalState( instance, String.class), "user");
    }
    
}
