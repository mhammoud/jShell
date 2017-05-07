package exceptions;

/**
 * This class represents an InvalidCommandException that is thrown when trying
 * to execute a command with an invalid keyword.
 * 
 * @author Brendan Zhang
 *
 */
public class InvalidCommandException extends Exception {
  private static final long serialVersionUID = 3803553275608297886L;

  public InvalidCommandException(String command) {
    super(command + ": Invalid command");
  }
}
