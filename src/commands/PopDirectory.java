package commands;

import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.DirectoryStackException;
import exceptions.FileNotFoundException;
import utils.DirectoryStack;
import writer.ConsoleWriter;

/**
 * A command popd retrieves a Directory path from the top of the stack and sets
 * it as the current working directory for the FileSystem of this PopDirectory
 * command.
 * 
 * @author Mohamed Hammoud
 * @author Brendan Zhang
 */
public class PopDirectory extends Command {

  /**
   * The DirectoryStack that stores the saved Directories
   */
  private DirectoryStack savedDirectories;
  /**
   * The FileSystem that the PopDirectory command will be using. PopDirectory
   * will change the current Directory and get the Directory from the top of the
   * DirectoryStack of this FileSystem.
   */
  private FileSystem fileSystem;

  /**
   * Default class constructor. Sets fileSystem to
   * {@link JFileSystem#getFileSystem} and savedDirectories to the
   * DirectoryStack of fileSystem.
   */
  public PopDirectory() {
    fileSystem = JFileSystem.getFileSystem();
    this.savedDirectories = fileSystem.getSavedDirectoryStack();
    setWriter(new ConsoleWriter());
  }

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the PopDirectory command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t"
        + "popd - removes top directory from the DirectoryStack,\n"
        + "changes current directory to that directory" + "\n\n" + "SYNOPSIS"
        + "\n\t" + "pushd" + "\n\n" + "DESCRIPTION" + "\n\t"
        + "The popd or 'Pop Directory' takes the last directory\n\t"
        + "that was entered into the Directory Stack\n\t"
        + "(i.e. the last directory used with the pushd command).\n\t"
        + "The current working directory is then switched to the\n\t"
        + "directory that was on the top of the Directory stack." + "\n\n"
        + "EXAMPLES" + "\n\t"
        + "If the working directory is /abc/ which has a directory\n\t"
        + "bcd and if the input is: pushd /abc/bcd,\n\t"
        + "then the directory /abc/ is saved on the top of the\n\t"
        + "Directory Stack and the working directory is changed to\n\t"
        + "/abc/bcd";
  }

  /**
   * Executes the "popd" command
   */
  @Override
  public void executeCommand() {
    try {
      this.popDirectory();
    } catch (FileNotFoundException fnfe) {
      // Print an error Directory popped can't be found.
      setErrorMessage(fnfe.getMessage());
      System.out.println(getErrorMessage());
    } catch (DirectoryStackException dse) {
      // Print an error if stack is empty.
      setErrorMessage(dse.getMessage());
      System.out.println(getErrorMessage());
    }

    // Writes output of this Command
    getWriter().setToWrite(getOutput());
    getWriter().write();
  }

  /**
   * Retrieves (and removes) the top Directory from the saved Directories of
   * this PopDirectory command and changes the current Directory to that
   * Directory. Throws a {@link FileNotFoundException} if the Directory to
   * change to doesn't exist. Throws a {@link DirectoryStackException} if the
   * DirectoryStack is empty.
   * 
   * @throws FileNotFoundException If the Directory to change to doesn't exist.
   * @throws DirectoryStackException If the DirectoryStack is empty.
   */
  private void popDirectory()
      throws FileNotFoundException, DirectoryStackException {

    // Throw exception if DirectoryStack is empty
    if (this.savedDirectories.getLength() == 0) {
      throw new DirectoryStackException();
    } else {
      // Pop from the path from the stack.
      // Get the Directory from the absolute path
      Directory directoryToSwitchTo = Directory
          .getDirectoryFromPath(this.savedDirectories.pop(), fileSystem);
      // Set the new current directory
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
