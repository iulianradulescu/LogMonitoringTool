/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.filters;

import java.io.File;
import junit.framework.Assert;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.testng.annotations.Test;
import ro.mastermind.logmonit.Utils;
import ro.mastermind.logmonit.model.LogFile;

/**
 *
 * @author radulescu
 */
public class DateRangeFilterTest {
    
    private DateRangeFilter filter;
    
    private File file;
    
    public DateRangeFilterTest() {
	file = mock( File.class );
	
	when( file.getName( ) ).thenReturn("esop-performance-wls_portal1.log");
	when( file.lastModified( ) ).thenReturn( Utils.convert("17.04.2012 14:15:00,000").getTime( ) );
    }
    
    
 
    @Test
    public void testAcceptFile_1( ) {
	filter = new DateRangeFilter( Utils.convert("11.04.2012 15:21:00,000"), Utils.convert("11.04.2012 15:23:00,000"));
	
	Assert.assertTrue( filter.accept( new LogFile( file ) ) );
    }
    
    @Test
    public void testAcceptFile_Reject( ) {
	filter = new DateRangeFilter( Utils.convert("19.04.2012 15:21:00,000"), Utils.convert("11.04.2012 15:23:00,000"));
	
	Assert.assertFalse( filter.accept( new LogFile( file ) ) );
    }
}
