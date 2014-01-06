/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ro.mastermind.logmonit.analysis;

import java.util.List;
import ro.mastermind.logmonit.model.LogFile;
import ro.mastermind.logmonit.model.LogLine;
import ro.mastermind.logmonit.model.LogReport;

/**
 * Interface implemented by all custom analysis to be performed on a logReport. It is used to implement the Visitor pattern
 * @author radulescu
 */
public abstract class Analysis {
    
    protected String[] parameters;
    
    /** apply the analysis at LogLine level*/
    public abstract void analyze( LogLine line );
    
    protected abstract void processParameters( String[] parameters ); 
    
    /**
     * default implementation; if subclasses  needs to do something different then it can be overridden
     * @param file 
     */
    public void analyze(LogFile file) {
	List<LogLine> lines = file.lines();
	for ( LogLine line : lines ) {
	    line.analyze( this );
	}
    }
    
    /**
     * default implementation; if subclasses  needs to do something different then it can be overridden
     * @param report 
     */
    public void analyze( LogReport report ) {
	List<LogFile> files = report.files();
	
	for ( LogFile file : files ) {
	    file.analyze( this );
	}
    }
    
    public abstract void printResults( );
    
    public Analysis( String[] parameters ) {
	this.parameters = parameters;
	
	processParameters( this.parameters );
    }
    
    public String[] parameters( ) {
	return this.parameters;
    }    
}
