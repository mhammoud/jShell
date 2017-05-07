package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.ListSegments;
import directoryFileFoldersPackage.FileSystem;
import mocks.MockFileSystem;

public class ListSegmentsTest {
  private ListSegments listSegments;
  private FileSystem fileSystem;

  @Before
  public void setUp() {
    fileSystem = new MockFileSystem();
    listSegments = new ListSegments();
    listSegments.setFileSystem(fileSystem);
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void testListSegmentsCurrentDirectory() {
    listSegments.setArgs(new String[] {});
    listSegments.executeCommand();
    Assert.assertEquals("dirA\ndirB\ndirC", listSegments.getOutput());
  }

  @Test
  public void testListSegmentsDirectoryRelativePath() {
    listSegments.setArgs(new String[] {"dirA"});
    listSegments.executeCommand();
    Assert.assertEquals("dirA:dirD fileOne fileThree fileTwo",
        listSegments.getOutput());
  }

  @Test
  public void testListSegmentsDirectoryFullPath() {
    listSegments.setArgs(new String[] {"/dirA"});
    listSegments.executeCommand();
    Assert.assertEquals("/dirA:dirD fileOne fileThree fileTwo",
        listSegments.getOutput());
  }

  @Test
  public void testListSegmentsInvalidDirectory() {
    listSegments.setArgs(new String[] {"/d"});
    listSegments.executeCommand();
    Assert.assertEquals("", listSegments.getOutput());
  }

  @Test
  public void testListSegmentsRecursiveAllDirectories() {
    listSegments.setArgs(new String[] {"-R"});
    listSegments.executeCommand();
    String expected =
        "/: dirA dirB dirC " + "\n" + "/dirA: dirD fileOne fileThree fileTwo "
            + "\n" + "/dirA/dirD: dirE fileFour fileTwo " + "\n"
            + "/dirA/dirD/dirE: " + "\n" + "/dirB: " + "\n" + "/dirC: ";
    Assert.assertEquals(expected, listSegments.getOutput());
  }

  @Test
  public void testListSegmentsRecursiveDirectory() {
    listSegments.setArgs(new String[] {"-R", "dirA"});
    listSegments.executeCommand();
    String expected = "/dirA: dirD fileOne fileThree fileTwo " + "\n"
        + "/dirA/dirD: dirE fileFour fileTwo " + "\n" + "/dirA/dirD/dirE: ";
    Assert.assertEquals(expected, listSegments.getOutput());
  }

  @Test
  public void testListSegmentsRecursiveWithFile() {
    listSegments
        .setArgs(new String[] {"-R", "dirA/dirD", "dirA/dirD/fileFour"});
    listSegments.executeCommand();
    String expected = "/dirA/dirD: dirE fileFour fileTwo \n"
        + "/dirA/dirD/dirE: \n" + "dirA/dirD/fileFour";
    Assert.assertEquals(expected, listSegments.getOutput());
  }

  @Test
  public void testListSegmentRecursiveCaseSensitive() {
    listSegments.setArgs(new String[] {"-r"});
    listSegments.executeCommand();
    String expected =
        "/: dirA dirB dirC " + "\n" + "/dirA: dirD fileOne fileThree fileTwo "
            + "\n" + "/dirA/dirD: dirE fileFour fileTwo " + "\n"
            + "/dirA/dirD/dirE: " + "\n" + "/dirB: " + "\n" + "/dirC: ";
    Assert.assertEquals(expected, listSegments.getOutput());
  }
}
