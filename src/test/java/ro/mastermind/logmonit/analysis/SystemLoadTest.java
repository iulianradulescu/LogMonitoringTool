/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.analysis;

import org.testng.annotations.Test;
import ro.mastermind.logmonit.exceptions.InvalidAnalysisConfigurationException;

/**
 *
 * @author radulescu
 */
public class SystemLoadTest {
    
    private SystemLoad instance;
    
    public SystemLoadTest() {
    }
    
    @Test
    public void testProcessParameters_1( ) {
	instance = new SystemLoad( new String[]{});
    }
    
    @Test(expectedExceptions = {InvalidAnalysisConfigurationException.class})
    public void testProcessParameters_2( ) {
	instance = new SystemLoad( new String[]{"test"});
    }    
}
