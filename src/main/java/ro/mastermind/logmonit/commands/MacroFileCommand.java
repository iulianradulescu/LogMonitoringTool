/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.mastermind.logmonit.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ro.mastermind.logmonit.operations.LogReportOperations;
import ro.mastermind.logmonit.config.Configuration;
import ro.mastermind.logmonit.exceptions.InvalidCommandException;

/**
 * special command, which executes the set of commands read from an input file specified as parameter
 *
 * @author radulescus
 */
public class MacroFileCommand extends UserCommand {

    private File commandFile;

    public MacroFileCommand(LogReportOperations ops, String[] parameters) {
	super(ops, parameters);
    }

    @Override
    public void validate() {
	//should have one and only one parameter and that should be a file that exists
	if (parameters == null || parameters.length != 1) {
	    throw new InvalidCommandException(String.format("Invalid number of parameters. Expected 1 found %d", parameters == null ? 0 : parameters.length));
	}

	commandFile = new File(parameters[0]);
	if (!commandFile.exists()) {
	    throw new InvalidCommandException(String.format("File %s specified as parameter should exists!", parameters[0]));
	}

	if (commandFile.isDirectory()) {
	    throw new InvalidCommandException(String.format("File %s specified as parameter should not be a directory!", parameters[0]));
	}
    }

    @Override
    public void execute() {
	UserCommandFactory factory = new UserCommandFactory();

	BufferedReader reader = null;
	try {
	    reader = new BufferedReader(new FileReader(Configuration.instance().getCommandFile()));

	    String line;
	    while ((line = reader.readLine()) != null) {
		if (line.startsWith("#")) {
		    //is a comment line; skip it;
		    continue;
		}
		UserCommand command = factory.makeUserCommand(super.ops, line);
		command.validateAndExecute();
	    }

	} catch (FileNotFoundException exFNFE) {
	    throw new InvalidCommandException(String.format("The command file %s was not found!", Configuration.instance().getCommandFile().getPath()));
	} catch (IOException exIOE) {
	    throw new InvalidCommandException(String.format("Command file %s could not be processed!", Configuration.instance().getCommandFile().getPath()));
	} finally {
	    if (reader != null) {
		try {
		    reader.close();
		} catch (IOException exIOE) {
		    //TODO
		}
	    }
	}
    }

}
