package commands;

import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.FileNotFoundException;
import exceptions.InvalidArgumentsException;
import utils.CommandTable;
import writer.ConsoleWriter;

/**
 * This class consists of methods and variables required to execute the "cd"
 * command to change the current directory.
 * 
 * The ChangeDirectory Command changes the current directory of the FileSystem
 * to a given Directory. If the directory doesn't exist, output a PathError.
 * 
 * @author Brendan Zhang
 * 
 */
public class ChangeDirectory extends Command {

  /**
   * The FileSystem that the current Directory will change
   */
  private FileSystem fileSystem;

  /**
   * Default class constructor. Sets the FileSystem to
   * {@link JFileSystem#getFileSystem()}, sets the output to an empty String,
   * and sets args to null.
   */
  public ChangeDirectory() {
    setArgs(null);
    setFileSystem(JFileSystem.getFileSystem());
    setWriter(new ConsoleWriter());
  }

  /**
   * Class constructor that sets the output to empty String and the arguments
   * and FileSystem to the specified ones.
   * 
   * @param arguments The arguments to set to
   * @param fileSystem The FileSystem to set to
   */
  public ChangeDirectory(String[] arguments, FileSystem fileSystem) {
    setArgs(arguments);
    setFileSystem(fileSystem);
    setWriter(new ConsoleWriter());
  }

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the ChangeDirectory command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "cd - change directory" + "\n\n" + "SYNOPSIS"
        + "\n\t" + "cd DIR" + "\n\n" + "DESCRIPTION" + "\n\t"
        + "Changes the current working directory " + "to the given DIR."
        + "\n\tThe DIR given can either be a full path "
        + "or be reletive to the current one." + "\n\n" + "EXAMPLE" + "\n\t"
        + "Change the current directory to be in abc "
        + "where the current directory is\n\tcurDir and curDir is in base "
        + "directory base. Either example is fine:" + "\n\t\t"
        + "cd /base/curDir/abc" + "\n\t\t" + "cd abc\n";
  }

  /**
   * Executes the "cd" command.
   * 
   * @throws FileNotFoundException
   */
  @Override
  public void executeCommand() {
    try {
      changeDirectory(getArgs());
    } catch (InvalidArgumentsException iae) {
      // Number of arguments is not equal to 1
      setErrorMessage(iae.getMessage());
      System.out.println(getErrorMessage());
    } catch (FileNotFoundException fnfe) {
      // Directory couldn't be found
      setErrorMessage(fnfe.getMessage());
      System.out.println(getErrorMessage());

    }
  }

  /**
   * Changes to the specified directory.
   * 
   * @param arguments String array that contains the arguments of this
   *        ChangeDirectory Command
   * @throws InvalidArgumentsException If there are more than arguments for this
   *         ChangeDirectory command
   * @throws FileNotFoundException If the Directory can not be found
   */
  private void changeDirectory(String[] arguments)
      throws InvalidArgumentsException, FileNotFoundException {

    if (arguments.length != 1) {
      throw InvalidArgumentsException.createInvalidArgumentsException(
          CommandTable.CHANGE_DIRECTORY, arguments);
    }

    fileSystem.setCurrentDirectory(
        Directory.getDirectoryFromPath(arguments[0], fileSystem));
  }

  /**
   * Gets the FileSystem.
   * 
   * @return The FileSystem to use
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * 
   * /** Sets the FileSystem.
   * 
   * @param fileSystem The FileSystem to set to
   */
  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }
}
