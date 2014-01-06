/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.exceptions;

/**
 *
 * @author radulescu
 */
public class LogParsingException extends RuntimeException {
    public LogParsingException( String message ) {
	super( message );
    }
}
