package exceptions;

/**
 * This class represents a File/Directory not found Exception that is thrown
 * when trying to access a File/Directory that doesn't exist.
 * 
 * @author Brendan Zhang
 *
 */
public class FileNotFoundException extends Exception {
  private static final long serialVersionUID = 7320010673813201354L;

  public FileNotFoundException(String path) {
    super(path + ": No such file or directory");
  }
}
