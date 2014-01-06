/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.enums;

import java.util.Date;

/**
 *
 * @author radulescu
 */
public enum LogAttribute {
    TIMESTAMP( Date.class ), 
    USERNAME( String.class ), 
    CLASSNAME( String.class ),
    URL( String.class ),
    DURATION( Integer.class ), 
    MESSAGE( String.class );
    
    private Class type;
    
    LogAttribute( Class type ) {
	this.type = type;
    }
    
    public Class type( ) {
	return this.type;
    }
    
    public boolean isOfType( Object value ) {
	try {
	    type.cast( value );
	    return true;
	} catch ( ClassCastException exCCE ) {
	    return false;
	}
    }
}
