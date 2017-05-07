package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.ChangeDirectory;
import commands.Concatenate;
import commands.Copy;
import commands.Curl;
import commands.Echo;
import commands.Exit;
import commands.Grep;
import commands.History;
import commands.ListSegments;
import commands.MakeDirectory;
import commands.Manual;
import commands.Move;
import commands.PopDirectory;
import commands.PresentWorkingDirectory;
import commands.PushDirectory;

public class ManualTest {
  private Manual manual;
  private String expected;
  private String actual;

  @Before
  public void setup() {
    manual = new Manual();
    expected = "";
    actual = "";
  }

  @Test
  public void testChangeDirectory() {
    manual.setArgs(new String[] {"cd"});
    manual.executeCommand();
    expected = ChangeDirectory.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testConcatenate() {
    manual.setArgs(new String[] {"cat"});
    manual.executeCommand();
    expected = Concatenate.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testEcho() {
    manual.setArgs(new String[] {"echo"});
    manual.executeCommand();
    expected = Echo.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testExit() {
    manual.setArgs(new String[] {"exit"});
    manual.executeCommand();
    expected = Exit.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testHistory() {
    manual.setArgs(new String[] {"history"});
    manual.executeCommand();
    expected = History.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testListSegments() {
    manual.setArgs(new String[] {"ls"});
    manual.executeCommand();
    expected = ListSegments.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testMakeDirectory() {
    manual.setArgs(new String[] {"mkdir"});
    manual.executeCommand();
    expected = MakeDirectory.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testManual() {
    manual.setArgs(new String[] {"man"});
    manual.executeCommand();
    expected = Manual.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testPopDirectory() {
    manual.setArgs(new String[] {"popd"});
    manual.executeCommand();
    expected = PopDirectory.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testPresentWorkingDirectory() {
    manual.setArgs(new String[] {"pwd"});
    manual.executeCommand();
    expected = PresentWorkingDirectory.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testPushDirectory() {
    manual.setArgs(new String[] {"pushd"});
    manual.executeCommand();
    expected = PushDirectory.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testCurl() {
    manual.setArgs(new String[] {"curl"});
    manual.executeCommand();
    expected = Curl.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testCopy() {
    manual.setArgs(new String[] {"cp"});
    manual.executeCommand();
    expected = Copy.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testGrep() {
    manual.setArgs(new String[] {"grep"});
    manual.executeCommand();
    expected = Grep.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testMove() {
    manual.setArgs(new String[] {"mv"});
    manual.executeCommand();
    expected = Move.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testWrongCommand() {
    manual.setArgs(new String[] {"sdfsfs"});
    manual.executeCommand();
    expected = "No manual entry for sdfsfs";
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testNoCommand() {
    manual.setArgs(new String[] {});
    manual.executeCommand();
    expected = "What manual page do you want?";
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }


  @Test
  public void testCommandWithArguments() {
    manual.setArgs(new String[] {"pushd", "123"});
    manual.executeCommand();
    expected = PushDirectory.getManMessage();
    actual = manual.getOutput();
    Assert.assertEquals(expected, actual);
  }

}
