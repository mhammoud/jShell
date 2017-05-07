package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.ListSegments;
import commands.MakeDirectory;
import directoryFileFoldersPackage.FileSystem;
import mocks.MockFileSystem;

public class MakeDirectoryTest {

  /**
   * The MakeDirectory command we will be executing
   */
  private MakeDirectory makeDirectory;
  /**
   * The FileSystem we will be running the Command on
   */
  private FileSystem fileSystem;
  /**
   * The ListSegments command we will be executing to ensure the directory was
   * actually created
   */
  private ListSegments listSegments;

  @Before
  public void setUp() {
    fileSystem = new MockFileSystem();
    makeDirectory = new MakeDirectory();
    makeDirectory.setFileSystem(fileSystem);
    listSegments = new ListSegments();
    listSegments.setFileSystem(fileSystem);
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void testMakeDirectoryRelativePathInRootDirectory() {
    makeDirectory.setArgs(new String[] {"one"});
    makeDirectory.executeCommand();
    Assert.assertEquals("", makeDirectory.getOutput());
    listSegments.setArgs(new String[] {});
    listSegments.executeCommand();
    Assert.assertEquals("dirA\n" + "dirB\n" + "dirC\n" + "one",
        listSegments.getOutput());
  }

  @Test
  public void testMakeDirectoryAbsolutePath() {
    makeDirectory.setArgs(new String[] {"/dirA/dirD/dirE/dirN"});
    makeDirectory.executeCommand();
    Assert.assertEquals("", makeDirectory.getOutput());
    listSegments.setArgs(new String[] {"dirA/dirD/dirE"});
    listSegments.executeCommand();
    Assert.assertEquals("dirA/dirD/dirE:dirN", listSegments.getOutput());
  }

  @Test
  public void testMakeDirectoryMultipleDirectories() {
    makeDirectory.setArgs(new String[] {"dirOne", "dirTwo", "dirThree"});
    makeDirectory.executeCommand();
    Assert.assertEquals("", makeDirectory.getOutput());
    listSegments.setArgs(new String[] {});
    listSegments.executeCommand();
    Assert.assertEquals(
        "dirA\n" + "dirB\n" + "dirC\n" + "dirOne\n" + "dirThree\n" + "dirTwo",
        listSegments.getOutput());
  }

  @Test
  public void testMakeDirectoryExistingDirectory() {
    makeDirectory.setArgs(new String[] {"dirA"});
    makeDirectory.executeCommand();
    Assert.assertEquals("", makeDirectory.getOutput());
    listSegments.setArgs(new String[] {});
    listSegments.executeCommand();
    Assert.assertEquals("dirA\n" + "dirB\n" + "dirC", listSegments.getOutput());
  }

  @Test
  public void testMakeDirectoryRelativePath() {
    makeDirectory.setArgs(new String[] {"dirA/dirE"});
    makeDirectory.executeCommand();
    Assert.assertEquals("", makeDirectory.getOutput());
    listSegments.setArgs(new String[] {"dirA"});
    listSegments.executeCommand();
    Assert.assertEquals("dirA:dirD dirE fileOne fileThree fileTwo",
        listSegments.getOutput());
  }

  @Test
  public void testMakeDirectoryInvalidName() {
    makeDirectory.setArgs(new String[] {"/\\"});
    makeDirectory.executeCommand();
    Assert.assertEquals("", makeDirectory.getOutput());
    listSegments.setArgs(new String[] {});
    listSegments.executeCommand();
    Assert.assertEquals("dirA\n" + "dirB\n" + "dirC", listSegments.getOutput());
  }

  @Test
  public void testMakeDirectoryWhenDirectoryDoesntExist() {
    makeDirectory.setArgs(new String[] {"/dirF/dirM"});
    makeDirectory.executeCommand();
    Assert.assertEquals("", makeDirectory.getOutput());
    listSegments.setArgs(new String[] {});
    listSegments.executeCommand();
    Assert.assertEquals("dirA\n" + "dirB\n" + "dirC", listSegments.getOutput());
  }

  @Test
  public void testMakeDirectoryMultipleDirectoriesWithError() {
    makeDirectory
        .setArgs(new String[] {"dirOne", "dirInvalid/dirThree", "dirTwo"});
    makeDirectory.executeCommand();
    Assert.assertEquals("", makeDirectory.getOutput());
    listSegments.setArgs(new String[] {});
    listSegments.executeCommand();
    Assert.assertEquals("dirA\n" + "dirB\n" + "dirC\n" + "dirOne\n" + "dirTwo",
        listSegments.getOutput());
  }
}
