package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.Exit;
import driver.JShell;

public class ExitTest {
  /**
   * The Exit command to execute
   */
  private Exit exit;
  /**
   * The JShell to use with Exit command
   */
  private JShell shell;

  @Before
  public void setup() {
    shell = new JShell();
    exit = new Exit(shell);
  }

  @Test
  public void testExit() {
    exit.setArgs(new String[] {});
    exit.executeCommand();
    Assert.assertEquals(false, shell.isRunning());
  }

  @Test
  public void testExitArguments() {
    exit.setArgs(new String[] {"someArguments", "onetwothree"});
    exit.executeCommand();
    Assert.assertEquals(false, shell.isRunning());
  }
}
