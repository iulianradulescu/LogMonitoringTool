package ro.mastermind.logmonit.operations;

import ro.mastermind.logmonit.analysis.Analysis;
import ro.mastermind.logmonit.enums.ReportType;
import ro.mastermind.logmonit.filters.LogFilter;
import ro.mastermind.logmonit.model.LogReport;

/**
 * Class that provides the context for executing the operations on a log report according to the states defined, as response to the commands 
 * @author radulescu
 */
public class LogReportOperations {
 
    private LogReport report;
    
    /** current state*/
    private State currentState = State.initialState( );
    
    public void defineReport( ReportType type ) {
	currentState.defineReport( this, type );
    }
    
    public void addFilter( LogFilter filter ) {
	currentState.addFilter(this, filter);
    }
    
    public void parseLogData( ) {
	currentState.parseLogData( this );
    }
    
    public void runAnalysis( Analysis analysis ) {
	currentState.runAnalysis(this, analysis  );
    }
    
    public void report( LogReport report ) {
	this.report = report;
    }
    
    public LogReport report( ) {
	return this.report;
    }
    
    public void state( State newState ) {
	this.currentState = newState;
    }
}
