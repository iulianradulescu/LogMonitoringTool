package ro.mastermind.logmonit.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ro.mastermind.logmonit.analysis.Analysis;
import ro.mastermind.logmonit.enums.LogAttribute;
import ro.mastermind.logmonit.filters.LogFilter;

/**
 * Base class for different types of log lines
 *
 * @author radulescu
 */
public class LogLine {

    /**
     * timestamp when the log line was written
     */
    protected Date timestamp;

    /**
     * the username under which the log lines was written
     */
    protected String username;

    protected String classname;

    protected Map<LogAttribute, Object> specificAttributes = new HashMap<LogAttribute, Object>();

    public boolean validate(LogFilter filter) {
	return filter.accept(this);
    }

    public boolean validate(List<LogFilter> filters) {
	for (LogFilter filter : filters) {
	    if (!validate(filter)) {
		return false;
	    }
	}
	return true;
    }

    public void analyze(Analysis analysis) {
	analysis.analyze(this);
    }

    public Date getTimestamp() {
	return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    public String getUsername() {
	return this.username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public void setClassname(String classname) {
	this.classname = classname;
    }

    public String getClassname() {
	return this.classname;
    }

    public Object getLogAttribute(LogAttribute attribute) {
	return specificAttributes.get(attribute);
    }

    public void setLogAttribute(LogAttribute attribute, Object value) {
	//check to see if the attribute's type corresponds to the object type; if not, throw an error
	if (attribute.isOfType(value)) {
	    //then add it to the map
	    specificAttributes.put(attribute, value);
	}
    }

    public boolean hasLogAttribute(LogAttribute attribute) {
	return specificAttributes.containsKey(attribute);
    }
}
