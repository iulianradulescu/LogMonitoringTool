/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum type to describe acceptable date formats for filtering
 * @author radulescu
 */
public enum DateFormat {
    EXTENDED("dd.MM.yy-HH:mm"), //complete format
    SHORT("dd.MM-HH:mm"), //assume the current year
    CURRENT("HH:mm"); //within the current day
    
    private String format;
    
    DateFormat( String format ) {
	this.format = format;
    }
    
    public String format( ) {
	return this.format;
    }
    
    /**
     * list of strings representing the date formats
     * @return 
     */
    public static List<String> list( ) {
	ArrayList<String> list = new ArrayList<String>( );
	
	for ( DateFormat value : DateFormat.values( ) ){
	    list.add( value.format( ) );
	}
	
	return list;
    }
}
