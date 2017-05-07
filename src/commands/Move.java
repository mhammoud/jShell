package commands;

import directoryFileFoldersPackage.AbstractFile;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.FileNotFoundException;
import exceptions.InvalidArgumentsException;
import exceptions.RemoveRootDirectoryException;
import utils.CommandTable;
import writer.ConsoleWriter;


/**
 * A command that moves the given file or directory and moves it from one
 * directory to another
 * 
 * @author Julia Yan
 * @author Brendan Zhang
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
    setWriter(new ConsoleWriter());
  }

  /**
   * Gets the man message for this command
   *
   * @return The manual string for the Move command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "mv- move" + "\n\n" + "SYNOPSIS" + "\n\t"
        + "mv OLDPATH NEWPATH" + "\n\n" + "DESCRIPTION" + "\n\t"
        + "Moves the file or directory at OLDPATH and puts it in "
        + "the given NEWPATH. The" + "\n\t"
        + "NEWPATH can be the directory that the file or directory will "
        + "be moved to or the" + "\n\t" + "new path name if OLDPATH is a file. "
        + "If NEWPATH has a file with the same name" + "\n\t"
        + "already there then it replaces/overrites that file." + "\n\n"
        + "EXAMPLE" + "\n\t"
        + "The current working directory contains dirA and file1. Move file1 "
        + "to be in dirA." + "\n\t" + "Both ways are accepted:" + "\n\t\t"
        + "mv ./file1 ./dirA" + "\n\t\t" + "mv ./file1 ./dirA/file1\n";
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
      this.setErrorMessage(iae.getMessage());
      System.out.println(getErrorMessage());
    } catch (FileNotFoundException fnfe) {
      this.setErrorMessage(fnfe.getMessage());
      System.out.println(getErrorMessage());
    } catch (RemoveRootDirectoryException rrde) {
      this.setErrorMessage(rrde.getMessage());
      System.out.println(getErrorMessage());
    }

    getWriter().setToWrite(getOutput());
    getWriter().write();
  }

  /**
   * Moves the file from one directory to the new one. This deletes the file in
   * the old directory.
   * 
   * @throws InvalidArgumentsException If the number of arguments is not equal
   *         to 2.
   * @throws FileNotFoundException If the AbstractFile to delete doesn't exist.
   * @throws RemoveRootDirectoryException If the AbstractFile to remove is a
   *         root Directory.
   */
  private void move() throws InvalidArgumentsException, FileNotFoundException,
      RemoveRootDirectoryException {

    // Check there are appropriate arguments
    if (getArgs().length != 2) {
      throw InvalidArgumentsException
          .createInvalidArgumentsException(CommandTable.MOVE, getArgs());
    }

    // Perform Copy with the arguments from this Move command
    Copy copy = new Copy(getArgs(), fileSystem, getWriter());
    copy.executeCommand();
    this.setErrorMessage(copy.getErrorMessage());

    // Remove file if the Copy succeeded.
    if (getErrorMessage().equals("")) {
      // Get the AbstractFile of the old file.
      AbstractFile oldFile =
          AbstractFile.getAbstractFileFromPath(getArgs()[0], fileSystem);
      // Remove the AbstractFile
      AbstractFile.removeAbstractFile(oldFile);
    }

  }
}
