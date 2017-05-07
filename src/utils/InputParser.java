package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import commands.Command;
import commands.Exit;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import driver.JShell;
import exceptions.InvalidCommandException;
import exceptions.InvalidNumberException;
import writer.ConsoleWriter;
import writer.FileWriter;
import writer.Writer;

/**
 * This class consists of the static methods required to parse the String input
 * into a Command.
 * 
 * @author Brendan Zhang
 *
 */
public class InputParser {
  private static final String ALIAS_KEYWORD = "!";
  private JShell shell;
  private BufferedReader bufferedReader;
  private FileSystem fileSystem;
  private String errorMessage;

  public InputParser(JShell shell) {
    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    this.shell = shell;
    this.fileSystem = JFileSystem.getFileSystem();
    this.errorMessage = null;
  }

  /**
   * Parses the input String into keywords and arguments and returns the
   * corresponding Command.
   * 
   * @param input String that represents a Command
   * @return Command that corresponds to the input
   * @throws InvalidCommandException
   * @throws InvalidNumberException
   */
  public Command parseInput(String input)
      throws InvalidCommandException, InvalidNumberException {

    // Split command by whitespace
    String[] commandArgumentList = input.trim().split("\\s+");

    // Empty command
    if (commandArgumentList.length == 0) {
      throw new InvalidCommandException(input);
    }

    Command command = null;
    String keyword = commandArgumentList[0];
    // Arguments don't contain keyword. Remove it.
    String[] arguments = InputParser.removeElement(commandArgumentList, 0);

    // Empty keyword
    if (keyword.length() == 0) {
      return null;
    }

    // Don't add commands with "!" to input log
    if (!input.contains(ALIAS_KEYWORD)) {
      // Add the command to the input log
      fileSystem.getInputLog().add(input);
    } else {
      // Parse for alias keyword.
      return parseInput(parseAlias(input));
    }

    try {
      if (CommandTable.getCommandTable().get(keyword) != null) {
        command = (Command) Class
            .forName(CommandTable.getCommandTable().get(keyword)).newInstance();
      }
    } catch (InstantiationException ie) {
      System.out.println("An InstantiationException has occurred: " + ie);
    } catch (IllegalAccessException iae) {
      System.out.println("An IllegalAccessException has occurred: " + iae);
    } catch (ClassNotFoundException cnfe) {
      System.out.println("A ClassNotFoundException has occurred: " + cnfe);
    }

    if (command == null) {
      setErrorMessage(new InvalidCommandException(input).getMessage());
      throw new InvalidCommandException(input);
    } else if (command instanceof Exit) {
      // Special case for Exit because it requires JShell parameters
      command = new Exit(shell);
      return command;
    } else {
      command.setArgs(arguments);
    }

    // Sets the Writer for the command and then returns the Command
    return this.setWriterForCommand(command);
  }

  /**
   * Gets the next command from the user through standard input and parses the
   * Command and arguments. Creates the command corresponding to the user input
   * and returns the Command.
   * 
   * @return Command that corresponds to the input
   */
  public Command getCommand() {
    try {
      return parseInput(bufferedReader.readLine());
    } catch (IOException ioe) {
      System.out.println("An I/O Exception has occurred: " + ioe);
    } catch (InvalidCommandException ice) {
      System.out.println(ice.getMessage());
    } catch (InvalidNumberException ine) {
      System.out.println(ine.getMessage());
    }
    return null;
  }

  public FileSystem getFileSystem() {
    return fileSystem;
  }

  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }


  /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * Returns a new String array that is equal to arr with the element at the
   * specified index removed.
   * 
   * @param arr The String array to remove the elements from
   * @param index The index of the element to remove
   * @return The String array arr with the specified element removed
   */
  private static String[] removeElement(String[] arr, int index) {
    List<String> arrayAsList = new ArrayList<String>(Arrays.asList(arr));
    arrayAsList.remove(index);
    String[] arguments = (String[]) arrayAsList.toArray(new String[0]);
    return arguments;
  }


  /**
   * Returns a new String array that is equal to arr with the element from index
   * start up to index end (but not including) removed.
   * 
   * @param arr The String array to remove the elements from
   * @param start The starting index of the elements to remove from
   * @param end The ending index of the elements to remove from
   * @return The String array arr with the specified elements removed
   */
  private static String[] removeElement(String[] arr, int start, int end) {
    List<String> arrayAsList = new ArrayList<String>(Arrays.asList(arr));
    // Remove from start to end (not including end).
    for (int i = 0; i < end - start; i++) {
      arrayAsList.remove(start);
    }
    String[] arguments = (String[]) arrayAsList.toArray(new String[0]);
    return arguments;
  }

  /**
   * Returns a String that is equivalent to str with each substring in the form
   * of "!n", where n is an integer, is replaced by the nth command in the input
   * log. Otherwise, it returns str.
   * 
   * @param str The String to parse for the Alias keyword
   * @return String equal to str with "!n" replaced by the nth command in the
   *         input log.
   * @throws InvalidCommandException If "!" isn't followed by a number
   * @throws InvalidNumberException If the number that follows "!" is either <0
   *         or >= size of the input log.
   */
  private String parseAlias(String str)
      throws InvalidNumberException, InvalidCommandException {

    int lastAlias = 0;
    lastAlias = str.indexOf(ALIAS_KEYWORD);
    // If str contains alias keyword, get n by checking each character after the
    // alias keyword to see if it is numeric until reaching a non-numeric
    // character.
    if (lastAlias >= 0) {
      int startNumber = lastAlias + 1;
      int endNumber = startNumber;
      while (endNumber < str.length()) {
        if (isNumeric(str.substring(endNumber, endNumber + 1))) {
          endNumber++;
        } else {
          break;
        }
      }
      int n = Integer.parseInt(str.substring(startNumber, endNumber));

      // Substitute !n for nth command in input log and then parseAlias on the
      // new string
      return parseAlias(str.substring(0, lastAlias)
          + fileSystem.getInputLog().get(n - 1) + str.substring(endNumber));
    } else {
      // If str doesn't contain alias keyword, just return str.
      return str;
    }
  }

  /**
   * Sets the Writer for the Command, based on its command arguments.
   * 
   * @param command The Command to set the Writer of
   * @return The Command with its writer set
   */
  private Command setWriterForCommand(Command command) {
    final String appendString = ">>";
    final String writeString = ">";
    // Set writer (and remove last 2 arguments if writing to File)
    if (command != null) {
      Writer writer = null;
      if (command.getArgs().length >= 2) {
        String lastArgument = command.getArgs()[command.getArgs().length - 1];
        String secondLastArgument =
            command.getArgs()[command.getArgs().length - 2];
        if (secondLastArgument.equals(writeString)) {
          command.setArgs(InputParser.removeElement(command.getArgs(),
              command.getArgs().length - 2, command.getArgs().length));
          writer =
              new FileWriter(lastArgument, false, JFileSystem.getFileSystem());
        } else if (secondLastArgument.equals(appendString)) {
          command.setArgs(InputParser.removeElement(command.getArgs(),
              command.getArgs().length - 2, command.getArgs().length));
          writer =
              new FileWriter(lastArgument, true, JFileSystem.getFileSystem());
        }
      }
      if (writer == null) {
        writer = new ConsoleWriter();
      }
      command.setWriter(writer);
    }
    return command;
  }

  /**
   * Returns true iff str is numeric.
   * 
   * @param str The string to check if it is numeric.
   * @return <tt>true</tt> if the str is numeric. Otherwise, return
   *         <tt>false</tt>.
   */
  private boolean isNumeric(String str) {
    {
      try {
        Integer.parseInt(str);
        return true;
      } catch (Exception e) {
        return false;
      }
    }
  }
}
