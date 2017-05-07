package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import commands.ChangeDirectory;
import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.FileSystem;
import exceptions.InvalidNameException;
import mocks.MockFileSystem;

public class ChangeDirectoryTest {

  private static FileSystem fileSystem;
  private static ChangeDirectory changeDirectory;
  private static final String DIR_A = "dirA";
  private static final String DIR_B = "dirB";
  private static final String DIR_C = "dirC";
  private static final String DIR_D = "dirD";
  private static final String DIR_E = "dirE";

  @BeforeClass
  public static void beforeClassSetup() {
    fileSystem = new MockFileSystem();
  }

  @Before
  public void setup() {
    // Set current Directory to root Directory
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void testRootDirectory() throws InvalidNameException {
    String commandArgument = "/";
    changeDirectory =
        new ChangeDirectory(new String[] {commandArgument}, fileSystem);
    changeDirectory.executeCommand();
    Assert.assertEquals(Directory.createEmptyRootDirectory().toString(),
        fileSystem.getCurrentDirectory().toString());
  }

  @Test
  public void testChangingDirectory() throws InvalidNameException {
    changeDirectory = new ChangeDirectory(new String[] {DIR_B}, fileSystem);
    changeDirectory.executeCommand();
    Directory rootDirectory = Directory.createEmptyRootDirectory();
    Directory directoryOne = Directory.createSubDirectory(DIR_B, rootDirectory);
    Assert.assertEquals(directoryOne.toString(),
        fileSystem.getCurrentDirectory().toString());

    changeDirectory = new ChangeDirectory(new String[] {"/"}, fileSystem);
    changeDirectory.executeCommand();
    Assert.assertEquals(rootDirectory.toString(),
        fileSystem.getCurrentDirectory().toString());

    changeDirectory = new ChangeDirectory(new String[] {DIR_C}, fileSystem);
    changeDirectory.executeCommand();
    Directory directoryTwo = Directory.createSubDirectory(DIR_C, rootDirectory);
    Assert.assertEquals(directoryTwo.toString(),
        fileSystem.getCurrentDirectory().toString());
  }

  @Test
  public void testChangingMultipleDirectory() throws InvalidNameException {
    changeDirectory = new ChangeDirectory(new String[] {DIR_A}, fileSystem);
    changeDirectory.executeCommand();
    changeDirectory = new ChangeDirectory(new String[] {DIR_D}, fileSystem);
    changeDirectory.executeCommand();
    Directory rootDirectory = Directory.createEmptyRootDirectory();
    Directory directoryOne = Directory.createSubDirectory(DIR_A, rootDirectory);
    Directory directoryThree =
        Directory.createSubDirectory(DIR_D, directoryOne);
    Assert.assertEquals(directoryThree.toString(),
        fileSystem.getCurrentDirectory().toString());
  }

  @Test
  public void testFullPath() throws InvalidNameException {
    String commandArgument = "/dirA/dirD/dirE";
    changeDirectory =
        new ChangeDirectory(new String[] {commandArgument}, fileSystem);
    changeDirectory.executeCommand();
    Directory rootDirectory = Directory.createEmptyRootDirectory();
    Directory directoryOne = Directory.createSubDirectory(DIR_A, rootDirectory);
    Directory directoryTwo = Directory.createSubDirectory(DIR_D, directoryOne);
    Directory directoryThree =
        Directory.createSubDirectory(DIR_E, directoryTwo);
    Assert.assertEquals(directoryThree.toString(),
        fileSystem.getCurrentDirectory().toString());
  }

  @Test
  public void testLongerRelativePath() throws InvalidNameException {
    String commandArgument = "dirA/dirD/dirE";
    changeDirectory =
        new ChangeDirectory(new String[] {commandArgument}, fileSystem);
    changeDirectory.executeCommand();
    Directory rootDirectory = Directory.createEmptyRootDirectory();
    Directory directoryOne = Directory.createSubDirectory(DIR_A, rootDirectory);
    Directory directoryTwo = Directory.createSubDirectory(DIR_D, directoryOne);
    Directory directoryThree =
        Directory.createSubDirectory(DIR_E, directoryTwo);
    Assert.assertEquals(directoryThree.toString(),
        fileSystem.getCurrentDirectory().toString());
  }

  @Test
  public void testInvalidPath() {
    String commandArgument = "/dirE";
    changeDirectory =
        new ChangeDirectory(new String[] {commandArgument}, fileSystem);
    changeDirectory.executeCommand();
    Assert.assertEquals("", changeDirectory.getOutput());
    Assert.assertEquals("/dirE: No such file or directory",
        changeDirectory.getErrorMessage());
  }

  @Test
  public void testInvalidArguments() {
    changeDirectory = new ChangeDirectory(
        new String[] {"number", "of", "arguments", "too", "large"}, fileSystem);
    changeDirectory.executeCommand();
    Assert.assertEquals("", changeDirectory.getOutput());
    Assert.assertEquals("cd number of arguments too large: Invalid argument",
        changeDirectory.getErrorMessage());
  }
}
