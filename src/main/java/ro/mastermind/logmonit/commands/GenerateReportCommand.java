/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import ro.mastermind.logmonit.operations.LogReportOperations;

/**
 *
 * @author radulescu
 */
public class GenerateReportCommand extends UserCommand {

    public GenerateReportCommand(LogReportOperations ops, String[] parameters ) {
	super(ops, parameters);
    }

    @Override
    public void execute() {
	//parse the date; it will populate ops.LogReport with the corresponding information
	ops.parseLogData();
    }

    @Override
    public void validate() {
	//the operation does not have parameters, so there is no need for validations.
    }
    
}
