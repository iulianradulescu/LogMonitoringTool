package ro.mastermind.logmonit.operations;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import ro.mastermind.logmonit.analysis.Analysis;
import ro.mastermind.logmonit.config.Configuration;
import ro.mastermind.logmonit.enums.FileType;
import ro.mastermind.logmonit.enums.ReportType;
import ro.mastermind.logmonit.filters.LogFilter;
import ro.mastermind.logmonit.model.LogFile;
import ro.mastermind.logmonit.model.LogReport;
import ro.mastermind.logmonit.parsers.FileParser;
import ro.mastermind.logmonit.parsers.FileParserFactory;

/**
 * Base class for defining the states of a LogReport processing
 *
 * @author radulescu
 */
public class State {

    public void defineReport(LogReportOperations ops, ReportType type) {
	throw new UnsupportedOperationException("Operation not allowed in this state!");
    }

    public void addFilter(LogReportOperations ops, LogFilter filter) {
	throw new UnsupportedOperationException("Operation not allowed in this state!");
    }

    public void parseLogData(LogReportOperations ops) {
	throw new UnsupportedOperationException("Operation not allowed in this state!");
    }

    public void runAnalysis(LogReportOperations ops, Analysis analysis) {
	throw new UnsupportedOperationException("Operation not allowed in this state!");
    }

    public static State initialState() {
	return new UninitializedState();
    }

    /**
     * In this state, which is the initial one, only a defineReport operation is allowed
     */
    static class UninitializedState extends State {

	@Override
	public void defineReport(LogReportOperations ops, ReportType type) {
	    ops.report(new LogReport(type, null));
	    ops.state(new ReportDefinedState());
	}
    }

    /**
     * in this state, we can either parse the log data or define filters
     */
    static class ReportDefinedState extends State {

	@Override
	public void addFilter(LogReportOperations ops, LogFilter filter) {
	    ops.report().addLogFilter(filter);
	    //state does not change
	}

	@Override
	public void parseLogData(LogReportOperations ops) {
	    //do the parsing
	    File[] files = listFiles(ops.report().getType());

	    //we will use 3 thread s to process the files
	    ExecutorService service = Executors.newFixedThreadPool( 5 );
	    
	    for (File file : files) {
		service.execute( new FileProcessorRunnable(file, ops.report()));
	    }
	    
	    //now we must wait for all to finish
	    service.shutdown();
	    try {
		service.awaitTermination( 60L, TimeUnit.MINUTES );
	    } catch (InterruptedException ex) {
		Logger.getLogger(State.class.getName()).log(Level.SEVERE, null, ex);
	    }

	    //print report status
	    System.out.println(ops.report());

	    //change the state
	    ops.state(new ParsedLogDataState());
	}
    }

    /**
     * in this state, we can: - change the filtering, so we go to ReportDEfinedState - run statistics
     */
    static class ParsedLogDataState extends State {

	@Override
	public void addFilter(LogReportOperations ops, LogFilter filter) {
	    ops.addFilter(filter);
	    //TODO: we should clear the parsed data
	    ops.state(new ReportDefinedState());
	}

	@Override
	public void runAnalysis(LogReportOperations ops, Analysis analysis) {
	    //run analysis
	    analysis.analyze(ops.report());

	    //print the results;
	    analysis.printResults();
	    //state does not change
	}
    }

    /**
     * helper method; it lists the log files which can be the source of information for the giving report report types
     *
     * @param reportType
     * @return
     */
    protected File[] listFiles(final ReportType reportType) {
	File logDirectory = Configuration.instance().getLogDirectory();

	return logDirectory.listFiles(new FilenameFilter() {

	    public boolean accept(File dir, String name) {
		FileType fileType = FileType.forName(name);
		if (fileType != null) {
		    return reportType.isAccepted(fileType);
		}

		return false;
	    }
	});
    }

    private static class FileProcessorRunnable implements Runnable {

	private File file;
	private LogReport report;

	public FileProcessorRunnable(File file, LogReport report) {
	    this.file = file;
	    this.report = report;
	}

	public void run() {
	    System.out.format("Process file %s on thread %s \n", file.getName(), Thread.currentThread().getName() );
	    FileParser parser = new FileParserFactory().createFileParser(FileType.forName(file.getName()));
	    LogFile logFile = parser.parse(file, report.getFilters());

	    if (logFile != null) {
		report.addFile(logFile);
	    }
	}

    }
}
