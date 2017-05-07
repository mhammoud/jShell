package exceptions;

/**
 * This class represents an Invalid Name Exception that is thrown when creating
 * a File or Directory with an invalid name.
 * 
 * @author Brendan Zhang
 *
 */
public class InvalidNameException extends Exception {
  private static final long serialVersionUID = -202441463157790602L;

  public InvalidNameException(String name) {
    super(name + ": Invalid directory or file name");
  }

  /**
   * Creates a new InvalidNameError with the message for the specified command
   * keyword and arguments supplied to the Command.
   * 
   * @param commandKeyword keyword for the command;
   * @param args arguments of the command
   * @return a new InvalidNameError for the command and arguments
   */
  public static InvalidNameException createInvalidNameException(
      String commandKeyword, String[] args) {
    // Create error message
    String message = commandKeyword;
    for (String arg : args) {
      message += " " + arg;
    }
    return new InvalidNameException(message);
  }

  /**
   * Returns true if the name is a valid File or Directory name.
   * 
   * @param name File or Directory name
   * @return <tt>true</tt> if the name is a valid. Otherwise, return
   *         <tt>false</tt>.
   */
  public static boolean isNameInvalid(String name) {
    // Null name is not valid
    if (name == null) {
      return true;
    }
    // Empty string is not valid
    if (name.length() == 0) {
      return true;
    }
    // Reserved keywords are invalid names.
    if (name.equals(".") || name.equals("..")) {
      return true;
    }
    // Illegal characters cannot be in the name
    String illegalCharacters = "!@$&*()?:[]\"<>'`|={}\\/,;";
    for (int i = 0; i < illegalCharacters.length(); i++) {
      if (name.contains(illegalCharacters.substring(i, i + 1))) {
        return true;
      }
    }
    // Valid name
    return false;
  }
}
