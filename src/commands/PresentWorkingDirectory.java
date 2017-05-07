package commands;

import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import writer.ConsoleWriter;

/**
 * This class contains the methods and variables required to prints the present
 * working directory for a FileSystem.
 * 
 * @author Brendan Zhang
 * @author Mohamed Hammoud
 */
public class PresentWorkingDirectory extends Command {

  /**
   * The FileSystem that this PresentWorkingDirectory command will be using to
   * get the current Directory and print the path of it
   */
  private FileSystem fileSystem;

  /**
   * Default constructor. Sets fileSystem to {@link JFileSystem#getFileSystem}
   * for this PresentWorkingDirectory command.
   */
  public PresentWorkingDirectory() {
    setFileSystem(JFileSystem.getFileSystem());
    setWriter(new ConsoleWriter());
  }

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the PresentWorkingDirectory command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "pwd - present working directory" + "\n\n"
        + "SYNOPSIS" + "\n\t" + "pwd" + "\n\n" + "DESCRIPTION" + "\n\t"
        + "Displays the path of the current "
        + "directory. Prints the full path name." + "\n\n" + "EXAMPLE" + "\n\t"
        + "Currently working in directory: /base/curDir" + "\n\t"
        + "The second line is the printed path." + "\n\t\t" + "pwd" + "\n\t\t"
        + "/base/curDir";
  }

  /**
   * Executes the "pwd" command.
   * 
   * Prints the absolute path of the current Directory.
   */
  @Override
  public void executeCommand() {
    // Set output to String that contains the path of the current directory
    setOutput(fileSystem.getCurrentDirectory().toString());
    // Writes output of this command
    getWriter().setToWrite(getOutput());
    getWriter().write();
  }

  /**
   * Gets the fileSystem of this PresentWorkingDirectory Command.
   * 
   * @return The fileSystem of this PresentWorkingDirectory command
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * Sets the fileSystem of this PresentWorkingDirectory Command.
   * 
   * @param The fileSystem of this PresentWorkingDirectory command
   */
  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }
}
