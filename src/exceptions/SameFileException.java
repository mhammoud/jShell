package exceptions;

/**
 * This class represents a SameFileException which is thrown when trying to copy
 * a File/Directory to a path that is equal to the same File.
 * 
 * @author bzhan
 *
 */
public class SameFileException extends Exception {
  private static final long serialVersionUID = -3391827208198717804L;

  public SameFileException(String filePath, String secondFilePath) {
    super("'" + filePath + "' and '" + secondFilePath + "' are the same file");
  }
}
