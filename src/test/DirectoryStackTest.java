package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import utils.DirectoryStack;

/**
 * Tests that the correct directories are being placed on the stack and being
 * removed according to the principles of a Stack.
 * 
 * @author MohamedHammoud
 *
 */
public class DirectoryStackTest {

  private DirectoryStack dirStack;

  @Before
  public void setup() {
    dirStack = new DirectoryStack();
    dirStack.push("a\\b");
    dirStack.push("a\\b\\c");
  }

  @Test
  /**
   * Checks if stack follows LIFO.
   */
  public void testDirStackLIFOCheck() {
    Assert.assertEquals("a\\b\\c", dirStack.peek());
  }

  @Test
  /**
   * Check if stack removes an item correctly.
   */
  public void testDirStackPop() {
    Assert.assertEquals("a\\b\\c", dirStack.pop());
  }

  @Test
  /**
   * Tests if a stack has the correct length.
   */
  public void testDirStackLength() {
    Assert.assertEquals(2.0, dirStack.getLength(), 0);

  }

  @Test
  /**
   * Test pushing after popping.
   */
  public void testDirPushAndPop() {
    dirStack.pop();
    dirStack.push("a\\b\\def");
    Assert.assertEquals("a\\b\\def", dirStack.pop());
  }

  @Test
  /**
   * Test an empty stack.
   */
  public void testEmptyStack() {
    dirStack.pop();
    dirStack.pop(); // empty dir stack.
    Assert.assertEquals(
        "There are no saved directories to be able to change " + "to",
        dirStack.pop());
  }
}
