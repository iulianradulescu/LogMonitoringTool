/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ro.mastermind.logmonit.operations.LogReportOperations;
import ro.mastermind.logmonit.analysis.Analysis;
import ro.mastermind.logmonit.analysis.AnalysisFactory;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 *
 * @author radulescu
 */
public class RunAnalysisCommand extends UserCommand {

    private Analysis analysis;
    
    public RunAnalysisCommand(LogReportOperations ops, String[] parameters ) {
	super(ops, parameters );
    }

    @Override
    public void execute() {
	ops.runAnalysis( analysis );
    }
    
    public Analysis analysis( ) {
	return this.analysis;
    }

    @Override
    public void validate() {
	//it is only one parameter for the command
	//the parameter is in the form of <analysis>(<list_of_params);
	//examples: httpReq(0,10000, 5); sysLoad( ), userActivity(username).
	
	if ( parameters == null || parameters.length != 1 ) {
	    throw new InvalidCommandException(String.format("Invalid number of parameters. Expected 1 found %d", parameters == null ? 0 : parameters.length));
	}
	
	String format = "([a-zA-Z]+)\\((.*)\\)";
	
	Pattern p = Pattern.compile( format );
	Matcher m = p.matcher( parameters[0]);
	
	if ( m.matches( ) ){
	    //the first group is the statistics name while the rest are the parameters
	    String analysisName = m.group(1);
	    String paramString = m.group(2).trim();
	    
	    String[] parameters = paramString.length() == 0 ? null : paramString.split(",");
	    
	    AnalysisFactory factory = new AnalysisFactory( );
	    
	    analysis = factory.makeAnalysisObject( analysisName, parameters);
	} else {
	    throw new InvalidCommandException("The parameter did not match the expected pattern! It should correspond to a function call (i.e. systemLoad())");
	}
    }
    
}
