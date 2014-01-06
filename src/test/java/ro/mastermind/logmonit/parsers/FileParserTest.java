/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.mastermind.logmonit.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ro.mastermind.logmonit.exceptions.UnexpectedLogFileFormatException;
import ro.mastermind.logmonit.model.LogLine;

import static org.powermock.api.mockito.PowerMockito.*;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.testng.PowerMockTestCase;
import ro.mastermind.logmonit.Utils;
import static ro.mastermind.logmonit.Utils.convert;

import ro.mastermind.logmonit.filters.DateRangeFilter;
import ro.mastermind.logmonit.filters.LogFilter;
import ro.mastermind.logmonit.filters.UsernameFilter;
import ro.mastermind.logmonit.model.LogFile;

/**
 * test class for FileParser base class
 *
 * @author radulescu
 */
@PrepareForTest({FileParser.class})
@PowerMockIgnore({"ro.mastermind.logmonit.exceptions.*"})
public class FileParserTest extends PowerMockTestCase{

    FileParser parser;

    //additional mocks
    File file;
    BufferedReader reader;

    public FileParserTest() {
    }

    @BeforeMethod
    public void lineParsingSetup() throws Exception {
	parser = Mockito.mock(FileParser.class, Mockito.CALLS_REAL_METHODS);

	file = mock(File.class);
	reader = mock(BufferedReader.class);
	FileReader freader = mock(FileReader.class);

	PowerMockito.whenNew(FileReader.class).withArguments(file).thenReturn(freader);
	PowerMockito.whenNew(BufferedReader.class).withArguments(freader).thenReturn(reader);

	when(file.getName()).thenReturn("portal-app.log");
	when(file.getPath()).thenReturn("portal-app.log");
	
	doNothing().when(parser).parseSpecificPart(any(LogLine.class), any(String.class));
    }

    @Test
    public void testParseBaseFormat() {
	String log = "11 Apr 2012 15:21:53,204 [INFO] [user] [class] message";
	LogLine line = new LogLine();
	parser.parseBaseFormat(line, log);

	Assert.assertEquals(line.getUsername(), "user");

	//parse the date
	Assert.assertEquals(convert("11.04.2012 15:21:53,204"), line.getTimestamp());
    }
    
    @Test
    public void testParseBaseFormat_2() {
	String log = "11 Apr 2012 15:21:55,446 [WARN] [olivia.marinescu] [ro.insse.esop.portal.filters.ProcessingTimeFilter] - Server-side processing:[path = /esop-web/pages/datacollection/dataCollection.faces][time = 2544ms]";
	LogLine line = new LogLine();
	String message = parser.parseBaseFormat(line, log);

	Assert.assertEquals("olivia.marinescu", line.getUsername());
	
	Assert.assertEquals("ro.insse.esop.portal.filters.ProcessingTimeFilter", line.getClassname());

	//parse the date
	Assert.assertEquals(convert("11.04.2012 15:21:55,446"),line.getTimestamp());
	
	//check the message
	Assert.assertEquals(" - Server-side processing:[path = /esop-web/pages/datacollection/dataCollection.faces][time = 2544ms]", message);
    }

    @Test(expectedExceptions = {UnexpectedLogFileFormatException.class})
    public void testParseBaseFormat_InvalidDateFormat() {
	String log = "11 04 2012 15:21:53,204 [INFO] [user] [class] message";
	parser.parseBaseFormat(new LogLine(), log);
    }

    @Test(expectedExceptions = {UnexpectedLogFileFormatException.class})
    public void testParseBaseFormat_InvalidLogFormat() {
	String log = "11 Apr 2012 15:21:53,204 [user] [class] message";
	Assert.assertNull(parser.parseBaseFormat(new LogLine(), log));
    }

    @Test
    public void testParseFileWithNoFilters() throws IOException {
	when(reader.readLine()).thenReturn("11 Apr 2012 15:21:53,204 [INFO] [user] [class] message").thenReturn("11 Apr 2012 15:21:53,204 [INFO] [user] [class] message").thenReturn(null);

	LogFile logFile = parser.parse(file, new ArrayList());

	//count the number of lines
	Assert.assertEquals(2, logFile.lines().size());
    }

    @Test
    public void testParseFileWithUsernameFilter() throws IOException {
	when(reader.readLine()).thenReturn("11 Apr 2012 15:21:53,204 [INFO] [user] [class] message").thenReturn("11 Apr 2012 15:21:53,204 [INFO] [user2] [class] message").thenReturn(null);

	List<LogFilter> filters = new ArrayList<LogFilter>();
	filters.add(new UsernameFilter("user"));

	LogFile logFile = parser.parse(file, filters);

	//count the number of lines
	Assert.assertEquals(1, logFile.lines().size());
    }

    @Test
    public void testParseFileWithDateFilterOnFile_1() throws IOException {
	when(reader.readLine()).thenReturn("nomessage").thenReturn("nomessage").thenReturn(null);
	when(file.lastModified()).thenReturn(new Date().getTime() - 1000 * 60 * 60 * 24);

	List<LogFilter> filters = new ArrayList<LogFilter>();
	filters.add(new DateRangeFilter(new Date(), null));

	LogFile logFile = parser.parse(file, filters);

	//no log file generated
	Assert.assertNull(logFile);
    }

    @Test
    public void testParseFileWithDateFilterOnFile_2() throws IOException {
	when(reader.readLine()).thenReturn("11 Apr 2012 15:21:53,204 [INFO] [user] [class] message").thenReturn("11 Apr 2012 15:21:56,204 [INFO] [user2] [class] message").thenReturn(null);
	when(file.lastModified()).thenReturn(new Date().getTime());

	List<LogFilter> filters = new ArrayList<LogFilter>();
	filters.add(new DateRangeFilter(Utils.convert("11.04.2012 15:20:53,204"), Utils.convert("11.04.2012 15:21:54,204")));

	LogFile logFile = parser.parse(file, filters);

	//count the number of lines
	Assert.assertEquals(1, logFile.lines().size());
    }

    
}
