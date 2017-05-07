package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import directoryFileFoldersPackage.AbstractFile;
import directoryFileFoldersPackage.FileSystem;
import exceptions.FileNotFoundException;
import mocks.MockFileSystem;

public class AbstractFileTest {
  private static FileSystem fileSystem;
  private static final String DIR_A = "dirA";
  private static final String DIR_B = "dirB";
  private static final String DIR_C = "dirC";
  private static final String DIR_D = "dirD";
  private static final String DIR_E = "dirE";
  private static final String FILE_ONE = "fileOne";

  @BeforeClass
  public static void beforeClassSetup() {
    fileSystem = new MockFileSystem();
  }

  @Before
  public void setup() {
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void testGetDirectoryFromRootPath() throws FileNotFoundException {
    Assert.assertEquals(fileSystem.getRootDirectory(),
        AbstractFile.getAbstractFileFromPath("/", fileSystem));
  }

  @Test
  public void testGetDirectoryFromShortAbsolutePath()
      throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_B, AbstractFile
        .getAbstractFileFromPath("/" + DIR_B, fileSystem).toString());
  }

  @Test
  public void testGetDirectoryFromLongAbsolutePath()
      throws FileNotFoundException {
    Assert
        .assertEquals("/" + DIR_A + "/" + DIR_D + "/" + DIR_E,
            AbstractFile
                .getAbstractFileFromPath(
                    "/" + DIR_A + "/" + DIR_D + "/" + DIR_E, fileSystem)
                .toString());
  }

  @Test
  public void testGetDirectoryFromShortRelativePath()
      throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_C,
        AbstractFile.getAbstractFileFromPath(DIR_C, fileSystem).toString());
  }

  @Test
  public void testGetDirectoryFromLongRelativePath()
      throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_A + "/" + DIR_D + "/" + DIR_E, AbstractFile
        .getAbstractFileFromPath(DIR_A + "/" + DIR_D + "/" + DIR_E, fileSystem)
        .toString());
  }

  @Test
  public void testGetDirectoryFromLongRelativePathWithTwoDot()
      throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_A + "/" + DIR_D + "/" + DIR_E,
        AbstractFile.getAbstractFileFromPath(
            DIR_A + "/../" + DIR_A + "/" + DIR_D + "/../" + DIR_D + "/" + DIR_E,
            fileSystem).toString());
  }

  @Test
  public void testGetDirectoryFromLongAbsolutePathWithDot()
      throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_A + "/" + DIR_D + "/" + DIR_E,
        AbstractFile
            .getAbstractFileFromPath(
                "/" + DIR_A + "/./" + DIR_D + "/./" + DIR_E, fileSystem)
            .toString());
  }

  @Test
  public void testGetFileFromShortRelativePath() throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_A + "/" + FILE_ONE,
        AbstractFile
            .getAbstractFileFromPath("/" + DIR_A + "/" + FILE_ONE, fileSystem)
            .toString());
  }

  @Test
  public void testGetFileFromShortAbsolutePath() throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_A + "/" + FILE_ONE,
        AbstractFile
            .getAbstractFileFromPath("/" + DIR_A + "/" + FILE_ONE, fileSystem)
            .toString());
  }
}
