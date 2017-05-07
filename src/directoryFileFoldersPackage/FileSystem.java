package directoryFileFoldersPackage;

import java.util.ArrayList;

import utils.DirectoryStack;

/**
 * A FileSystem.
 * <p>
 * A user of this interface is able to change the current Directory, get the
 * root Directory and get the DirectoryStack and InputLog.
 * 
 * The Directory Stack stores the Directories saved by PushDirectory. The
 * InputLog stores all the inputs that the user has given to this
 * FileSystemInterface.
 * 
 * @author Brendan Zhang
 * @see Directory
 * @see DirectoryStack
 * @see InputLog
 *
 */
public interface FileSystem {

  /**
   * Returns the current Directory of this FileSystem.
   * 
   * @return the current Directory
   */
  public Directory getCurrentDirectory();

  /**
   * Sets the current Directory of this FileSystem.
   * 
   * @param currentDirectory the Directory to set
   */
  public void setCurrentDirectory(Directory currentDirectory);

  /**
   * Returns the root Directory of this FileSystem.
   * 
   * @return the root Directory
   */
  public Directory getRootDirectory();

  /**
   * Returns the DirectoryStack of the FileSystem. The DirectoryStack contains
   * the Directories saved by PushDirectory.
   * 
   * @return DirectoryStack of the FileSystemInterface
   */
  public DirectoryStack getSavedDirectoryStack();

  /**
   * Returns the InputLog of the FileSystem. The InputLog contains the commands
   * that the user has given to this FileSystem.
   * 
   * @return InputLog of the FileSystemInterface
   */
  public ArrayList<String> getInputLog();
}
