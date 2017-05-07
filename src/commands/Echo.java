package commands;

import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.InvalidArgumentsException;
import exceptions.InvalidNameException;
import utils.CommandTable;
import writer.ConsoleWriter;

/**
 * This class consists of methods and variables required to execute the "echo"
 * command to print the string arguments to the console or to a File.
 * 
 * The Echo Command takes a String argument with surrounding double quotes (")
 * as the String to print. It can then take two optional arguments to print the
 * message, the first being the write-method to the File. If the write-method is
 * ">", the Echo command will overwrite the File if it exists. If the
 * write-method is ">>", the Echo command will append to the File if it exists.
 * The second optional argument is the path to the file.
 * 
 * @author Brendan Zhang
 *
 */
public class Echo extends Command {
  /**
   * A FileSystem that the Echo command will be using to write to a File, if
   * necessary
   */
  private FileSystem fileSystem;

  /**
   * Default class constructor that sets the output and printToFile to empty
   * String, sets toWhere and the command arguments to null, and sets fileSystem
   * to {@link JFileSystem#getFileSystem()}.
   * 
   * @param arguments The arguments to set to
   * @param fileSystem The FileSystem to set to
   */
  public Echo() {
    this.setOutput("");
    this.setArgs(null);
    this.setFileSystem(JFileSystem.getFileSystem());
    setWriter(new ConsoleWriter());
  }

  /**
   * Class constructor that sets the output and printToFile to empty String,
   * sets toWhere to null, and the arguments and FileSystem to the specified
   * ones.
   * 
   * @param arguments The arguments to set to
   * @param fileSystem The FileSystem to set to
   */
  public Echo(String[] args, FileSystem fileSystem) {
    this.setArgs(args);
    this.setOutput("");
    this.setFileSystem(fileSystem);
    setWriter(new ConsoleWriter());
  }

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the Echo command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "echo - outputs string to standard output" + "\n\n"
        + "SYNOPSIS" + "\n\t" + "echo [STRING...] [> FILENAME]" + "\n\t"
        + "echo [STRING...] [>>FILENAME]" + "\n\n" + "DESCRIPTION" + "\n\t"
        + "Given a string as an argument, Echo outputs the\n\t"
        + "string to the standard output or to a file.\n\n" + "EXAMPLES"
        + "\n\t"
        + "1) Inputting 'echo pork' will output 'pork' to the console.\n\t"
        + "2) Inputting 'echo pork > pork'\n\t"
        + "would either make a new file called 'pork' in the\n\t"
        + "working directory with text 'pork', or replace the\n\t"
        + "contents of the file in the working directory\n\t"
        + "called pork with text 'pork'.\n\t"
        + "3) If there was an existing file called 'pork' in the\n\t"
        + "directory '\\abc\\', inputting 'echo pork >> \\abc\\pork'\n\t"
        + "would append 'pork' to the next line of the file\n\t"
        + "named 'pork' in the directory \\abc\\, instead of replacing\n\t"
        + "the contents of the file. If there was not an existing file,\n\t"
        + "it would create one.";
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

  /**
   * Executes the "echo" command. If the number of arguments is invalid, print
   * an {@link InvalidArgumentsException} error message. If the name of the File
   * to write to is invalid, prints an {@link InvalidNAmeException} error
   * message.
   */
  @Override
  public void executeCommand() {
    try {
      echo(getArgs());
    } catch (InvalidArgumentsException iae) {
      setErrorMessage(iae.getMessage());
      System.out.println(iae.getMessage());
    }
    getWriter().setToWrite(getOutput());
    getWriter().write();
  }

  /**
   * Executes the"echo" command. If the number of arguments is equal to 0, throw
   * an InvalidArgumentsException.
   * 
   * @throws InvalidNameException If the name of the File to write/append to is
   *         invalid.
   * @throws InvalidArgumentsException If the number of arguments is equal to 0
   *         or 2
   */

  private void echo(String[] args) throws InvalidArgumentsException {

    if (args.length > 0) {
      String toEcho = "";
      for (int i = 0; i < args.length; i++) {
        toEcho = (toEcho + (args[i]) + " ");
      }
      setOutput(removeLastSpaceAndQuotationMarks(toEcho));
    } else {
      // Arguments have length 0. Invalid number of arguments for Echo
      // command.
      throw InvalidArgumentsException
          .createInvalidArgumentsException(CommandTable.ECHO, getArgs());
    }
  }

  /**
   * Returns a new String that is equal to str with trailing space and the
   * surrounding quotation marks removed. If there are no quotation marks,
   * returns null.
   * 
   * @param str String to remove space and quotation marks
   * @return String that is equal to str with surrounding quotation marks
   *         removed. If str doesn't contain surrounding quotation marks,
   *         returns <tt>null</tt>.
   * @throws InvalidArgumentsException If str doesn't contain surrounding
   *         quotation marks or number of quotation marks is not equal to 2.
   */
  private String removeLastSpaceAndQuotationMarks(String str)
      throws InvalidArgumentsException {
    if (str.length() >= 2) {
      // Remove last space and quotation marks
      str = (str.substring(0, str.length() - 1));
      return removeQuotationMarks(str);
    }
    throw InvalidArgumentsException
        .createInvalidArgumentsException(CommandTable.ECHO, getArgs());
  }

  /**
   * Returns a new String that is equal to str with the surrounding quotation
   * marks removed. If there are no surrounding quotation marks or number of
   * quotation marks is not equal to 2, throws an InvalidArgumentsException.
   * 
   * @param str String to remove quotation marks
   * @return String that is equal to str with surrounding quotation marks
   *         removed.
   * @throws InvalidArgumentsException If str doesn't contain surrounding
   *         quotation marks or number of quotation marks is not equal to 2.
   */
  private String removeQuotationMarks(String str)
      throws InvalidArgumentsException {
    // Get number of occurrences of quotation marks
    int numQuotationMarks = str.length() - str.replace("\"", "").length();
    // Remove the quotation marks at beginning and end
    if (numQuotationMarks == 2 && str.substring(0, 1).equals("\"")
        && str.substring(str.length() - 1, str.length()).equals("\"")) {
      str = str.substring(1, str.length() - 1);
      return str;
    }
    // Throw exception if there aren't quotation marks at beginning and end or
    // if number of quotation marks is not equal to two.
    throw InvalidArgumentsException
        .createInvalidArgumentsException(CommandTable.ECHO, getArgs());
  }

}
