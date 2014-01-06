/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.exceptions;

/**
 * Exception thrown when a command name is invalid
 * @author radulescu
 */
public class InvalidCommandException extends RuntimeException {
    
    public InvalidCommandException( String message ) {
	super( message );
    }
}
