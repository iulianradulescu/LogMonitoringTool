/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit;

import junit.framework.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author radulescu
 */
public class UtilsTest {
    
    public UtilsTest() {
    }
    
    @Test
    public void testGetServerName_1( ) {
	Assert.assertEquals("wls_portal1",Utils.getServerName("wls_portal1.log"));
    }
    
    @Test
    public void testGetServerName_NULL_( ) {
	Assert.assertNull( Utils.getServerName("wls_portal1.lg"));
    }
    
    @Test
    public void testGetServerName_FullName( ) {
	Assert.assertEquals( "wls_portal1", Utils.getServerName("esop-perforamnce-wls_portal1.log"));
    }
    
    @Test
    public void testGetServerName_FullPath( ) {
	Assert.assertEquals( "wls_portal1", Utils.getServerName("/Users/radulescu/projects/2012/esop/technical/esop-perforamnce-wls_portal1.log"));
    }
}
