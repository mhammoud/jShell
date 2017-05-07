package directoryFileFoldersPackage;

import exceptions.FileNotFoundException;
import exceptions.InvalidNameException;

/**
 * This class represents a File.
 * <p>
 * A {@link File} contains the contents of the File.
 * <p>
 * Files are differentiated from Directories as Files have their contents stored
 * in a String while the contents of a Directory is the list of Files and
 * Directories in the Directory.
 * 
 * @author Brendan Zhang
 */
public class File extends AbstractFile implements Comparable<File>, Cloneable {

  /**
   * The contents of the File
   */
  private String contents;

  /**
   * Class constructor that creates a new File.
   * 
   * @param name The name of the File
   * @throws InvalidNameException If the name of the File is invalid.
   */
  public File(String name) throws InvalidNameException {
    super.setName(name);
    super.setParentDirectory(Directory.createEmptyRootDirectory());
    this.contents = "";
  }

  /**
   * Class constructor that creates a new File in a Directory.
   * 
   * @param name The name of the File
   * @param parentDirectory The Directory to create the File in
   * @throws InvalidNameException
   */
  public File(String name, Directory parentDirectory)
      throws InvalidNameException {
    if (InvalidNameException.isNameInvalid(name)) {
      throw new InvalidNameException(name);
    }
    super.setName(name);
    super.setParentDirectory(parentDirectory);
    this.contents = "";
    parentDirectory.getFiles().add(this);
  }

  /**
   * Sets the contents of this File
   * 
   * @param contents The contents to set to
   */
  public void setContents(String contents) {
    this.contents = contents;
  }

  /**
   * Gets the contents of this File
   * 
   * @return The contents of this File
   */
  public String getContents() {
    return this.contents;
  }

  /**
   * Appends s to the contents of this File.
   * 
   * @param s String to append to this File
   */
  public void append(String s) {
    if (this.getContents().equals("")) {
      this.setContents(s);
    } else {
      this.setContents(this.getContents() + "\n" + s);
    }
  }

  /**
   * Writes s to the contents of this File. If this File has anything in its
   * contents, it is overwritten.
   * 
   * @param s String to write to this File
   */
  public void write(String s) {
    this.setContents(s);
  }

  /**
   * @return String that corresponds to the absolute path of this File
   */
  public String toString() {
    if (this.getParentDirectory().isRoot()) {
      return this.getParentDirectory() + this.getName();
    } else {
      return this.getParentDirectory() + "/" + this.getName();
    }
  }

  /**
   * Returns the directory that corresponds to the specified path to the File
   * for the specified FileSystem. If the File does not exist, throws a
   * PathException.
   * 
   * @param path The path to the file
   * @param fileSystem The FileSystem to get the File from
   * @return File that corresponds to the path
   * @throws FileNotFoundException
   */
  public static File getFileFromPath(String path, FileSystem fileSystem)
      throws FileNotFoundException {

    String fileName =
        Directory.seperateInnerMostAbstractFileFromPath(path)[1];
    String dirOfFile =
        Directory.seperateInnerMostAbstractFileFromPath(path)[0];

    if (Directory.doesDirectoryExist(dirOfFile, fileSystem)) {
      if (Directory.getDirectoryFromPath(dirOfFile, fileSystem)
          .doesFileExist(fileName)) {
        return Directory.getDirectoryFromPath(dirOfFile, fileSystem)
            .getFile(fileName);
      }
    }

    throw new FileNotFoundException(path);
  }

  /**
   * Returns true if the File exist. Otherwise, returns false.
   * 
   * @param path The path to the file
   * @param fileSystem The FileSystem to get the File from
   * @return true if the File exist. Otherwise, returns false.
   */
  public static boolean doesFileExist(String path, FileSystem fileSystem) {
    try {
      getFileFromPathHelper(path, fileSystem);
      return true;
    } catch (FileNotFoundException fnfe) {
      // Return false if File does not exist
      return false;
    }
  }

  /**
   * Returns the directory that corresponds to the specified path to the File
   * for the specified FileSystem. If the File does not exist, throws a
   * PathException.
   * 
   * @param path The path to the file
   * @param fileSystem The FileSystem to get the File from
   * @return File that corresponds to the path
   * @throws FileNotFoundException If the File does not exist
   */
  private static File getFileFromPathHelper(String path, FileSystem fileSystem)
      throws FileNotFoundException {

    String fileName =
        Directory.seperateInnerMostAbstractFileFromPath(path)[1];
    String dirOfFile =
        Directory.seperateInnerMostAbstractFileFromPath(path)[0];

    if (Directory.doesDirectoryExist(dirOfFile, fileSystem)) {
      if (Directory.getDirectoryFromPath(dirOfFile, fileSystem)
          .doesFileExist(fileName)) {
        return Directory.getDirectoryFromPath(dirOfFile, fileSystem)
            .getFile(fileName);
      }
    }

    throw new FileNotFoundException(path);
  }

  @Override
  /**
   * Compares this file to another file, returning the precendence based on
   * their alphabetical order.
   */
  public int compareTo(File file) {
    return this.getName().compareTo(file.getName());
  }

  @Override
  /**
   * Returns a copy of this file
   */
  public File clone() {
    File copy;
    try {
      copy = (File) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }

    return copy;
  }
}
