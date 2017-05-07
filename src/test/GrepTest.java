package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.Grep;
import directoryFileFoldersPackage.FileSystem;
import mocks.MockFileSystem;

public class GrepTest {
  
  private FileSystem fileSystem;
  private Grep grep;
  
  @Before
  public void setup() {
    fileSystem = new MockFileSystem();
    grep = new Grep();
    grep.setFileSystem(fileSystem);
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }
  
  @Test
  /**
   * This test checks if a grep can be performed on the contents of a fileTwo.
   * The regex used is an empty string "" - which matches all occurrences.
   */
  public void testGrepSingleFile() {
    grep.setArgs(new String[] {"\"\"", "dirA/fileTwo"});
    grep.executeCommand();
    Assert
    .assertEquals("fileTwo from A", grep.getOutput());
  }
  
  @Test
  /**
   * This test checks if a grep can be performed on the contents of fileTwo.
   * With a regex expression that has no matches.
   */
  public void testGrepSingleFileNoMatch() {
    grep.setArgs(new String[]{"\"[x-z]\"", "dirA/fileTwo"});
    grep.executeCommand();
    Assert.assertEquals("", grep.getOutput());
  }
  
  @Test
  /**
   * This test checks if grep throws a successful exception given that
   * a regex is invalid. A "*" is an invalid regex.
   */
  public void testGrepSingleFileInvalidRegex() {
    grep.setArgs(new String[]{"\"*\"", "dirA/fileTwo"});
    grep.executeCommand();
    Assert.assertEquals("Invalid regex expression: *", grep.getErrorMessage());
  }
  
  @Test
  /** 
   * This test checks if a grep, given multiple files, if any of the files
   * match the regex "file", that is, if any lines contain the word file.
   */
  public void testGrepMultipleFilesMatch() {
    grep.setArgs(new String[]{"\"file\"", "dirA/fileTwo", "dirA/fileOne"});
    grep.executeCommand();
    Assert
    .assertEquals("fileTwo from A\ncontents of fileOne", grep.getOutput());
  }
  
  @Test
  /**
   * This test checks if grep returns a FileNotFound error since the file
   * does not exist.
   */
  public void testGrepFileDoesNotExist() {
    grep.setArgs(new String[]{"\"[abc]\"", "dirA/fileNine"});
    grep.executeCommand();
    Assert
    .assertEquals("dirA: No such file or directory", 
        grep.getErrorMessage());
    
  }

  @Test
  /**
   * This test checks for an invalid arguments error since
   * a file is missing in the arguments string.
   */
  public void testGrepFileNotProvided() {
    grep.setArgs(new String[]{"\"\""});
    grep.executeCommand();
    Assert
    .assertEquals("grep \"\": Invalid argument", 
        grep.getErrorMessage());
  }
  
  @Test
  /**
   * This test checks if grep returns an error if quotes are missing.
   */
  public void testGrepMissingQuotations() {
    grep.setArgs(new String[]{"[abc]", "dirA/fileOne"});
    grep.executeCommand();
    Assert
    .assertEquals("Invalid regex expression: Missing quotations", 
        grep.getErrorMessage()); 
  }
  
  @Test
  /**
   * This test checks if grep correctly performs a recursive grep
   * on dirA for the regex "fileTwo"
   */
  public void testGrepRecursiveDirectory() {
    grep.setArgs(new String[]{"-R", "\"fileTwo\"", "dirA"});
    grep.executeCommand();
    String expected = "/dirA/fileTwo: fileTwo from A" + "\n"
        + "/dirA/dirD/fileTwo: fileTwo from D";
    Assert.assertEquals(expected, grep.getOutput());
  }
  
  @Test
  /**
   * This test checks a recursive grep performs correctly on a directory
   * with no files, using "" as a regex to match any occurrence of a string.
   */
  public void testGrepRecursiveNoFiles() {
    grep.setArgs(new String[]{"-r", "\"\"", "dirC"});
    grep.executeCommand();
    Assert.assertEquals("", grep.getOutput());
  }
  
  @Test
  /** This test checks if an invalid regex returns the correct error
   * given that a recursive call is occurring.
   * 
   */
  public void testGrepRecursiveInvalidRegex() {
    grep.setArgs(new String[]{"-r", "\"*\"", "dirC"});
    grep.executeCommand();
    Assert.assertEquals("Invalid regex expression: *", grep.getErrorMessage());
  }
  
  @Test
  /** 
   * This test checks if an argument error is returned if a directory
   * is not provided to a recursive call.
   */
  public void testGrepRecursiveMissingDirectory() {
    grep.setArgs(new String[]{"-r", "\"\""});
    grep.executeCommand();
    Assert.assertEquals("grep -r \"\": Invalid argument", 
        grep.getErrorMessage());
    
  }
  
  @Test
  /**
   * This test checks dirA for any matches on a slightly more complex regex.
   * Regex (One|Two)
   */
  public void testGrepRecursiveComplexRegex() {
    grep.setArgs(new String[]{"-r", "\"(One|Two)\"", "dirA"});
    grep.executeCommand();
    String expected = "/dirA/fileOne: contents of fileOne" + "\n"
        + "/dirA/fileTwo: fileTwo from A" + "\n"
        + "/dirA/dirD/fileTwo: fileTwo from D";
    Assert.assertEquals(expected, grep.getOutput()); 
  }
  
  @Test
  /**
   * This test runs through multiple directories recursively to match
   * a string that contains "file*".
   */
  public void testGrepMultipleDirectories() {
    grep.setArgs(new String[]{"-r", "\"[file](One|Two)\"", "dirA", "dirC"});
    grep.executeCommand();
    String expected = "/dirA/fileOne: contents of fileOne" + "\n"
        + "/dirA/fileTwo: fileTwo from A" + "\n"
        + "/dirA/dirD/fileTwo: fileTwo from D";
    Assert.assertEquals(expected, grep.getOutput());
    
  }
  
  
}
