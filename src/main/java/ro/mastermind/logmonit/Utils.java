/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * utility class for different helper methods 
 * @author radulescu
 */
public class Utils {
    
    /**
     * extracts the server name part from the filename
     * @param filename
     * @return 
     */
    public static String getServerName( String filename ) {
	//get the index of .log extension and the index of the lasy hypen
	int extension = filename.indexOf(".log");
	int hypen = filename.lastIndexOf("-");
	
	if ( extension == - 1 || hypen >= extension ) {
	    return null;
	} 
	
	return filename.substring(hypen+1, extension );
    }
    
    /**
     * utility methods that converts a string in dd.MM.yyyy HH:mm:ss,S format in a Date object or returns null if it cannot do it
     * @param dateString
     * @return 
     */
    public static Date convert( String dateString ) {
	try {
	    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss,S");
	    return format.parse(dateString);
	} catch (ParseException eXPE) {
	    return null;
	}
    }
}
