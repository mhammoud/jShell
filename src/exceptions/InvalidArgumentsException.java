package exceptions;

/**
 * This class represents an InvalidArgumentException which is thrown when an
 * invalid argument is passed into a Command.
 * 
 * @author Brendan Zhang
 *
 */
public class InvalidArgumentsException extends Exception {
  private static final long serialVersionUID = -7034586389002544029L;

  private InvalidArgumentsException(String message) {
    super(message + ": Invalid argument");
  }

  /**
   * Creates a new InvalidArgumentException with the message for the specified
   * command keyword and arguments supplied to the Command.
   * 
   * @param commandKeyword keyword for the command;
   * @param args arguments of the command
   * @return a new InvalidArgumentException for the command and arguments
   */
  public static InvalidArgumentsException createInvalidArgumentsException(
      String commandKeyword, String[] args) {
    // Create error message
    String message = commandKeyword;
    for (String arg : args) {
      message += " " + arg;
    }
    return new InvalidArgumentsException(message);
  }
}
