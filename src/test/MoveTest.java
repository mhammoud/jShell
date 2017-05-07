package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import commands.Move;
import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.File;
import directoryFileFoldersPackage.FileSystem;
import exceptions.FileNotFoundException;
import mocks.MockFileSystem;

public class MoveTest {

  private FileSystem fileSystem = new MockFileSystem();
  private Move move;
  private String file;
  private String newLoc;

  /**
   * MockFileSystem is set as: root has: dirA, dirB, dirC dirA has: dirD,
   * fileOne, fileTwo, fileThree dirD has: dirE, fileTwo, fileFour
   */
  @Before
  public void setUp() {
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
    move = new Move();
    move.setFileSystem(fileSystem);
  }

  /**
   * Moving fileOne to dirC given a directory as new location
   */
  @Test
  public void testMoveFileGivenNewDir() {
    String fileOne = "fileOne";
    test("./dirA/fileOne", "./dirC");

    // Check all directories
    assertFalse("The File 'fileOne' is still in dirA",
        File.doesFileExist(file, fileSystem));
    assertTrue("The File 'fileOne' is not in dirC",
        File.doesFileExist(newLoc + "/" + fileOne, fileSystem));
  }

  /**
   * Moving fileOne to dirC given the new path it will be at
   */
  @Test
  public void testMoveFileGivenNewPath() {
    test("./dirA/fileOne", "./dirC/fileOne");

    // Check all directories
    assertFalse("The File 'fileOne' is still in dirA",
        File.doesFileExist(file, fileSystem));
    assertTrue("The File 'fileOne' is not in dirC",
        File.doesFileExist(newLoc, fileSystem));
  }

  /**
   * Moving dirC to dirA given a directory as new location
   */
  @Test
  public void testMoveDirGivenDir() {
    test("./dirC", "./dirA");

    // Check all directories
    assertFalse("The Directory 'dirC' is still in the root Directory",
        Directory.doesDirectoryExist(file, fileSystem));
    assertTrue("The Directory 'dirA' does not exist",
        Directory.doesDirectoryExist(newLoc, fileSystem));
  }

  /**
   * Moving dirC to dirA given the new path it will be at
   */
  @Test
  public void testMoveDirGivenNewPath() {
    test("./dirC", "./dirA/dirC");

    // Check all directories
    assertFalse("The Directory 'dirC' is still in the root Directory",
        Directory.doesDirectoryExist(file, fileSystem));
    assertTrue("The Directory 'dirA' does not exist",
        Directory.doesDirectoryExist(newLoc, fileSystem));
  }

  /**
   * Moving fileOne in dirB to dirC, throws FileNotFoundException and does not
   * move anything
   */
  @Test
  public void testInvalidFile() {
    test("./dirB/fileOne", "./dirC");

    // Check all directories
    assertEquals(move.getErrorMessage(),
        "./dirB/fileOne: No such file or directory");
  }

  /**
   * Moving dirF to dirC, throws FileNotFOundException and does not move
   * anything
   */
  @Test
  public void testInvalidDir() {
    test("./dirF", "./dirC");
    assertEquals(move.getErrorMessage(), "./dirF: No such file or directory");
  }

  /**
   * Moving fileTwo in dirA to dirD, replaces the fileTwo in dirD with the one
   * in dirA
   * 
   * @throws FileNotFoundException If File couldn't be found
   */
  @Test
  public void testSameFileName() throws FileNotFoundException {
    String expectedContents, actualContents, fileName = "fileTwo";
    test("./dirA/" + fileName, "./dirA/dirD/" + fileName);

    expectedContents = "fileTwo from A";
    actualContents = File.getFileFromPath(newLoc, fileSystem).getContents();
    assertEquals(
        "Expected the contents of /dirA/dirD/fileTwo to be: '"
            + expectedContents + "' but received " + actualContents,
        expectedContents, actualContents);
    assertFalse("The File '" + fileName + "' still exists in 'dirA'",
        File.doesFileExist(file, fileSystem));
  }

  /**
   * Moving fileOne back into dirA, should not move any files around
   */
  @Test
  public void testMoveToSameDir() {
    test("./dirA/fileOne", "./dirA");
    assertTrue("The File 'fileOne' is not in dirA",
        File.doesFileExist(file, fileSystem));
  }

  /**
   * Moving dirA into dirE, throws MovingIntoSubdirectoryException and should
   * not move any files
   */
  @Test
  public void testMovingParentDir() {
    test("./dirA", "./dirA/dirD/dirE");
    assertEquals("Cannot move './dirA' to a subdirectory of itself, "
        + "'./dirA/dirD/dirE'", move.getErrorMessage());
  }

  /**
   * Performs the move execution to be tested
   * 
   * @param file - the file to be moved
   * @param newLoc - the new location which the file will reside
   */
  private void test(String file, String newLoc) {
    this.file = file;
    this.newLoc = newLoc;
    move.setArgs(new String[] {file, newLoc});
    move.executeCommand();
  }
}
