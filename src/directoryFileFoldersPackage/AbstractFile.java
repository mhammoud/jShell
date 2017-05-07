package directoryFileFoldersPackage;

import exceptions.FileNotFoundException;
import exceptions.InvalidNameException;
import exceptions.RemoveRootDirectoryException;

/**
 * This abstract class contains the methods and variables that are common to
 * both Files and Directories, which includes a name and a parent Directory.
 * 
 * @author Brendan Zhang
 *
 */
public abstract class AbstractFile {

  /**
   * The name of the AbstractFile
   */
  private String name;
  /**
   * The Directory that the AbstractFile is contained in. A root Directory must
   * have a parent Directory of itself.
   */
  private Directory parentDirectory;

  /**
   * Returns the AbstractFile that corresponds to the specified path to the
   * AbstractFile for the specified FileSystem. If the directory does not exist,
   * prints a FileNotFoundException message and returns null.
   * 
   * @param path String that contains a path to the AbstractFile
   * @param fileSystem The FileSystem that the AbstractFile is stored in
   * @return The AbstractFile that corresponds to the path
   * @throws FileNotFoundException If the File does not Exist
   */
  public static AbstractFile getAbstractFileFromPath(String path,
      FileSystem fileSystem) throws FileNotFoundException {
    if (Directory.doesDirectoryExist(path, fileSystem)) {
      return Directory.getDirectoryFromPath(path, fileSystem);
    } else {
      return File.getFileFromPath(path, fileSystem);
    }
  }

  /**
   * Added the given AbstractFile to the given Directory.
   * 
   * @param fileToAdd The AbstractFile to add
   * @param directory The Directory to add to
   * @throws RemoveRootDirectoryException
   */
  public static void addToDirectory(AbstractFile fileToAdd, Directory directory)
      throws FileNotFoundException, RemoveRootDirectoryException {
    // Add the file to new path
    if (isFile(fileToAdd)) {
      // If the file already exists, remove the old file
      if (directory.doesFileExist(fileToAdd.getName())) {
        removeAbstractFile(directory.getFile(fileToAdd.getName()));
      }
      directory.addFile((File) fileToAdd);
    } else if (isDir(fileToAdd)) {
      directory.addSubDirectory((Directory) fileToAdd);
    }
  }

  /**
   * Removes the given AbstractFile. If the file being removed is the root
   * Directory, throw a RemoveRootDirectoryException.
   * 
   * @param fileToRemove The AbstractFile to remove.
   * @throws FileNotFoundException If the AbstractFile doesn't exist.
   * @throws RemoveRootDirectoryException If the File being removed is a root
   *         Directory.
   */
  public static void removeAbstractFile(AbstractFile fileToRemove)
      throws FileNotFoundException, RemoveRootDirectoryException {
    // Removes depending on if old file is a File or Directory
    if (isFile(fileToRemove)) {
      fileToRemove.getParentDirectory().removeFile(fileToRemove.getName());
    } else {
      if (((Directory) fileToRemove).isRoot()) {
        // Not allowed to remove root Directory
        throw new RemoveRootDirectoryException();
      } else {
        fileToRemove.getParentDirectory()
            .removeSubDirectory(fileToRemove.getName());
      }
    }
  }

  /**
   * Returns <tt>true</tt> if the given AbstractFile is of type File
   * 
   * @param file The AbstractFile
   * @return <tt>true</tt> if the AbstractFile is of type File. Otherwise,
   *         return <tt>false</tt>.
   */
  public static boolean isFile(AbstractFile file) {
    return file instanceof File;
  }

  /**
   * Returns <tt>true</tt> if the given AbstractFile is of type Directory
   * 
   * @param file The AbstractFile
   * @return <tt>true</tt> if the AbstractFile is of type Directory. Otherwise,
   *         return <tt>false</tt>.
   */
  public static boolean isDir(AbstractFile file) {
    return file instanceof Directory;
  }

  /**
   * Returns the name of the AbstractFile
   * 
   * @return String that contains the name of the AbstractFile
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name of the AbstractFile
   * 
   * @param name Name to set to
   * @throws InvalidNameException If the name to set to is invalid.
   */
  public void setName(String name) throws InvalidNameException {
    if (InvalidNameException.isNameInvalid(name)) {
      throw new InvalidNameException(name);
    }
    this.name = name;
  }

  /**
   * Sets the name of the AbstractFile without checking for a valid file name.
   * 
   * @param name Name to set to
   */
  public void setNameRootDirectory(String name) {
    this.name = name;
  }


  /**
   * Returns the name of the parent Directory of this AbstractFile
   * 
   * @return The parent Directory of this AbstractFile
   */
  public Directory getParentDirectory() {
    return parentDirectory;
  }


  /**
   * Sets the parent Directory of this AbstractFile
   * 
   * @param parentDirectory The parent Directory to set to
   */
  public void setParentDirectory(Directory parentDirectory) {
    this.parentDirectory = parentDirectory;
  }
}
