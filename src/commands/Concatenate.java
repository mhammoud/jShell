package commands;

import directoryFileFoldersPackage.File;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.FileNotFoundException;
import writer.ConsoleWriter;

/**
 * This class consists of methods and variables required to execute the "cd"
 * command to change the current directory.
 * 
 * The ChangeDirectory Command changes the current directory of the FileSystem
 * to a given Directory. If the directory doesn't exist, print a PathError.
 * 
 * @author Brendan Zhang
 * 
 */
public class Concatenate extends Command {

  /**
   * The FileSystem that the Concatenate command will use to get the Files from
   */
  private FileSystem fileSystem;

  /**
   * Default class constructor. Sets the FileSystem to
   * {@link JFileSystem#getFileSystem()}, sets the output to an empty String,
   * and sets args to null.
   */
  public Concatenate() {
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
  public Concatenate(String[] args, FileSystem fileSystem) {
    setArgs(args);
    setFileSystem(fileSystem);
    setWriter(new ConsoleWriter());
  }

  /**
   * Gets the man message for this command.
   * 
   * @return The manual string for the Concatenate command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "cat - concatenate" + "\n\n" + "SYNOPSIS" + "\n\t"
        + "cat FILE1 [FILE2 ...]" + "\n\n" + "DESCRIPTION" + "\n\t"
        + "For each file, displays the contents "
        + "of the file(s). Each file is seperated by 3 line breaks." + "\n\n"
        + "EXAMPLE" + "\n\t" + "f1 is a text file containing 'Hello', "
        + "f2 is a text file containing 'What is your name?'.\n\tBelow "
        + "is the output for each example command use:" + "\n\t" + "Example 1:"
        + "\n\t\t" + "cat f1" + "\n\t\t" + "Hello" + "\n\n\t" + "Example 2:"
        + "\n\t\t" + "cat f1 f2" + "\n\t\t"
        + "Hello\n\n\n\t\tWhat is your name?\n";
  }

  /**
   * Executes the "cat" command.
   */
  @Override
  public void executeCommand() {
    // For each file path, print the contents of the file
    for (int i = 0; i < getArgs().length; i++) {
      File file = null;
      try {
        file = File.getFileFromPath(getArgs()[i], fileSystem);
      } catch (FileNotFoundException fnfe) {
        setErrorMessage(fnfe.getMessage());
        System.out.println(getErrorMessage());
      }
      String addToOutput = "";
      if (file != null) {
        // Don't add new-line
        if (i == getArgs().length - 1) {
          addToOutput = file.getContents();
        } else {
          addToOutput = file.getContents() + "\n\n\n";
        }
        getWriter().setToWrite(addToOutput);
        getWriter().write();
        setOutput(getOutput() + addToOutput);
      }
    }
  }

  /**
   * Gets the fileSystem for this Concatenate command.
   * 
   * @return the fileSystem
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * Sets the fileSystem for this Concatenate command.
   * 
   * @param fileSystem the fileSystem to set
   */
  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }
}
