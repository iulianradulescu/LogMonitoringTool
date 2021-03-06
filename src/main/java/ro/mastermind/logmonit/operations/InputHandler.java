/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.mastermind.logmonit.operations;

import java.util.Scanner;
import ro.mastermind.logmonit.commands.UserCommand;
import ro.mastermind.logmonit.commands.UserCommandFactory;
import ro.mastermind.logmonit.config.Configuration;
import ro.mastermind.logmonit.exceptions.QuitException;

/**
 * Singleton class to handle the input and execute appropriate commands; it also manages the whole context of operations
 * ( the reference to LogReportOperations)
 *
 * @author radulescu
 */
public class InputHandler {

    private LogReportOperations ops;
    private UserCommandFactory factory;

    private static class InputHandlerInstanceHolder {

	private static InputHandler INSTANCE = new InputHandler();
    }

    private InputHandler() {
	ops = new LogReportOperations();
	factory = new UserCommandFactory();
    }

    public static InputHandler instance() {
	return InputHandlerInstanceHolder.INSTANCE;
    }

    private UserCommand handleInput() {
	//promp the user
	promptUser();

	//read the input
	Scanner sc = new Scanner(System.in);
	String input = sc.nextLine();

	return factory.makeUserCommand(ops, input);
    }

    private void promptUser() {
	System.out.print("> ");
    }

    public void handleAllInputs() {
	//if an execution file is specified in command-line, parse it and execute those commands as a MacroCommand;
	//otherwise handle the input from keyboard
	try {
	    if (Configuration.instance().getCommandFile() != null && Configuration.instance().getCommandFile().exists()) {
		//make a MacroFileCommand command to handle the file
		UserCommand macro = factory.makeUserCommand(ops, String.format("runfile %s", Configuration.instance().getCommandFile().getPath()));

		//run the command
		macro.validateAndExecute();
	    }

	    //continue with the standard input; it will exit via a QuitException generated by a QuitCommand
	    while ( true ) {
		UserCommand command = handleInput();
		command.validateAndExecute();
	    }
	} catch (QuitException exQE) {
	    //gracefully ended it; nothing to do hear
	}
    }
}
