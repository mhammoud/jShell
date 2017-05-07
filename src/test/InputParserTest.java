package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.ChangeDirectory;
import commands.Command;
import commands.Echo;
import driver.JShell;
import exceptions.InvalidCommandException;
import exceptions.InvalidNumberException;
import utils.InputParser;

public class InputParserTest {

  private InputParser inputParser;

  @Before
  public void setup() {
    inputParser = new InputParser(new JShell());
  }

  @Test
  public void testParseInputChangeDirectory()
      throws InvalidCommandException, InvalidNumberException {
    Command command = inputParser.parseInput("cd a");
    Assert.assertEquals("ChangeDirectory", command.getClass().getSimpleName());
    Assert.assertArrayEquals(new String[] {"a"},
        ((ChangeDirectory) command).getArgs());

    Command commandTwo = inputParser.parseInput("cd a b/c c/d ./d");
    Assert.assertEquals("ChangeDirectory",
        commandTwo.getClass().getSimpleName());
    Assert.assertArrayEquals(new String[] {"a", "b/c", "c/d", "./d"},
        ((ChangeDirectory) commandTwo).getArgs());
  }

  @Test
  public void testParseInputEcho()
      throws InvalidCommandException, InvalidNumberException {
    Command command = inputParser.parseInput("echo \"abc\"");
    Assert.assertEquals("Echo", command.getClass().getSimpleName());
    Assert.assertArrayEquals(new String[] {"\"abc\""},
        ((Echo) command).getArgs());

    command = inputParser.parseInput("echo \"abc\" >> hello");
    Assert.assertEquals("Echo", command.getClass().getSimpleName());
    Assert.assertArrayEquals(new String[] {"\"abc\""},
        ((Echo) command).getArgs());

    command = inputParser.parseInput("echo \"abc\" > hello");
    Assert.assertEquals("Echo", command.getClass().getSimpleName());
    Assert.assertArrayEquals(new String[] {"\"abc\""},
        ((Echo) command).getArgs());

    command = inputParser.parseInput("echo \"abc\" >>> hello");
    Assert.assertEquals("Echo", command.getClass().getSimpleName());
    Assert.assertArrayEquals(new String[] {"\"abc\"", ">>>", "hello"},
        ((Echo) command).getArgs());
  }

  @Test
  public void testInvalidCommand()
      throws InvalidCommandException, InvalidNumberException {
    Command command = inputParser.parseInput("");
    Assert.assertEquals(null, command);

    command = inputParser.parseInput("            ");
    Assert.assertEquals(null, inputParser.getErrorMessage());
  }
}
