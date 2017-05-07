package exceptions;

/**
 * This class represents a MoveIntoSubdirectoryException that is thrown when
 * trying to move a Directory into one of it's sub-directories.
 * 
 * @author Brendan Zhang
 *
 */
public class MoveIntoSubdirectoryException extends Exception {
  private static final long serialVersionUID = 9221926378306785854L;

  public MoveIntoSubdirectoryException(String source, String dest) {
    super("Cannot move '" + source + "' to a subdirectory of itself, '" + dest
        + "'");
  }
}
