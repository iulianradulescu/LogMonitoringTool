/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.mastermind.logmonit.analysis;

import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;
import ro.mastermind.logmonit.exceptions.InvalidAnalysisConfigurationException;
import ro.mastermind.logmonit.model.LogLine;

import org.powermock.reflect.Whitebox;
import ro.mastermind.logmonit.enums.LogAttribute;

/**
 *
 * @author radulescu
 */
public class HTTPRequestPerformanceTest {

    private HTTPRequestPerformance instance;

    public HTTPRequestPerformanceTest() {
    }

    @Test(expectedExceptions = {InvalidAnalysisConfigurationException.class})
    public void testProcessParameters_checkNoOf_1() {
	//the method is called within the constructor, so the most easy way to test it is to intantiate an object
	instance = new HTTPRequestPerformance(null);
    }

    @Test(expectedExceptions = {InvalidAnalysisConfigurationException.class})
    public void testProcessParameters_checkNoOf_2() {
	//the method is called within the constructor, so the most easy way to test it is to intantiate an object
	instance = new HTTPRequestPerformance(new String[]{"1", "2"});
    }

    @Test(expectedExceptions = {InvalidAnalysisConfigurationException.class})
    public void testProcessParameters_invalidFormat() {
	//the method is called within the constructor, so the most easy way to test it is to intantiate an object
	instance = new HTTPRequestPerformance(new String[]{"p1", "p2", "p3"});
    }

    @Test(expectedExceptions = {InvalidAnalysisConfigurationException.class})
    public void testProcessParameters_invalidNoOfRanges() {
	//the method is called within the constructor, so the most easy way to test it is to intantiate an object
	instance = new HTTPRequestPerformance(new String[]{"1", "2", "-2"});
    }

    @Test(expectedExceptions = {InvalidAnalysisConfigurationException.class})
    public void testProcessParameters_invalidBounds_1() {
	//the method is called within the constructor, so the most easy way to test it is to intantiate an object
	instance = new HTTPRequestPerformance(new String[]{"1", "-2", "2"});
    }

    @Test(expectedExceptions = {InvalidAnalysisConfigurationException.class})
    public void testProcessParameters_invalidBounds_2() {
	//lbound is greater than ubound
	instance = new HTTPRequestPerformance(new String[]{"10", "5", "2"});
    }

    @Test
    public void testProcessParameters_ranges() {
	//lbound is greater than ubound
	instance = new HTTPRequestPerformance(new String[]{"0", "100", "5"});

	List ranges = Whitebox.getInternalState(instance, List.class);
	Assert.assertEquals(ranges.size(), 6);
    }

    @Test
    public void testAnalyze_1() {
	instance = new HTTPRequestPerformance(new String[]{"0", "100", "4"});

	LogLine line = new LogLine();
	line.setLogAttribute(LogAttribute.DURATION, 20);
	instance.analyze(line);

	//there should be no result; since the LogLine does not contain DURATION
	Map results = Whitebox.getInternalState(instance, Map.class);
	Assert.assertTrue(results.size() == 1);
    }

    @Test
    //there should be no result; since the LogLine does not contain DURATION
    public void testAnalyze_2() {
	instance = new HTTPRequestPerformance(new String[]{"0", "100", "4"});

	LogLine line = new LogLine();
	instance.analyze(line);

	Map results = Whitebox.getInternalState(instance, Map.class);
	Assert.assertTrue(results.isEmpty());
    }

    @Test
    //LogLine has a DURATION attribute in the last range
    public void testAnalyze_3() {
	instance = new HTTPRequestPerformance(new String[]{"0", "100", "4"});

	LogLine line = new LogLine();
	line.setLogAttribute(LogAttribute.DURATION, 120);
	instance.analyze(line);

	Map results = Whitebox.getInternalState(instance, Map.class);
	Assert.assertTrue(results.size() == 1);
    }
}
