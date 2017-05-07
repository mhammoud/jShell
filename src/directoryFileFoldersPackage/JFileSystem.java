package directoryFileFoldersPackage;

import java.util.ArrayList;

import utils.DirectoryStack;

/**
 * This class represents a JFileSystem.
 * <p>
 * A JFileSystem consists of a current Directory, a root Directory, a
 * DirectoryStack, and InputLog.
 * 
 * The Directory Stack stores the Directories saved by PushDirectory. The
 * InputLog stores all the inputs that the user has given to this
 * FileSystemInterface.
 * 
 * @author Brendan Zhang
 *
 */
public class JFileSystem implements FileSystem {

  /**
   * Only have one instance of JFileSystem at any point in time. Set it
   * initially to null.
   */
  private static JFileSystem fileSystem = null;

  /**
   * The root Directory of this JFileSystem.
   */
  private Directory rootDirectory;

  /**
   * The current Directory of this JFileSystem.
   */
  private Directory currentDirectory;

  /**
   * The Directories that have been saved to this JFileSystem using the
   * PushDirectory command.
   */
  private DirectoryStack savedDirectories;

  /**
   * A List of Strings that contain all the commands sent to this JFileSystem.
   */
  private ArrayList<String> inputLog;

  /**
   * Private class constructor to hide instantiation.
   */
  private JFileSystem() {
    this.rootDirectory = Directory.createEmptyRootDirectory();
    this.currentDirectory = rootDirectory;
    this.savedDirectories = new DirectoryStack();
    this.inputLog = new ArrayList<String>();
  }

  /**
   * Returns an instance of JFileSystem. Only one instance of JFileSystem is
   * created at any point in time.
   * 
   * @return An instance of JFileSystem.
   */
  public static JFileSystem getFileSystem() {
    if (fileSystem == null) {
      fileSystem = new JFileSystem();
    }
    return fileSystem;
  }

  public Directory getCurrentDirectory() {
    return currentDirectory;
  }

  public void setCurrentDirectory(Directory currentDirectory) {
    this.currentDirectory = currentDirectory;
  }

  public Directory getRootDirectory() {
    return rootDirectory;
  }

  public DirectoryStack getSavedDirectoryStack() {
    return this.savedDirectories;
  }

  public ArrayList<String> getInputLog() {
    return inputLog;
  }
}
