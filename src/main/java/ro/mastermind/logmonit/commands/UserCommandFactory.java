/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.mastermind.logmonit.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import ro.mastermind.logmonit.operations.LogReportOperations;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 * Factory class for creating user commands based on the input received from user. Implementation of singleton patterns
 *
 * @author radulescu
 */
public class UserCommandFactory {

    /**
     * interface for anonymous factory classes
     */
    private static interface UserCommandFactoryCommand {

	UserCommand create(LogReportOperations ops, String[] parameters);
    }

    private Map<String, UserCommandFactoryCommand> commandMap = new HashMap<String, UserCommandFactoryCommand>();

    public UserCommandFactory() {
	//populate the list of commands
	commandMap.put("report", new UserCommandFactoryCommand() {

	    public UserCommand create(LogReportOperations ops, String[] parameters) {
		return new DefineReportCommand(ops, parameters);
	    }

	});

	commandMap.put("datefilter", new UserCommandFactoryCommand() {

	    public UserCommand create(LogReportOperations ops, String[] parameters) {
		return new DefineDateRangeFilterCommand(ops, parameters);
	    }

	});

	commandMap.put("serverfilter", new UserCommandFactoryCommand() {

	    public UserCommand create(LogReportOperations ops, String[] parameters) {
		return new DefineServerFilterCommand(ops, parameters);
	    }

	});

	commandMap.put("generate", new UserCommandFactoryCommand() {

	    public UserCommand create(LogReportOperations ops, String[] parameters) {
		return new GenerateReportCommand(ops, parameters);
	    }

	});
	
	commandMap.put("analyze", new UserCommandFactoryCommand() {

	    public UserCommand create(LogReportOperations ops, String[] parameters) {
		return new RunAnalysisCommand(ops, parameters);
	    }

	});
	
	commandMap.put("runfile", new UserCommandFactoryCommand() {

	    public UserCommand create(LogReportOperations ops, String[] parameters) {
		return new MacroFileCommand(ops, parameters);
	    }

	});
	
	commandMap.put("help", new UserCommandFactoryCommand() {

	    public UserCommand create(LogReportOperations ops, String[] parameters) {
		return new HelpCommand(ops, parameters);
	    }

	});
	
	commandMap.put("list", new UserCommandFactoryCommand() {

	    public UserCommand create(LogReportOperations ops, String[] parameters) {
		return new ListCommand(ops, parameters);
	    }

	});
	
	commandMap.put("quit", new UserCommandFactoryCommand() {

	    public UserCommand create(LogReportOperations ops, String[] parameters) {
		return new QuitCommand(ops, parameters);
	    }

	});
    }

    public UserCommand makeUserCommand(LogReportOperations ops, String inputString) {
	if (inputString.isEmpty()) {
	    //it is a QuitCommand
	    return new QuitCommand(ops, null);
	}

	//parse the info; elements of the command are separated by spaces, first should be the actual command and the rest are the
	//parameters;
	String[] elements = inputString.split(" ");

	//get the appropriate command factory
	UserCommandFactoryCommand factoryCommand = commandMap.get(elements[0]);
	if (factoryCommand == null) {
	    //invalid command
	    throw new InvalidCommandException(String.format("Invalid command specified: %s",elements[0]));
	}
	
	if ( elements.length == 1 ) {
	    return factoryCommand.create(ops, null );
	} else {
	    return factoryCommand.create(ops, Arrays.copyOfRange( elements, 1, elements.length ));
	}
    }
    
    public Set<String> list( ) {
	 return commandMap.keySet();
    }
}
