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
public class InvalidAnalysisConfigurationException extends RuntimeException {
    
    public InvalidAnalysisConfigurationException( String message ) {
	super( message );
    }
}
