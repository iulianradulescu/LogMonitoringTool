/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.exceptions;

/**
 * Exception thrown when something is wrong in the configuration of the application (command-line arguments or information loaded from config files)
 * @author radulescu
 */
public class InvalidConfigurationException extends RuntimeException {
    
    public InvalidConfigurationException( String message ) {
	super( message );
    }
}
