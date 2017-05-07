package exceptions;

/**
 * This class represents a RemoveRootDirectoryException which is thrown when
 * trying to remove a root Directory.
 * 
 * @author Brendan Zhang
 *
 */
public class RemoveRootDirectoryException extends Exception {
  private static final long serialVersionUID = 9221926378306785854L;

  public RemoveRootDirectoryException() {
    super("Cannot remove root Directory");
  }
}
