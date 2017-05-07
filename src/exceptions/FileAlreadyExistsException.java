package exceptions;

/**
 * This class represents a File/Directory Already Exists Exception that is
 * thrown when trying to create a File that already exists.
 * 
 * @author Brendan Zhang
 *
 */
public class FileAlreadyExistsException extends Exception {
  private static final long serialVersionUID = 23719075226886946L;


  public FileAlreadyExistsException(String directoryName) {
    super(directoryName + ": File or directory already exists");
  }
}
