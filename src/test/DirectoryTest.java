package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.FileSystem;
import exceptions.FileNotFoundException;
import mocks.MockFileSystem;

public class DirectoryTest {

  private static FileSystem fileSystem;
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
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void testGetDirectoryFromRootPath() throws FileNotFoundException {
    Assert.assertEquals(fileSystem.getRootDirectory(),
        Directory.getDirectoryFromPath("/", fileSystem));
  }

  @Test
  public void testGetDirectoryFromShortAbsolutePath()
      throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_B,
        Directory.getDirectoryFromPath("/" + DIR_B, fileSystem).toString());
  }

  @Test
  public void testGetDirectoryFromLongAbsolutePath()
      throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_A + "/" + DIR_D + "/" + DIR_E,
        Directory.getDirectoryFromPath("/" + DIR_A + "/" + DIR_D + "/" + DIR_E,
            fileSystem).toString());
  }

  @Test
  public void testGetDirectoryFromShortRelativePath()
      throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_C,
        Directory.getDirectoryFromPath(DIR_C, fileSystem).toString());
  }

  @Test
  public void testGetDirectoryFromLongRelativePath()
      throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_A + "/" + DIR_D + "/" + DIR_E,
        Directory
            .getDirectoryFromPath(DIR_A + "/" + DIR_D + "/" + DIR_E, fileSystem)
            .toString());
  }

  @Test
  public void testGetDirectoryFromLongRelativePathWithTwoDot()
      throws FileNotFoundException {
    Assert.assertEquals("/" + DIR_A + "/" + DIR_D + "/" + DIR_E,
        Directory.getDirectoryFromPath(
            DIR_A + "/../" + DIR_A + "/" + DIR_D + "/../" + DIR_D + "/" + DIR_E,
            fileSystem).toString());
  }

  @Test
  public void testGetDirectoryFromLongAbsolutePathWithDot()
      throws FileNotFoundException {
    Assert
        .assertEquals("/" + DIR_A + "/" + DIR_D + "/" + DIR_E,
            Directory
                .getDirectoryFromPath(
                    "/" + DIR_A + "/./" + DIR_D + "/./" + DIR_E, fileSystem)
                .toString());
  }

}
