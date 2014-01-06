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
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.time.DateUtils;
import ro.mastermind.logmonit.operations.LogReportOperations;
import ro.mastermind.logmonit.enums.DateFormat;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;
import ro.mastermind.logmonit.filters.DateRangeFilter;

/**
 *
 * @author radulescu
 */
public class DefineDateRangeFilterCommand extends UserCommand { 
    
    private Date from;
    private Date to;
    
    public DefineDateRangeFilterCommand(LogReportOperations ops, String[] parameters ) {
	super(ops, parameters );
    }

    @Override
    public void execute( ) {
	ops.addFilter( new DateRangeFilter( from, to ) );
    }

    /**
     * this command accepts two parameters, from(date) and to(date). Both parameters should be present. In case the user
     * wants to specify only one date (from or to) it leaves the other one empty (e.g. to()). If no dates are specified, all logs are parsed
     * without any date filtering
     * 
     *  example: datefilter from(01.01.2013) to(()
     */
    @Override
    public void validate() {
	//2 and only 2 parameters
	if ( parameters == null || parameters.length != 2 ) {
	    throw new InvalidCommandException(String.format("Invalid number of parameters. Expected 2 found %d", parameters == null ? 0 : parameters.length));
	}
	
	//validate and parse each parameter
	from = validateParameter( parameters[0], "from");
	to = validateParameter( parameters[1], "to");
	
	if ( from != null && to != null ) {
	    //from must be less than to
	    if (from.after( to ) ) {
		throw new InvalidCommandException(String.format("from date must be before to date!"));
	    }
	}
	
    }
    
    /**
     * Validates and parse each parameter
     * @param parameter
     * @param paramName
     * @return 
     */
    protected Date validateParameter( String parameter, String paramName ) {
	String pattern = String.format("^%s\\(([0-9\\.\\-\\:\\s]*)\\)$",paramName);
	
	Pattern p = Pattern.compile( pattern );
	Matcher m = p.matcher( parameter );
	
	if ( !m.matches( ) ) {
	    throw new InvalidCommandException(String.format("Invalid %s parameter. Expected %s(date) or %s() and found %s", 
		    paramName, paramName, paramName, parameter));
	}
	
	String date = m.group(1);
	
	if ( date.trim( ).length( ) > 0 ) {	
	    DateFormat[] values = DateFormat.values();
	    for ( DateFormat value : values ) {
		try {
		    Locale locale = new Locale( "ro", "RO" );
		    SimpleDateFormat format = new SimpleDateFormat( value.format( ) );
		    Date result = format.parse( date );
		    
		    if ( value == DateFormat.CURRENT || value == DateFormat.SHORT ) {
			Date now = new Date( );		
			now = value == DateFormat.CURRENT ? DateUtils.truncate( now, Calendar.DATE ) : DateUtils.truncate( now, Calendar.YEAR ) ;
			
			result = DateUtils.addMilliseconds( now, ( int )(result.getTime( ) + TimeZone.getTimeZone("GMT+2").getRawOffset( )));
		    }
		    
		    return result;
		} catch ( ParseException exPE ) {
		    //invalid format of the date; try the next one
		}
	    }
	    //it reaches this when none of the formats were met
	    throw new InvalidCommandException(String.format("Invalid date format for %s parameter. Expected %s and found %s", 
					paramName, DateFormat.list(), parameters));
	}
	return null;
    }
}
