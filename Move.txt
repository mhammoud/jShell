package commands;

import directoryFileFoldersPackage.*;
import error.InvalidArgumentsException;
import error.FileNotFoundException;
import utils.CommandTable;


/**
 * A command that moves the given file or directory and moves it from one
 * directory to another
 * 
 * @author Julia Yan
 *
 */
public class Move extends Command {
  /**
   * File System being used
   */
  private FileSystem fileSystem;

  /**
   * Default Constructor. Sets the file system as the current JFileSystem
   * {@link JFileSystem#getFileSystem}
   */
  public Move() {
    this.setArgs(null);
    this.setOutput("");
    this.setFileSystem(JFileSystem.getFileSystem());
  }

  /**
   * Gets the man message for this command
   *
   * @return The manual string for the Move command
   */
  public static String getManMessage() {
    return "";
  }

  /**
   * Sets the file system that the command will use to move files
   * 
   * @param The file system to be used
   */
  public void setFileSystem(FileSystem f) {
    this.fileSystem = f;
  }

  /**
   * Executes the move command
   */
  @Override
  public void executeCommand() {
    try {
      this.move();
    } catch (InvalidArgumentsException iae) {
      System.out.println(iae.getMessage());
    } catch (FileNotFoundException fnfe) {
      System.out.println(fnfe.getMessage());
    }
  }

  /**
   * Moves the file from one directory to the new one. This deletes the file in
   * the old directory.
   * 
   * @throws InvalidArgumentsException
   * @throws FileNotFoundException
   */
  private void move() throws InvalidArgumentsException, FileNotFoundException {
    // Check there are appropriate arguments
    if (getArgs().length != 2) {
      throw InvalidArgumentsException.createInvalidArgumentsError(CommandTable.MOVE,
          getArgs());
    }

    // Get the abstract file from both paths
    AbstractFile oldFile =
        AbstractFile.getAbstractFileFromPath(getArgs()[0], fileSystem);
    AbstractFile newFile =
        AbstractFile.getAbstractFileFromPath(getArgs()[1], fileSystem);

    // Check if the new file is a directory
    if (!(isDir(newFile))) {
      throw InvalidArgumentsException.createInvalidArgumentsError(CommandTable.MOVE,
          getArgs());
    }

    // Remove the file from the old path
    removeFromOldDir(oldFile, newFile);

    // Set parent directory of the file
    oldFile.setParentDirectory(((Directory) newFile));

    // Add the file to new path
    addToNewDir(oldFile, ((Directory) newFile));
  }

  /**
   * Added the given file to the given directory
   * 
   * @param oldFile
   * @param newFile
   */
  private void addToNewDir(AbstractFile oldFile, Directory newFile) {
    // Add the file to new path
    if (isFile(oldFile)) {
      newFile.addFile((File) oldFile);
    } else if (isDir(oldFile)) {
      newFile.addSubDirectory((Directory) oldFile);
    }
  }

  /**
   * Removes the current file being moved from the old location. If the file
   * being removed is the parent directory of the current location, throws a
   * InvalidArgumentException
   * 
   * @param oldFile
   * @param newFile
   * @throws InvalidArgumentsException
   * @throws FileNotFoundException
   */
  private void removeFromOldDir(AbstractFile oldFile, AbstractFile newFile)
      throws InvalidArgumentsException, FileNotFoundException {
    // Removes depending on if old file is a File or Directory
    if (isFile(oldFile)) {
      oldFile.getParentDirectory().removeFile(oldFile.getName());
    } else if (isDir(oldFile) && !(isSubDirectory(oldFile, newFile))) {
      oldFile.getParentDirectory().removeSubDirectory(oldFile.getName());
    } else {
      throw InvalidArgumentsException.createInvalidArgumentsError(CommandTable.MOVE,
          getArgs());
    }
  }

  /**
   * Determines if the old file location is actually the parent directory of the
   * new file location
   * 
   * @param oldFile
   * @param newFile
   * @return
   */
  private boolean isSubDirectory(AbstractFile oldFile, AbstractFile newFile) {
    if ((oldFile instanceof Directory)
        && (((Directory) oldFile).doesChildDirectoryExist(newFile.getName()))) {
      return true;
    }
    return false;
  }

  /**
   * Check to see if the given file is of type File
   * 
   * @param file to check
   * @return true iff the file is of type File
   */
  private boolean isFile(AbstractFile file) {
    if (file instanceof File) {
      return true;
    }
    return false;
  }

  /**
   * Check to see if the given file is of type Directory
   * 
   * @param file to check
   * @return true iff the file is of type Directory
   */
  private boolean isDir(AbstractFile file) {
    if (file instanceof Directory) {
      return true;
    }
    return false;
  }
}
