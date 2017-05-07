package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.Concatenate;
import commands.Echo;
import directoryFileFoldersPackage.FileSystem;
import mocks.MockFileSystem;
import writer.FileWriter;

public class EchoTest {

  private Echo echo;
  private FileSystem fileSystem;
  private Concatenate cat;

  @Before
  public void setUp() {
    fileSystem = new MockFileSystem();
    echo = new Echo();
    echo.setFileSystem(fileSystem);
  }

  @Test
  public void testEchoStringToStandardOutput() {
    echo.setArgs(new String[] {"\"abc\""});
    echo.executeCommand();
    Assert.assertEquals("abc", echo.getOutput());

    echo = new Echo();
    echo.setArgs(new String[] {"\"Hello World\""});
    echo.executeCommand();
    Assert.assertEquals("Hello World", echo.getOutput());

  }

  @Test
  public void testEchoAppendStringToFileWithCat() {
    echo.setArgs(new String[] {"\"abc\""});
    echo.setWriter(new FileWriter("hello", true, fileSystem));
    echo.executeCommand();
    cat = new Concatenate(new String[] {"hello"}, fileSystem);
    cat.executeCommand();
    Assert.assertEquals("abc", cat.getOutput());
  }

  @Test
  public void testEchoWriteStringToFileWithCat() {
    echo.setArgs(new String[] {"\"abc\""});
    echo.setWriter(new FileWriter("hello", false, fileSystem));
    echo.executeCommand();
    cat = new Concatenate(new String[] {"hello"}, fileSystem);
    cat.executeCommand();
    Assert.assertEquals("abc", cat.getOutput());
  }

  @Test
  public void testMultipleAppendStringToFileWithCat() {
    echo.setArgs(new String[] {"\"aaa\""});
    echo.setWriter(new FileWriter("hello", true, fileSystem));
    echo.executeCommand();

    echo = new Echo(new String[] {"\"ccc\""}, fileSystem);
    echo.setWriter(new FileWriter("hello", true, fileSystem));
    echo.executeCommand();

    echo = new Echo(new String[] {"\"ddd\""}, fileSystem);
    echo.setWriter(new FileWriter("hello", true, fileSystem));
    echo.executeCommand();

    cat = new Concatenate(new String[] {"hello"}, fileSystem);
    cat.executeCommand();
    Assert.assertEquals("aaa\nccc\nddd", cat.getOutput());
  }

  @Test
  public void testMultipleWriteStringToFileWithCat() {
    echo.setArgs(new String[] {"\"aaa\""});
    echo.setWriter(new FileWriter("hello", false, fileSystem));
    echo.executeCommand();

    echo = new Echo(new String[] {"\"ccc\""}, fileSystem);
    echo.setWriter(new FileWriter("hello", false, fileSystem));
    echo.executeCommand();

    echo = new Echo(new String[] {"\"ddd\""}, fileSystem);
    echo.setWriter(new FileWriter("hello", false, fileSystem));
    echo.executeCommand();

    Concatenate cat = new Concatenate(new String[] {"hello"}, fileSystem);
    cat.executeCommand();
    Assert.assertEquals("ddd", cat.getOutput());
  }

  @Test
  public void testTooManyArguments() {
    echo.setArgs(new String[] {"\"aaa\"", "hello"});
    echo.executeCommand();
    Assert.assertEquals("", echo.getOutput());
    Assert.assertEquals("echo \"aaa\" hello: Invalid argument",
        echo.getErrorMessage());
  }

  @Test
  public void testEchoToInvalidFileNameWithCat() {
    echo = new Echo(new String[] {"\"ddd\""}, fileSystem);
    echo.setWriter(new FileWriter("hello@!", false, fileSystem));
    echo.executeCommand();
    Assert.assertEquals("ddd", echo.getOutput());
    Assert.assertEquals("hello@!: Invalid directory or file name",
        ((FileWriter) (echo.getWriter())).getErrorMessage());
    Concatenate cat = new Concatenate(new String[] {"hello@!"}, fileSystem);
    cat.executeCommand();
    Assert.assertEquals("", cat.getOutput());
  }
}
