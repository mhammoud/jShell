package utils;

import java.util.ArrayList;

import exceptions.DirectoryStackException;

/**
 * A Stack ADT for use of holding absolute root paths.
 * 
 * @author MohamedHammoud
 *
 */
public class DirectoryStack {

  private ArrayList<String> dirStack;
  private int length;

  /**
   * Creates an empty directory stack.
   */
  public DirectoryStack() {
    this.dirStack = new ArrayList<String>();
    this.length = 0;
  }

  /**
   * Pushes an absolute directory path onto the stack.
   * 
   * @param directory
   */
  public void push(String absRootPath) {
    this.dirStack.add(absRootPath);
    this.length++;
  }

  /**
   * Returns the absolute value path at the top of the stack, and removes it
   * from the stack.
   * 
   * @return
   */
  public String pop() {
    
    if (this.length > 0) { 
      this.length--;
      return this.dirStack.remove(this.length);
    }
    
    // raise an error.
    DirectoryStackException dseError = new DirectoryStackException();
    return dseError.getMessage();
  }

  /**
   * Returns the absolute value path at the top of the stack but does not remove
   * it.
   * 
   * @return
   */
  public String peek() {
    return this.dirStack.get(this.length - 1);
  }

  /**
   * Returns the length of the stack.
   * 
   * @return
   */
  public int getLength() {
    return this.length;
  }

}
