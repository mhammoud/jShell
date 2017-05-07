package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.PresentWorkingDirectory;
import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.FileSystem;
import exceptions.FileNotFoundException;
import mocks.MockFileSystem;

public class PresentWorkingDirectoryTest {
  private FileSystem fileSystem;
  private PresentWorkingDirectory presentWorkingDirectory;

  @Before
  public void setup() {
    fileSystem = new MockFileSystem();
    presentWorkingDirectory = new PresentWorkingDirectory();
    presentWorkingDirectory.setFileSystem(fileSystem);
    // Set current Directory to root Directory
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void testPresentWorkingDirectoryRootPath() {
    presentWorkingDirectory.setArgs(new String[] {""});
    presentWorkingDirectory.executeCommand();
    Assert.assertEquals("/", presentWorkingDirectory.getOutput());
  }

  @Test
  public void testPresentWorkingDirectoryNonRootPath()
      throws FileNotFoundException {
    fileSystem.setCurrentDirectory(
        Directory.getDirectoryFromPath("/dirA/dirD", fileSystem));
    presentWorkingDirectory.setArgs(new String[] {""});
    presentWorkingDirectory.executeCommand();
    Assert.assertEquals("/dirA/dirD", presentWorkingDirectory.getOutput());
  }
}
