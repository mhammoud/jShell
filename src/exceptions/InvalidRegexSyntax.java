package exceptions;

/**
 * This class represents an invalid regex statement.
 * 
 * @author MohamedHammoud
 *
 */
public class InvalidRegexSyntax extends Exception {
  private static final long serialVersionUID = -5859797230162605212L;

  /**
   * Creates an invalid regex expression given the regex string.
   * 
   * @param message
   */
  private InvalidRegexSyntax(String message) {
    super("Invalid regex expression: " + message);
  }

  /**
   * Creates an invalid regex expression message using a given invalid regex
   * statement supplied to the Command.
   * 
   * @param rgx
   * @return
   */
  public static InvalidRegexSyntax createInvalidRegexSyntaxError(String rgx) {
    return new InvalidRegexSyntax(rgx);
  }


}
