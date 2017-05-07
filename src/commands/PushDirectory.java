package commands;

import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.FileNotFoundException;
import exceptions.InvalidArgumentsException;
import utils.CommandTable;
import utils.DirectoryStack;
import writer.ConsoleWriter;

/**
 * A command pushd pushes the current working directory onto the directory stack
 * and changes the current working directory to the given directory (the given
 * directory can be both absolute/relative).
 * 
 * @author Mohamed Hammoud
 * @author Brendan Zhang
 *
 */
public class PushDirectory extends Command {

  /**
   * The DirectoryStack that stores the saved Directories
   */
  private DirectoryStack savedDirectories;

  /**
   * The FileSystem that the PushDirectory command will be using. PushDirectory
   * will change the current Directory and put the Directory onto the top of the
   * DirectoryStack of this FileSystem.
   */
  private FileSystem fileSystem;

  /**
   * Default class constructor. Sets fileSystem to
   * {@link JFileSystem#getFileSystem} and savedDirectories to the
   * DirectoryStack of fileSystem.
   */
  public PushDirectory() {
    this.fileSystem = JFileSystem.getFileSystem();
    this.savedDirectories = fileSystem.getSavedDirectoryStack();
    setOutput("");
    setWriter(new ConsoleWriter());
  }

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the PushDirectory command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t"
        + "pushd - pushes current working directory onto the\n\t"
        + "DirectoryStack, changes current directory to new directory" + "\n\n"
        + "SYNOPSIS" + "\n\t" + "pushd DIR" + "\n\n" + "DESCRIPTION" + "\n\t"
        + "The pushd or 'Push Directory' saves the current working\n\t"
        + "directory and puts it on the Directory Stack, where it\n\t"
        + "can be called later using the popd command. Then, the\n\t"
        + "current working directory is switched to the given DIR\n\t"
        + "(path to a directory).\n\n" + "EXAMPLES" + "\n\t"
        + "If the working directory is /abc/ which has a directory\n\t"
        + "bcd and if the input is: pushd /abc/bcd,\n\t"
        + "then the directory /abc/ is saved on the top of the\n\t"
        + "Directory Stack and the working directory is changed to\n\t"
        + "/abc/bcd";
  }

  /**
   * Executes the PushDirectory command.
   * 
   */
  @Override
  public void executeCommand() {
    try {
      this.pushDirectory();
    } catch (FileNotFoundException fnfe) {
      // Prints an error message if Directory to switch to doesn't exist
      setErrorMessage(fnfe.getMessage());
      System.out.println(getErrorMessage());
    } catch (InvalidArgumentsException iae) {
      // Prints an error message if more than 1 argument for this PushDirectory
      // command
      setErrorMessage(iae.getMessage());
      System.out.println(getErrorMessage());
    }
    // Writes output of this Command
    getWriter().setToWrite(getOutput());
    getWriter().write();
  }


  /**
   * Adds the current Directory into the DirectoryStack and changes the current
   * Directory to the specified Directory path from the command arguments.
   * Throws an InvalidArgumentsError if there are more than 1 argument. Throws a
   * PathError, if the Directory to switch to doesn't exist.
   * 
   * @throws FileNotFoundException If the Directory to switch to doesn't exist.
   * @throws InvalidArgumentsException If the number of arguments is not equal
   *         to one.
   */
  private void pushDirectory()
      throws FileNotFoundException, InvalidArgumentsException {

    /*
     * There should be one argument only, the path to the Directory to set to
     * after pushing the current directory
     */
    if (getArgs().length != 1) {
      throw InvalidArgumentsException
          .createInvalidArgumentsException(CommandTable.PUSH_DIRECTORY, getArgs());
    } else {
      // Get current Directory
      Directory currentDirectory = fileSystem.getCurrentDirectory();
      Directory directoryToSwitchTo;
      String currWorkingDirectory = currentDirectory.toString();

      // push current working directory onto stack.
      this.savedDirectories.push(currWorkingDirectory);

      // Get Directory from path
      directoryToSwitchTo =
          Directory.getDirectoryFromPath(getArgs()[0], fileSystem);
      // Set new directory to given directory.
      fileSystem.setCurrentDirectory(directoryToSwitchTo);
    }
  }

  /**
   * @return the fileSystem
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * @param fileSystem the fileSystem to set
   */
  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
    this.savedDirectories = fileSystem.getSavedDirectoryStack();
  }
}
