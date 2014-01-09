/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.config;

import org.junit.Assert;
import org.testng.annotations.Test;
import ro.mastermind.logmonit.exceptions.InvalidConfigurationException;

/**
 *
 * @author radulescu
 */
public class ConfigurationTest {
    
    public ConfigurationTest() {
    }
    
    @Test( expectedExceptions = {InvalidConfigurationException.class})
    public void testParseArgsNull( ) {
	Configuration.instance().parseArgs( null );
    }
    
    @Test( expectedExceptions = {InvalidConfigurationException.class})
    public void testParseArgsEmpty( ) {
	Configuration.instance().parseArgs( new String[] {} );
    }
    
    @Test( expectedExceptions = {InvalidConfigurationException.class})
    public void testParseArgsDMissingArgument( ) {
	Configuration.instance().parseArgs( new String[] {"f"} );
    }
    
    @Test
    public void testParseArgsWithDArgument( ) {
	String path = "temp";
	Configuration.instance().parseArgs( new String[] {"-d", path } );
	
	Assert.assertTrue( Configuration.instance().getLogDirectory( ).getPath( ).equals( path ));
    }
    
}
