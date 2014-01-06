/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.testng.annotations.Test;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 * Test class for the DefineDateRangeFilerCommand command object 
 * @author radulescu
 */
public class DefineDateRangeFilterCommandTest {
    
    private DefineDateRangeFilterCommand command;
    
    public DefineDateRangeFilterCommandTest() {
	command = new DefineDateRangeFilterCommand( null, null);
    }
    
    @Test 
    public void testValidateParameter_EXTENDED( ) {	
	String parameter = "from(10.01.13-08:30)";
	Assert.assertEquals(convert("10.01.2013-08:30"), command.validateParameter(parameter, "from"));
    }
    
    @Test
    public void testValidateParameter_SHORT( ) {
	String parameter = "from(10.01-9:40)";
	Assert.assertEquals(convert("10.01.2013-9:40"), command.validateParameter(parameter, "from"));
    }
    
    @Test
    public void testValidateParameter_CURRENT( ) {
	String parameter = "from(10:50)";
	//it means today at 10:50
	Calendar now = new GregorianCalendar( );
	now.set(Calendar.HOUR_OF_DAY, 10);
	now.set(Calendar.MINUTE, 50);
	now.set(Calendar.SECOND, 0);
	now.set(Calendar.MILLISECOND, 0);
	
	Assert.assertEquals( now.getTime( ), command.validateParameter(parameter, "from"));
    }
    
    @Test
    public void testValidateParameter_SHORT_BIG_HOURS( ) {
	Calendar now = new GregorianCalendar( );
	now.set(Calendar.DAY_OF_MONTH, 13);
	now.set(Calendar.HOUR_OF_DAY, 8);
	now.set(Calendar.MINUTE, 45);
	now.set(Calendar.SECOND, 0);
	now.set(Calendar.MILLISECOND, 0);
	
	command.validateParameter("from(10.01-80:45)", "from");
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidateParameterIncorrectFormat_1( ) {
	String parameter = "form()";
	
	command.validateParameter( parameter, "from");
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidateParameterIncorrectFormat_2( ) {
	String parameter = "form(10,03)";
	
	command.validateParameter( parameter, "from");
    }
    
    @Test
    public void testValidateParameterNoDate( ) {
	Assert.assertNull( command.validateParameter("from()", "from"));
    }
    
    @Test( expectedExceptions = {InvalidCommandException.class})
    public void testValidateParameterInvalidDate_1( ) {
	command.validateParameter("from(10.30)", "from");
    }
    
    @Test
    public void testValidate_OK( ) {
	command = new DefineDateRangeFilterCommand( null, new String[] {"from()", "to()"} );
	command.validate();
    }
    
    @Test
    public void testValidate_OK_WithDates( ) {
	command = new DefineDateRangeFilterCommand( null, new String[] {"from(10.05-09:00)", "to(10.05-09:05)"} );
	command.validate();
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidate_NOK( ) {
	command = new DefineDateRangeFilterCommand( null, new String[] {"from()"} );
	command.validate();
    }
    
    @Test(expectedExceptions = {InvalidCommandException.class})
    public void testValidate_NOK_WithDates( ) {
	command = new DefineDateRangeFilterCommand( null, new String[] {"from(10.05-09:00)", "to(10.05-08:59"} );
	command.validate();
    }
    
    private Date convert( String date ) {
	try {
	    return new SimpleDateFormat( "dd.MM.yyyy-HH:mm" ).parse( date );
	} catch ( ParseException exPE ) {
	    //it will never get there
	    return null;
	}
    }
}
