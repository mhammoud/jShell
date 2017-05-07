package commands;

import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.FileAlreadyExistsException;
import exceptions.FileNotFoundException;
import exceptions.InvalidNameException;
import utils.CommandTable;
import writer.ConsoleWriter;

/**
 * <p>
 * This class contains the variables and methods to perform the "mkdir" command.
 * 
 * The MakeDirectory Command creates a Directory in the FileSystem. It can take
 * either an absolute path or partial path as an argument.
 * 
 * If the Directory to make the new Directory in doesn't exist, prints a
 * PathError and CantCreateDirectoryError. If the Directory to make already
 * exists, prints a DirectoryAlreadyExistError. If the Directory name is not
 * valid, prints an InvalidNameError.
 * 
 * @author Brendan Zhang
 * 
 */
public class MakeDirectory extends Command {
  /**
   * The FileSystem that the Directory will be made in
   */
  private FileSystem fileSystem;

  /**
   * Default class constructor. Sets args to null, output to empty String,
   * fileSystem to {@link JFileSystem#getFileSystem()) and writer to a new
   * ConsoleWriter.
   */
  public MakeDirectory() {
    this.setArgs(null);
    this.setFileSystem(JFileSystem.getFileSystem());
    setWriter(new ConsoleWriter());
  }

  /**
   * 
   * Default class constructor. Setsoutput to empty String and writer to a new
   * ConsoleWriter.
   *
   * @param args arguments for this MakeDirectory command
   * @param fileSystem The FileSystem to make the Directory in
   */
  public MakeDirectory(String[] args, FileSystem fileSystem) {
    this.setArgs(args);
    this.setFileSystem(fileSystem);
    setWriter(new ConsoleWriter());
  }

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the MakeDirectory command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "mkdir - make directory" + "\n\n" + "SYNOPSIS"
        + "\n\t" + "mkdir DIR ..." + "\n\n" + "DESCRIPTION" + "\n\t"
        + "Makes a new directory. Can be made in the "
        + "current working directory or by\n\tspecifying a full path." + "\n\n"
        + "EXAMPLE" + "\n\t" + "Creates directory abc in the current directory"
        + "\n\t\t" + "mkdir abc" + "\n\t" + "Creates directory abc in "
        + "directory /base/someDir" + "\n\t\t" + "mkdir /base/someDir/abc\n";
  }

  /**
   * Executes the "mkdir" Commmand. If the Directory to make the new Directory
   * in doesn't exist, prints a PathError and CantCreateDirectoryError. If the
   * Directory to make already exists, prints a DirectoryAlreadyExistError. If
   * the Directory name is not valid, prints an InvalidNameError.
   */
  @Override
  public void executeCommand() {
    // For each directory
    for (String directory : getArgs()) {
      try {
        makeDirectory(directory);
      } catch (FileNotFoundException fnfe) {
        setErrorMessage("mkdir: cannot create directory. " + fnfe.getMessage());
        System.out.println(getErrorMessage());
      } catch (FileAlreadyExistsException faee) {
        setErrorMessage(faee.getMessage());
        System.out.println(getErrorMessage());
      } catch (InvalidNameException ine) {
        setErrorMessage(ine.getMessage());
        System.out.println(getErrorMessage());
      }
      // Writes output of command
      getWriter().setToWrite(getOutput());
      getWriter().write();
    }
  }

  /**
   * Executes the "mkdir" Commmand. If the Directory to make the new Directory
   * in doesn't exist, prints a PathError and CantCreateDirectoryError. If the
   * Directory to make already exists, prints a DirectoryAlreadyExistError. If
   * the Directory name is not valid, prints an InvalidNameError.
   * 
   * @param directory
   * @throws FileNotFoundException If the Directory to make the new Directory in
   *         doesn't exist.
   * @throws FileAlreadyExistsException If the Directory to make already exists.
   * @throws InvalidNameException If the Directory name is not valid
   */
  private void makeDirectory(String directory) throws FileNotFoundException,
      FileAlreadyExistsException, InvalidNameException {

    // Separate the directory that the new directory will be made in from the
    // new directory's name
    String[] seperatedPath = Directory.seperateInnerMostAbstractFileFromPath(
        removeTrailingSlash(directory));
    String pathOfDirectoryToMakeIn = seperatedPath[0];
    String newDirectoryName = seperatedPath[1];

    Directory directoryToMakeIn =
        Directory.getDirectoryFromPath(pathOfDirectoryToMakeIn, fileSystem);

    // If the directory to make the new Directory in exists and Directory
    // doesn't already exist and the Directory name is valid, create the new
    // Directory.
    if (directoryToMakeIn.doesChildDirectoryExist(newDirectoryName)) {
      // Directory to make already exists.
      // Throw DirectoryAlreadyExistsException.
      throw new FileAlreadyExistsException(directory);
    } else if (InvalidNameException.isNameInvalid(newDirectoryName)) {
      // Directory to make has an invalid name.
      // Throw InvalidNameException.
      throw InvalidNameException
          .createInvalidNameException(CommandTable.MAKE_DIRECTORY, getArgs());
    } else {
      Directory.createSubDirectory(newDirectoryName, directoryToMakeIn);
    }
  }

  /**
   * Removes trailing slash from the path.
   * 
   * @param path String that contains a path to a File or Directory
   * @return String of the path with trailing slash removed
   */
  private String removeTrailingSlash(String path) {
    if (path.substring(path.length() - 1, path.length()).equals("/")) {
      return path.substring(0, path.length() - 1);
    } else {
      return path;
    }
  }

  /**
   * Gets the FileSystem that will be used.
   * 
   * @return The FileSystem that will be used
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * Sets the FileSystem that will be used.
   * 
   * @param fileSystem The FileSystem to set
   */
  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }
}
