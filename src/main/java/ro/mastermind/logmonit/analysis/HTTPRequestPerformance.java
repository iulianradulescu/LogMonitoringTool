/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import ro.mastermind.logmonit.enums.LogAttribute;
import ro.mastermind.logmonit.exceptions.InvalidAnalysisConfigurationException;
import ro.mastermind.logmonit.model.LogLine;

/**
 * displays a statistics of the durations for HTTP requests received by the web layer
 * @author radulescu
 */
public class HTTPRequestPerformance extends Analysis {
    
    private List<Range> ranges;
    
    private Map<Range, Integer> results = new HashMap<Range, Integer>( );

    @Override
    public void printResults() {	
	System.out.println(StringUtils.repeat("-", 60));
	//three columns in the header
	System.out.printf("%-15s \t %-15s \t %-15s\n", "Minimum","Maximum", "Requests");
	System.out.println(StringUtils.repeat("-", 60));
	
	Set<Range> keys = results.keySet();
	
	for ( Range key : keys ) {
	    System.out.printf("%-15d \t %-15d \t %-15d\n",key.low, key.high, results.get( key ) );
	}
	
	System.out.println(StringUtils.repeat("-", 60));
    }
    
    private static class Range {
	int low;
	int high;
	
	public Range( int low, int high) {
	    this.low = low;
	    this.high = high;
	}
	
	public boolean inRange( int value ) {
	    return ( low <= value && value <= high );
	}
    }

    public HTTPRequestPerformance(String[] parameters) {
	super(parameters);
    }

    public void analyze(LogLine line) {
	//only if the line has the indicated attribute;
	if ( line.hasLogAttribute( LogAttribute.DURATION ) ) {
	    for ( Range range : ranges ) {
		//we are sure that it was an integer, since the process of adding them to LogLine is controlled in this way
		if ( range.inRange( ( Integer )line.getLogAttribute( LogAttribute.DURATION ) ) ) {
		    //increment the results
		    Integer result = results.get( range );
		    if ( result == null ) {
			result = 0;
		    }

		    result++;
		    results.put( range, result );
		    break;
		} 
	    }
	}
    }

    @Override
    protected void processParameters( String[] parameters ){
	//we need to parse the parameters; there should be three numerical:
	//first 2 indicate the range of duration in milliseconds; the third is the number of equal subranges in which the initial range is divided to calculate
	//the statistics
	
	if ( this.parameters == null || this.parameters.length != 3 ) {
	    throw new InvalidAnalysisConfigurationException(String.format("Invalid number of parameters for HTTPRequestPerformance Analysis. Found %d, expected 3!",
		    this.parameters == null ? 0 : this.parameters.length ) );
	}
	
	try {
	    int lbound = Integer.valueOf( parameters[0] );
	    int ubound = Integer.valueOf( parameters[1] );
	    int noOfRanges = Integer.valueOf( parameters[2] );
	    
	    if ( noOfRanges <= 0 ) {
		throw new InvalidAnalysisConfigurationException("The third parameter, indicating the number of ranges, should be a poztive integer!"); 
	    }
	    
	    if ( lbound < 0 || ubound < 0 ) {
		throw new InvalidAnalysisConfigurationException("The lower or upper bounds should pozitive integers!"); 
	    }
	    
	    if ( lbound >= ubound ) {
		throw new InvalidAnalysisConfigurationException("The upper bound should be greater than lower bound!"); 
	    }
	    
	    int rangeLength = ( ubound - lbound ) / noOfRanges;
	    
	    ranges = new ArrayList<Range>( );
	    //we wil create npOfRanges + 1 ranges: the last will be everything greater than ubound
	    for (int i = 0; i < noOfRanges; i++ ) {
		if ( lbound+rangeLength < ubound ) {
		    ranges.add(new Range( lbound, lbound+rangeLength ) );
		    lbound += rangeLength + 1;
		} else {
		    ranges.add( new Range( lbound, ubound ) );
		    break;
		}
	    }
	    
	    ranges.add( new Range( ubound + 1, Integer.MAX_VALUE));
	    
	} catch (NumberFormatException exNFE ) {
	    throw new InvalidAnalysisConfigurationException("All parameters for HTTPRequestPerformance analysis should be integer numbers"); 
	}
    }
}
