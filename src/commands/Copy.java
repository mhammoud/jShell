package commands;

import directoryFileFoldersPackage.AbstractFile;
import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.File;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.FileNotFoundException;
import exceptions.InvalidArgumentsException;
import exceptions.InvalidNameException;
import exceptions.MoveIntoSubdirectoryException;
import exceptions.RemoveRootDirectoryException;
import exceptions.SameFileException;
import utils.CommandTable;
import writer.ConsoleWriter;
import writer.Writer;

/**
 * A command that copies the given file or directory and puts the copy in
 * another given directory
 * 
 * @author Adam Ah-Chong
 * @author Brendan Zhang
 *
 */
public class Copy extends Command {
  /**
   * File System being used
   */
  private FileSystem fileSystem;

  /**
   * Default Constructor. Sets the file system as the current JFileSystem
   * {@link JFileSystem#getFileSystem}, args to null, and writer to a new
   * ConsoleWriter.
   */
  public Copy() {
    this.setArgs(null);
    this.setOutput("");
    this.setFileSystem(JFileSystem.getFileSystem());
    setWriter(new ConsoleWriter());
  }

  /**
   * Class constructor. Sets the file system as the current JFileSystem
   * {@link JFileSystem#getFileSystem}
   */
  public Copy(String[] args, FileSystem fileSystem, Writer writer) {
    this.setOutput("");
    this.setArgs(args);
    this.setFileSystem(fileSystem);
    setWriter(writer);
  }

  /**
   * Gets the man message for this command
   *
   * @return The manual string for the Copy command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "cp - copy" + "\n\n" + "SYNOPSIS" + "\n\t"
        + "cp OLDPATH NEWPATH" + "\n\n" + "DESCRIPTION" + "\n\t"
        + "Copies the file or directory at OLDPATH and puts the copy in "
        + "the given NEWPATH."
        + "\n\tThe NEWPATH can be the directory that the file or directory "
        + "will be moved to or the" + "\n\t" + "new path name if OLDPATH is a "
        + "file. If NEWPATH has a file with the same name" + "\n\t"
        + "already there then it replaces/overrites that file." + "\n\n"
        + "EXAMPLE" + "\n\t"
        + "The current working directory contains dirA and file1. Copy file1 "
        + "to be in dirA." + "\n\t" + "Both ways are accepted:" + "\n\t\t"
        + "cp ./file1 ./dirA" + "\n\t\t" + "cp ./file1 ./dirA/file1\n";
  }

  public void setFileSystem(FileSystem f) {
    this.fileSystem = f;
  }

  /**
   * Executes the copy command
   * 
   * @throws SameFileException
   */
  @Override
  public void executeCommand() {
    try {
      this.copy();
    } catch (InvalidArgumentsException iae) {
      setErrorMessage(iae.getMessage());
      System.out.println(getErrorMessage());
    } catch (FileNotFoundException fnfe) {
      setErrorMessage(fnfe.getMessage());
      System.out.println(getErrorMessage());
    } catch (RemoveRootDirectoryException rrde) {
      setErrorMessage(rrde.getMessage());
      System.out.println(getErrorMessage());
    } catch (MoveIntoSubdirectoryException misde) {
      setErrorMessage(misde.getMessage());
      System.out.println(getErrorMessage());
    } catch (InvalidNameException ine) {
      setErrorMessage(ine.getMessage());
      System.out.println(getErrorMessage());
    } catch (SameFileException sfe) {
      setErrorMessage(sfe.getMessage());
      System.out.println(getErrorMessage());
    }
  }

  /**
   * Copies the file from the original directory to the new one. Does not delete
   * file from the old directory.
   * 
   * @throws InvalidArgumentsException
   * @throws FileNotFoundException
   * @throws RemoveRootDirectoryException
   * @throws MoveIntoSubdirectoryException
   * @throws InvalidNameException
   * @throws SameFileException
   */
  private void copy() throws InvalidArgumentsException, FileNotFoundException,
      RemoveRootDirectoryException, MoveIntoSubdirectoryException,
      InvalidNameException, SameFileException {
    // Check for appropriate number of arguments
    if (getArgs().length != 2) {
      throw InvalidArgumentsException
          .createInvalidArgumentsException(CommandTable.COPY, getArgs());
    }

    // Get the AbstractFile to copy from the command argument.
    AbstractFile fileToCopy =
        AbstractFile.getAbstractFileFromPath(getArgs()[0], fileSystem);

    // Create a new AbstractFile by cloning fileToCopy.
    AbstractFile newFile;
    if (AbstractFile.isDir(fileToCopy)) {
      newFile = ((Directory) fileToCopy).clone();
    } else {
      newFile = ((File) fileToCopy).clone();
    }

    // The Directory to put the copied File in
    Directory newDir;
    String newFilePath = getArgs()[1];
    String fileToCopyPath = getArgs()[0];
    // The name for the new AbstractFile
    String newName = null;

    try {
      // If the new file path is a valid path to a Directory, then simply add
      // the new File to this directory and keep everything else the same.
      newDir = Directory.getDirectoryFromPath(newFilePath, fileSystem);
    } catch (FileNotFoundException fnfe) {
      // Since the new file path is not a valid path to a Directory, File must
      // be renamed and placed in parent directory of the newFilePath.

      // Last part of newFilePath will be the new name for the AbstractFile
      newName = Directory.seperateInnerMostAbstractFileFromPath(newFilePath)[1];
      // Set newFilePath to parent Directory of newFilePath
      newFilePath =
          Directory.seperateInnerMostAbstractFileFromPath(newFilePath)[0];
      newDir = Directory.getDirectoryFromPath(newFilePath, fileSystem);
    }

    // Set the name for the newly created AbstractFile, if needed to change.
    if (newName != null) {
      newFile.setName(newName);
    }

    // Check we are not moving a directory into a child directory
    if (AbstractFile.isDir(fileToCopy)
        && Directory.isSubDirectory(((Directory) fileToCopy), newDir)) {
      throw new MoveIntoSubdirectoryException(fileToCopyPath, newFilePath);
    }

    // If moving to same location, throw an Exception.
    if (newDir.toString().equals(fileToCopy.toString())
        || (newFile.getName().equals(fileToCopy.getName()) && newDir.toString()
            .equals(fileToCopy.getParentDirectory().toString()))) {
      throw new SameFileException(fileToCopyPath, newFile.toString());
    } else {
      newFile.setParentDirectory(((Directory) newDir));
      AbstractFile.addToDirectory(newFile, ((Directory) newDir));
    }
  }
}
