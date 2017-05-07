package test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.History;
import mocks.MockFileSystem;

/**
 * Tests the history command to ensure proper retrieval of an n-number of last
 * used commands on a log.
 * 
 * @author MohamedHammoud
 *
 */
public class HistoryTest {

  private History history;
  private ArrayList<String> inputLog;
  private MockFileSystem mockFileSystem;

  @Before
  public void setup() {
    inputLog = new ArrayList<String>();
    mockFileSystem = new MockFileSystem();
  }

  @Test
  /**
   * Tests that the command log returns "history" when nothing else is on the
   * log.
   */
  public void testReturnEmptyCmdLog() {
    inputLog.add("history");
    mockFileSystem.setInputLog(inputLog);
    history = new History(new String[] {"1"}, mockFileSystem);
    history.executeCommand();
    Assert.assertEquals("1.history", history.getOutput());

  }

  @Test
  /**
   * Returns all commands used without any parameters.
   */
  public void testReturnAllCmdLog() {
    inputLog.add("history");
    inputLog.add("ls");
    mockFileSystem.setInputLog(inputLog);
    history = new History(new String[] {"2"}, mockFileSystem);
    history.executeCommand();
    String expected = "1.history\n2.ls";
    String actual = history.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  /**
   * Returns some of the command logs.
   */
  public void testReturnSomeCmdLog() {
    inputLog.add("history");
    inputLog.add("cd d invalid cmd");
    inputLog.add("cat f1");
    mockFileSystem.setInputLog(inputLog);
    history = new History(new String[] {"2"}, mockFileSystem);
    history.executeCommand();
    String expected = "2.cd d invalid cmd\n3.cat f1";
    String actual = history.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  /**
   * If n > size of arraylist, it returns all of commands used.
   */
  public void testReturnMoreThanSizeOfCmdLog() {
    inputLog.add("history");
    inputLog.add("cat f1");
    inputLog.add("history 4");
    mockFileSystem.setInputLog(inputLog);
    history = new History(new String[] {"4"}, mockFileSystem);
    history.executeCommand();
    String expected = "1.history\n2.cat f1\n3.history 4";
    String actual = history.getOutput();
    Assert.assertEquals(expected, actual);

  }

  @Test
  /**
   * Tests an invalid argument.
   */
  public void testInvalidArguments() {
    inputLog.add("history");
    mockFileSystem.setInputLog(inputLog);
    history = new History(new String[] {"gg"}, mockFileSystem);
    history.executeCommand();
    String expected = "history gg: Invalid argument";
    String actual = history.getErrorMessage();
    Assert.assertEquals(expected, actual);

  }
}
