package exceptions;

/**
 * This class represents an Exception that is thrown when trying to use the
 * alias "!n" when there is no Command associated with the history number n.
 * 
 * @author Brendan Zhang
 *
 */
public class InvalidNumberException extends Exception {
  private static final long serialVersionUID = 403311051361156896L;

  public InvalidNumberException(String str) {
    super(str + ": No command associated with history number");
  }
}
