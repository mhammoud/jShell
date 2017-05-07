package exceptions;

/**
 * This class represents an InvalidFileTypeException which is thrown when trying
 * to get a File using the Curl command that isn't of type .html or .txt.
 * 
 * @author Brendan Zhang
 *
 */
public class InvalidFileTypeException extends Exception {
  private static final long serialVersionUID = 7752695486968642672L;

  public InvalidFileTypeException(String file) {
    super(file + ": Invalid file type. Must be .html or .txt");
  }
}
