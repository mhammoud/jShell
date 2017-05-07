package directoryFileFoldersPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import exceptions.FileNotFoundException;
import exceptions.InvalidNameException;

/**
 * Class that represents a directory.
 * <p>
 * A {@link Directory} contains its File objects in an {@link ArrayList}. A
 * Directory also contains a reference to its parent Directory and a reference
 * to its children Directories as an {@link ArrayList}.
 * <p>
 * Directories are differentiated from Files as Files have their contents stored
 * in a String while the contents of a Directory is the list of Files and
 * Directories in the Directory.
 * 
 * @author Brendan Zhang
 * @author Mohamed Hammoud
 * @author Julia Yan
 */
public class Directory extends AbstractFile
    implements Comparable<Directory>, Cloneable {

  /**
   * The List of Files contained in this Directory
   */
  private ArrayList<File> files;
  /**
   * The List of Directories contained in this Directory
   */
  private ArrayList<Directory> childDirectories;
  /**
   * True if this Directory is a root Directory.
   */
  private final boolean root;

  /**
   * Class Constructor which creates an empty root Directory.
   * 
   * @param directoryName name of the Directory
   * 
   */
  private Directory(String directoryName) {
    super.setNameRootDirectory(directoryName);
    // Create a Directory given a directory name, with empty
    // parent folder (since it is the root)
    super.setParentDirectory(this);
    childDirectories = new ArrayList<Directory>();
    this.files = new ArrayList<File>();
    this.root = true;
  }

  /**
   * Constructor which creates a Directory given a parent Directory. That is,
   * the new sub-folder is a child of parentDirectory
   * 
   * @param directoryName name of the Directory
   * @param parentDirectory the parent Directory
   * @throws InvalidNameException If the Directory name is invalid
   * 
   */
  private Directory(String directoryName, Directory parentDirectory)
      throws InvalidNameException {
    // Create new Directory with given parentDirectory as its
    // parent directory.
    super.setName(directoryName);
    super.setParentDirectory(parentDirectory);
    this.childDirectories = new ArrayList<Directory>();
    this.files = new ArrayList<File>();
    this.root = false;

    // Add this directory to parent folder's list of child folders.
    parentDirectory.addSubDirectory(this);
  }

  /**
   * Creates an empty root Directory with specified directoryName
   * 
   * @param directoryName name of the new root folder
   * @return {@link Directory} with specified directory name.
   * @throws InvalidNameException
   */
  public static Directory createEmptyRootDirectory(String directoryName) {
    return new Directory(directoryName);
  }

  /**
   * Creates an empty root Directory with the directory name "/"
   * 
   * @return root Directory with directory name "/"
   * @throws InvalidNameException
   */
  public static Directory createEmptyRootDirectory() {
    return new Directory("/");
  }

  /**
   * Creates a new Directory with the specified name in the parent Directory.
   * 
   * @param directoryName The name of the directory to create
   * @param parentDirectory The parent Directory to create the new Directory in
   * @return The new Directory that was created in parentDirectory
   * @throws InvalidNameException
   */
  public static Directory createSubDirectory(String directoryName,
      Directory parentDirectory) throws InvalidNameException {
    return new Directory(directoryName, parentDirectory);
  }

  /**
   * Changes full path of a Directory into a String array of folder names
   * leading to the desired Directory, with the first element of the array being
   * the folder proceeding the current Directory.
   * 
   * @param path The path to Directory
   * @return String[] of the directory names to get to the Directory
   */
  public static String[] changePathIntoDirectoryNames(String path) {
    // take the directory names between every '\', '/' and '+' into a list
    List<String> listOfDirectoryNames =
        new ArrayList<String>(Arrays.asList(path.split("\\/+")));
    // Remove empty Strings and null elements from list
    listOfDirectoryNames.removeAll(Collections.singleton(null));
    listOfDirectoryNames.removeAll(Collections.singleton(""));
    return listOfDirectoryNames.toArray(new String[0]);
  }

  /**
   * Takes a path to a Directory and separates the innermost directory from the
   * rest of the path. The innermost AbstractFile name is stored in String[1],
   * while the rest are stored in String[0].
   * 
   * @param path String that contains the path to the Directory
   * @return String[] that contains the innermost AbstractFile name as String[1]
   *         and the Directory that the AbstractFile as String[0].
   */
  public static String[] seperateInnerMostAbstractFileFromPath(String path) {
    String fixedPath = path.replaceAll("//", "/");
    // find the last directory in the path (innermost directory in path)
    int lastSlash = fixedPath.lastIndexOf("/");
    if (lastSlash < 0) {
      return new String[] {"", fixedPath};
    } else {
      return new String[] {fixedPath.substring(0, lastSlash),
          fixedPath.substring(lastSlash + 1)};
    }
  }

  /**
   * Returns <tt>true</tt> if the path to the Directory is an absolute path.
   * Otherwise, return <tt>false</tt>.
   * 
   * @param path Path to the Directory
   * @return <tt>true</tt> if the path is an absolute path. Otherwise, return
   *         <tt>false</tt>.
   */
  public static boolean isAbsolutePath(String path) {
    if (path.length() == 0)
      return false;
    // if the path has a '/' as the first character, it must be an absolute path
    return path.substring(0, 1).equals("/");
  }

  /**
   * Returns the directory that corresponds to the specified path to the
   * directory for the specified FileSystem. If the directory does not exist,
   * throws a FileNotFoundException.
   * 
   * @param path String that contains the path to the Directory.
   * @param fileSystem FileSystem to get the Directory from.
   * @return The Directory that corresponds to the specified path.
   * @throws FileNotFoundException If the directory does not exist.
   */
  public static Directory getDirectoryFromPath(String path,
      FileSystem fileSystem) throws FileNotFoundException {

    // check whether to find a directory from an absolute path or a relative one
    // then return the directory that the path specifies
    if (Directory.isAbsolutePath(path)) {
      return fileSystem.getRootDirectory().getDirectoryFromAbsolutePath(path);
    } else {
      return fileSystem.getCurrentDirectory()
          .getDirectoryFromRelativePath(path);
    }
  }

  /**
   * Returns true if a Directory exists that corresponds to the specified path
   * to the directory. Otherwise, return false.
   * 
   * @param path String that contains the path to the Directory
   * @param fileSystem FileSystem to get the Directory from
   * @return <tt>true</tt> if the Directory exists. Otherwise, return
   *         <tt>false</tt>.
   */
  public static boolean doesDirectoryExist(String path, FileSystem fileSystem) {
    try {
      // If attempting to get a directory from the given path returns null,
      // the path must not lead to an existing directory.
      if (Directory.isAbsolutePath(path)) {
        return fileSystem.getRootDirectory()
            .getDirectoryFromAbsolutePath(path) != null;
      } else {
        return fileSystem.getCurrentDirectory()
            .getDirectoryFromRelativePath(path) != null;
      }
    } catch (FileNotFoundException fnfe) {
      // If we get a FileNotFoundException, just return false. Do not output any
      // error message.
      return false;
    }
  }

  /**
   * Returns true if parentDirectory is the parent Directory of childDirectory
   * 
   * @param parentDirectory The Directory to check if it is the parent Directory
   *        of childDirectory.
   * @param childDirectory The Directory to check if it is the child Directory
   *        of parentDirectory.
   * @return <tt>true</tt> if parentDirectory is the parent of childDirectory;
   *         Otherwise, return <tt>false</tt>.
   */
  public static boolean isSubDirectory(Directory parentDirectory,
      Directory childDirectory) {
    // check every immediate subdirectory
    for (Directory directory : parentDirectory.getChildDirectories()) {
      if (directory == childDirectory) {
        return true;
      }
      // recursively search through all subdirectories
      else {
        return isSubDirectory(directory, childDirectory);
      }
    }
    return false;
  }

  /**
   * Returns a String representation of the Directory. This String will be the
   * absolute path of the current Directory.
   */
  public String toString() {
    if (this.root) {
      return getName();
    } else {
      if (this.getParentDirectory().root) {
        return this.getParentDirectory().toString() + getName();
      } else {
        return this.getParentDirectory().toString() + "/" + getName();
      }
    }
  }

  @Override
  /**
   * Compares this directory to a directory a - returns which precedence of
   * alphabetical order.
   * 
   * @param directory The Directory to compare to
   * @return integer that is the result of a comparison between this Directory's
   *         name and the name of the directory to compare to.
   */
  public int compareTo(Directory directory) {
    return this.getName().compareTo(directory.getName());
  }

  @Override
  /**
   * Creates and returns a copy of this Directory.
   * 
   * @return A copy of this Directory
   */
  public Directory clone() {
    // copies the immutable variables
    Directory copy;
    try {
      copy = (Directory) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }

    // makes new copies of mutable variables
    // i.e. creates new files and subdirectories
    copy.setChildDirectories(new ArrayList<Directory>());
    copy.setFiles(new ArrayList<File>());
    for (Directory directory : this.childDirectories) {
      Directory directoryCopy = (Directory) directory.clone();
      directoryCopy.setParentDirectory(copy);
      copy.addSubDirectory(directoryCopy);
    }
    for (File file : this.files) {
      File fileCopy = (File) file.clone();
      fileCopy.setParentDirectory(copy);
      copy.addFile(fileCopy);
    }

    return copy;
  }

  /**
   * Returns <tt>true</tt> if this directory is the root directory. Otherwise,
   * return <tt>false</tt>.
   * 
   * @return <tt>true</tt> if this directory is the root directory. Otherwise,
   *         return <tt>false</tt>.
   */
  public boolean isRoot() {
    return this.root;
  }

  /**
   * Returns an {@link ArrayList} of this directory's children directories.
   * 
   * @return ArrayList<Folder> childFolders
   */
  public ArrayList<Directory> getChildDirectories() {
    return this.childDirectories;
  }

  /**
   * @param childDirectories the childDirectories to set
   */
  public void setChildDirectories(ArrayList<Directory> childDirectories) {
    this.childDirectories = childDirectories;
  }

  /**
   * Returns an ArrayList of the Files in this Directory.
   * 
   * @return ArrayList<File> the Files in this Directory
   */
  public ArrayList<File> getFiles() {
    return this.files;
  }

  /**
   * @param files the files to set
   */
  public void setFiles(ArrayList<File> files) {
    this.files = files;
  }

  /**
   * Returns the root directory from this directory.
   * 
   * @return
   */
  public Directory getRootDirectory() {
    Directory currentDirectory = this;
    while (!this.root) {
      currentDirectory = currentDirectory.getParentDirectory();
    }
    return currentDirectory;
  }


  /**
   * Returns true if there exists a child Directory with the specified directory
   * name.
   * 
   * @param directoryName the name of the child Directory
   * @return <tt>true</tt> if the child Directory exists; Otherwise, return
   *         <tt>false</tt>.
   */
  public boolean doesChildDirectoryExist(String directoryName) {
    for (Directory directory : this.getChildDirectories()) {
      if (directory.getName().equals(directoryName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns true if there exists a File with the specified file name in this
   * Directory.
   * 
   * @param name the name of the File
   * @return <tt>true</tt> if the File exists; Otherwise, return <tt>false</tt>.
   */
  public boolean doesFileExist(String fileName) {
    return this.getFile(fileName) != null;
  }

  /**
   * Returns the child directory with the specified directory name. If the child
   * directory doesn't exist, return null.
   * 
   * @param directoryName the name of the child Directory
   * @return The child Directory if it exists; Otherwise, return <tt>null</tt>.
   */
  public Directory getChildDirectory(String directoryName) {
    if (doesChildDirectoryExist(directoryName)) {
      for (Directory directory : this.getChildDirectories()) {
        if (directory.getName().equals(directoryName)) {
          return directory;
        }
      }
    }
    return null;
  }

  /**
   * Returns File if there exists a File with the specified file name.
   * 
   * @param fileName the name of the File
   * @return The File if it exists; Otherwise, return <tt>null</tt>.
   */
  public File getFile(String fileName) {
    for (File file : this.getFiles()) {
      if (file.getName().equals(fileName)) {
        return file;
      }
    }
    return null;
  }

  /**
   * Adds a given directory as a sub directory to the current directory
   * 
   * @param childFolder
   */
  public void addSubDirectory(Directory childFolder) {
    childDirectories.add(childFolder);
  }

  /**
   * Adds the given file to this directory
   * 
   * @param file object
   */
  public void addFile(File file) {
    this.files.add(file);
  }

  /**
   * Removes the file with the given name from this directory
   * 
   * @param file name
   * @throws FileNotFoundException
   */
  public void removeFile(String file) throws FileNotFoundException {
    int i = 0;

    // Find index of file
    while (i < this.files.size() && this.files.get(i).getName() != file) {
      i++;
    }

    // Remove file if found
    if (i == this.files.size()) {
      throw new FileNotFoundException(file);
    } else {
      this.files.remove(i);
    }
  }

  /**
   * Removes the directory with the given name from this directory
   * 
   * @param dir name
   * @throws FileNotFoundException
   */
  public void removeSubDirectory(String dir) throws FileNotFoundException {
    int i = 0;

    // Find index of directory
    while (i < this.childDirectories.size()
        && this.childDirectories.get(i).getName() != dir) {
      i++;
    }

    // Remove directory if found
    if (i == this.childDirectories.size()) {
      throw new FileNotFoundException(dir);
    } else {
      this.childDirectories.remove(i);
    }
  }

  /**
   * Returns the directory that corresponds to the specified path to the
   * directory. If the directory does not exist, returns null. The path
   * specified must be a relative path from this Directory.
   * 
   * @param path The relative path to the Directory
   * @return The Directory that corresponds to the path; If the directory does
   *         not exist, returns <tt>null</tt>.
   * @throws FileNotFoundException If the Directory does not exist
   */
  private Directory getDirectoryFromRelativePath(String path)
      throws FileNotFoundException {
    boolean directoryFound = true;
    String[] subDirectories = Directory.changePathIntoDirectoryNames(path);
    int index = 0;
    Directory currentDirectory = this;

    // Current directory
    if (subDirectories.length == 0) {
      return currentDirectory;
    }

    while (index < subDirectories.length && directoryFound) {
      if (subDirectories[index].equals("..")) {
        currentDirectory = currentDirectory.getParentDirectory();
        directoryFound = true;
      } else if (subDirectories[index].equals(".")) {
        directoryFound = true;
      } else {
        directoryFound =
            currentDirectory.doesChildDirectoryExist(subDirectories[index]);
        if (directoryFound) {
          currentDirectory =
              currentDirectory.getChildDirectory(subDirectories[index]);
        }
      }
      index++;
    }

    if (directoryFound) {
      return currentDirectory;
    } else {
      throw new FileNotFoundException(path);
    }
  }

  /**
   * Returns the directory that corresponds to the specified path to the
   * directory. If the directory does not exist, returns null. The path
   * specified must be an absolute path and begin with '/'.
   * 
   * @param path The absolute path to the Directory
   * @return The Directory that corresponds to the path; If the directory does
   *         not exist, returns <tt>null</tt>.
   * @throws FileNotFoundException If the Directory does not exist
   */
  private Directory getDirectoryFromAbsolutePath(String path)
      throws FileNotFoundException {
    boolean directoryFound = true;
    String[] subDirectories = Directory.changePathIntoDirectoryNames(path);
    int index = 0;
    Directory directoryToReturn = this.getRootDirectory();

    // Root directory
    if (subDirectories.length == 0) {
      return directoryToReturn;
    }

    while (index < subDirectories.length && directoryFound) {
      if (subDirectories[index].equals("..")) {
        directoryToReturn = directoryToReturn.getParentDirectory();
        directoryFound = true;
      } else if (subDirectories[index].equals(".")) {
        directoryFound = true;
      } else {
        directoryFound =
            directoryToReturn.doesChildDirectoryExist(subDirectories[index]);
        if (directoryFound) {
          directoryToReturn =
              directoryToReturn.getChildDirectory(subDirectories[index]);
        }
      }
      index++;
    }

    if (directoryFound) {
      return directoryToReturn;
    } else {
      throw new FileNotFoundException(path);
    }
  }
}
