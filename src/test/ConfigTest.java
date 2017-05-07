package test;

import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import utils.CommandTable;

public class ConfigTest {

  private Hashtable<String, String> ht;

  @Before
  public void setup() {
    // set up by getting the config's hash table
    ht = CommandTable.getCommandTable();
  }

  // Test every command input returns the correct command to import

  @Test
  public void testMakeDirectory() {
    Assert.assertEquals("commands.MakeDirectory", ht.get("mkdir"));
  }

  @Test
  public void testChangeDirectory() {
    Assert.assertEquals("commands.ChangeDirectory", ht.get("cd"));
  }

  @Test
  public void testListSegments() {
    Assert.assertEquals("commands.ListSegments", ht.get("ls"));
  }

  @Test
  public void testExit() {
    Assert.assertEquals("commands.Exit", ht.get("exit"));
  }

  @Test
  public void testPresentWorkingDirectory() {
    Assert.assertEquals("commands.PresentWorkingDirectory", ht.get("pwd"));
  }

  @Test
  public void testEcho() {
    Assert.assertEquals("commands.Echo", ht.get("echo"));
  }

  @Test
  public void testManual() {
    Assert.assertEquals("commands.Manual", ht.get("man"));
  }

  @Test
  public void testPopDirectory() {
    Assert.assertEquals("commands.PopDirectory", ht.get("popd"));
  }

  @Test
  public void testPushDirectory() {
    Assert.assertEquals("commands.PushDirectory", ht.get("pushd"));
  }

  @Test
  public void testHistory() {
    Assert.assertEquals("commands.History", ht.get("history"));
  }

  @Test
  public void testConcatenate() {
    Assert.assertEquals("commands.Concatenate", ht.get("cat"));
  }

  @Test
  public void testCurl() {
    Assert.assertEquals("commands.Curl", ht.get("curl"));
  }

}
