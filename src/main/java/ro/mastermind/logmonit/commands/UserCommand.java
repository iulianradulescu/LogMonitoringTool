package ro.mastermind.logmonit.commands;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import ro.mastermind.logmonit.operations.LogReportOperations;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 * Base class for user commands
 *
 * @author radulescu
 */
public abstract class UserCommand {

    protected LogReportOperations ops;
    protected String[] parameters;

    public UserCommand(LogReportOperations ops, String[] parameters) {
	this.ops = ops;
	this.parameters = parameters;
    }

    public abstract void validate();

    public abstract void execute();

    public void validateAndExecute() {
	try {
	    //validate the command
	    validate();

	    //execute the command
	    execute();
	} catch (InvalidCommandException exICE) {
	    //show a message and re run the hadnleInput method
	    writeErrorMessage(exICE.getMessage());
	}
    }

    public void writeOutput(String output) {
	System.out.println(output);
    }

    protected String readString(String output) {
	System.out.print(output);
	Scanner sc = new Scanner(System.in);
	return sc.nextLine();
    }

    protected int readInt(String output) {
	System.out.print(output);
	Scanner sc = new Scanner(System.in);
	try {
	    return sc.nextInt();
	} catch (Exception ex) {
	    //try again
	    writeOutput("Invalid input, it should be a number! Try again!");
	    return readInt(output);
	}
    }

    protected Date readDate(String output, String format) {
	System.out.print(String.format("%s - format[%s]", output, format));
	Scanner sc = new Scanner(System.in);
	try {
	    String dateString = sc.nextLine();

	    if (dateString.length() == 0) {
		//it means no value is entered; so return null
		return null;
	    } else {
		return new SimpleDateFormat(format).parse(dateString);
	    }
	} catch (Exception ex) {
	    writeOutput("Invalid input, it should match the date format specified!. Try again!");
	    return readDate(output, format);
	}
    }

    private void writeErrorMessage(String message) {
	System.out.println(String.format("ERR: %s", message));
    }
}
