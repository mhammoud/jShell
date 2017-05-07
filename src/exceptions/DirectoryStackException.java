package exceptions;

/**
 * This class represents a DirectoryStackEmptyException that is thrown when
 * trying to execute a PushDirectory Command with an empty DirectoryStack.
 * 
 * @author Brendan Zhang
 *
 */
public class DirectoryStackException extends Exception {
  private static final long serialVersionUID = 458544092518036733L;

  public DirectoryStackException() {
    super("There are no saved directories to be able to change to");
  }
}
