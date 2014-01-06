/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import ro.mastermind.logmonit.operations.LogReportOperations;
import ro.mastermind.logmonit.exceptions.QuitException;

/**
 *
 * @author radulescu
 */
public class QuitCommand extends UserCommand {

    public QuitCommand(LogReportOperations ops, String[] parameters) {
	super(ops, parameters);
    }

    @Override
    public void execute() {
	throw new QuitException( );
    }

    @Override
    public void validate() {
	//nothing to do
    }
    
}
