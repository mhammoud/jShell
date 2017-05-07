package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.Copy;
import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.File;
import directoryFileFoldersPackage.FileSystem;
import exceptions.FileNotFoundException;
import mocks.MockFileSystem;

public class CopyTest {

  private FileSystem fileSystem = new MockFileSystem();
  private Copy copy;
  private String file;
  private String newLoc;

  /**
   * MockFileSystem is set as: root has: dirA, dirB, dirC dirA has: dirD,
   * fileOne, fileTwo, fileThree dirD has: dirE, fileTwo, fileFour
   */
  @Before
  public void setUp() {
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
    copy = new Copy();
    copy.setFileSystem(fileSystem);
  }

  /**
   * Moving fileOne to dirC given a directory as new location
   */
  @Test
  public void testMoveFileGivenNewDir() {
    String fileOne = "fileOne";
    file = "./dirA/fileOne";
    newLoc = "./dirC";
    copy.setArgs(new String[] {file, newLoc});
    copy.executeCommand();

    // Check all directories
    Assert.assertTrue("The File 'fileOne' is not in dirC",
        File.doesFileExist(newLoc + "/" + fileOne, fileSystem));
  }

  /**
   * Moving fileOne to dirC given the new path it will be at
   */
  @Test
  public void testMoveFileGivenNewPath() {
    file = "./dirA/fileOne";
    newLoc = "./dirC/fileOne";
    copy.setArgs(new String[] {file, newLoc});
    copy.executeCommand();

    // Check all directories
    Assert.assertTrue("The File 'fileOne' is not in dirC",
        File.doesFileExist(newLoc, fileSystem));
  }

  /**
   * Moving dirC to dirA given a directory as new location
   */
  @Test
  public void testMoveDirGivenDir() {
    file = "./dirC";
    newLoc = "./dirA";
    copy.setArgs(new String[] {file, newLoc});
    copy.executeCommand();

    // Check all directories
    Assert.assertTrue("The Directory 'dirA' does not exist",
        Directory.doesDirectoryExist(newLoc, fileSystem));
  }

  /**
   * Moving dirC to dirA given the new path it will be at
   */
  @Test
  public void testMoveDirGivenNewPath() {
    file = "./dirC";
    newLoc = "./dirA/dirC";
    copy.setArgs(new String[] {file, newLoc});
    copy.executeCommand();

    // Check all directories
    Assert.assertTrue("The Directory 'dirA' does not exist",
        Directory.doesDirectoryExist(newLoc, fileSystem));
  }

  /**
   * Moving fileOne in dirB to dirC, throws FileNotFoundException and does not
   * move anything
   */
  @Test
  public void testInvalidFile() {
    file = "./dirB/fileOne";
    newLoc = "./dirC";
    copy.setArgs(new String[] {file, newLoc});
    copy.executeCommand();

    // Check all directories
    Assert.assertEquals(copy.getErrorMessage(),
        "./dirB/fileOne: No such file or directory");
  }

  /**
   * Moving dirF to dirC, throws FileNotFOundException and does not move
   * anything
   */
  @Test
  public void testInvalidDir() {
    file = "./dirF";
    newLoc = "./dirC";
    copy.setArgs(new String[] {file, newLoc});
    copy.executeCommand();

    // Check all directories
    Assert.assertEquals(copy.getErrorMessage(),
        "./dirF: No such file or directory");
  }

  /**
   * Moving fileTwo in dirA to dirD, replaces the fileTwo in dirD with the one
   * in dirA
   * 
   * @throws FileNotFoundException If File couldn't be found
   */
  @Test
  public void testSameFileName() throws FileNotFoundException {
    String fileName = "fileTwo";
    file = "./dirA/" + fileName;
    newLoc = "./dirA/dirD";
    copy.setArgs(new String[] {file, newLoc});
    copy.executeCommand();

    String expectedContents = "fileTwo from A";
    String actualContents =
        File.getFileFromPath(newLoc + "/" + fileName, fileSystem).getContents();
    Assert.assertEquals(
        "Expected the contents of /dirA/dirD/fileTwo to be: '"
            + expectedContents + "' but actually received " + actualContents,
        expectedContents, actualContents);
  }

  /**
   * Moving fileOne back into dirA, should not move any files around
   */
  @Test
  public void testMoveToSameDir() {
    file = "./dirA/fileOne";
    newLoc = "./dirA";
    copy.setArgs(new String[] {file, newLoc});
    copy.executeCommand();

    // Check all directories
    Assert.assertTrue("The File 'fileOne' is not in dirA",
        File.doesFileExist(file, fileSystem));
  }

  /**
   * Moving dirA into dirE, throws MovingIntoSubdirectoryException and should
   * not move any files
   */
  @Test
  public void testMovingParentDir() {
    file = "./dirA";
    newLoc = "./dirA/dirD/dirE";
    copy.setArgs(new String[] {file, newLoc});
    copy.executeCommand();

    // Check all directories
    Assert.assertEquals("Cannot move './dirA' to a subdirectory of itself, "
        + "'./dirA/dirD/dirE'", copy.getErrorMessage());
  }

  @Test
  public void testMoveToSameLocation() {
    String fileName = "fileOne";
    file = "dirA";
    newLoc = "./dirA";
    copy.setArgs(new String[] {file, newLoc});
    copy.executeCommand();

    // Check all directories
    Assert.assertTrue("The Directory 'dirA' is not in the root Directory",
        Directory.doesDirectoryExist(file, fileSystem));
    Assert.assertTrue("The File '" + fileName + "' does not exist in dirA",
        File.doesFileExist(newLoc + "/" + fileName, fileSystem));
  }
}
