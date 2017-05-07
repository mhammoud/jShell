package commands;

import java.util.ArrayList;

import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.InvalidArgumentsException;
import utils.CommandTable;
import writer.ConsoleWriter;

/**
 * The History Command retrieves the n latest used commands in the shell. If no
 * specified integer n is provided, all the commands used in chronological order
 * is provided. Every command is kept track of - including invalid inputs.
 * 
 * @author Mohamed Hammoud
 * @author Brendan Zhang
 * 
 */
public class History extends Command {

  /**
   * Holds the logs of the commands
   */
  private ArrayList<String> commandLogs;
  /**
   * The FileSystem that History will use
   */
  private FileSystem fileSystem;

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the History command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "history" + "\n\n" + "SYNOPSIS" + "\n\t"
        + "history [number]" + "\n\n" + "DESCRIPTION" + "\n\t"
        + "Prints out the recent commands, where the "
        + "first line is the oldest command recorded and the\n\tlast line is "
        + "the most recent line (inlcuding this call). This record includes "
        + "commands\n\tthat produced an error. If given a number x >= 0, will "
        + "give the last x commands" + "\n\n" + "EXAMPLE" + "\n\t"
        + "The user has previously typed in 'cat f1', "
        + "'cat f2', 'mkdir abc', 'pwd', in this order." + "\n\t" + "Example 1:"
        + "\n\t\t" + "history"
        + "\n\t\t1. cat f1\n\t\t2. cat f2\n\t\t3. mkdir abc\n\t\t4. pwd"
        + "\n\t\t5. history" + "\n\n\t" + "Example 2:" + "\n\t\t" + "histroy 2"
        + "\n\t\t4. pwd\n\t\t5. history 2";
  }

  /**
   * Default class constructor. Sets the command arguments to null, the output
   * to empty string, the fileSystem to {@link JFileSystem#getFileSystem()}, and
   * the commandLogs to the InputLog from fileSystem.
   */
  public History() {
    setFileSystem(JFileSystem.getFileSystem());
    this.commandLogs = fileSystem.getInputLog();
    this.setArgs(null);
    setWriter(new ConsoleWriter());
  }

  /**
   * Class constructor thats sets the the output to empty string, command
   * arguments to the specified arguments, the fileSystem to the specified
   * FileSystem, and the commandLogs to the InputLog from fileSystem.
   * 
   * @param args
   * @param fileSystem
   */
  public History(String[] args, FileSystem fileSystem) {
    this.setArgs(args);
    setFileSystem(fileSystem);
    this.commandLogs = fileSystem.getInputLog();
    setWriter(new ConsoleWriter());
  }

  /**
   * Executes the "history" command. Prints the output of the command and sets
   * any error message.
   */
  @Override
  public void executeCommand() {
    try {
      history();
    } catch (InvalidArgumentsException iae) {
      setErrorMessage(iae.getMessage());
      System.out.println(getErrorMessage());
    }
    getWriter().setToWrite(getOutput());
    getWriter().write();
  }

  /**
   * Executes the "history" command. If no arguments are passed, shows the
   * entire command log. Otherwise, if an integer n is passed as an argument,
   * shows the last n commands in the command log.
   * 
   * @throws InvalidArgumentsException If more than 1 argument
   */
  public void history() throws InvalidArgumentsException {
    int linesToShow = commandLogs.size();

    // One number argument
    if (getArgs().length == 1 && getArgs()[0].matches("\\d+")) {
      linesToShow = Integer.parseInt(getArgs()[0]);
      // No argument
    } else if (getArgs().length == 0) {
      this.commandLogs = fileSystem.getInputLog();
      linesToShow = commandLogs.size();
    } else {
      // Create error
      throw InvalidArgumentsException
          .createInvalidArgumentsException(CommandTable.HISTORY, getArgs());
    }
    // If no error, output history
    if (getOutput().equals("")) {
      int size = this.commandLogs.size();
      // the Index to start from
      int start = size - linesToShow;
      if (start < 0) {
        start = 0;
      }

      // Print each command with number
      for (int i = start; i < size; i++) {
        String currLogEntry = this.commandLogs.get(i);
        // Don't add new line for last line
        if (i != size - 1) {
          setOutput(getOutput() + (i + 1) + "." + currLogEntry + "\n");
        } else {
          setOutput(getOutput() + (i + 1) + "." + currLogEntry);
        }
      }
    }
  }

  /**
   * Gets the fileSystem
   * 
   * @return fileSystem the fileSystem to set
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * Sets the fileSystem
   * 
   * @param fileSystem The fileSystem to set to
   */
  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }
}

