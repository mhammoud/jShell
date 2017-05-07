package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.PopDirectory;
import commands.PushDirectory;
import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.FileSystem;
import exceptions.FileNotFoundException;
import mocks.MockFileSystem;

public class PushDirectoryTest {

  private FileSystem fileSystem;
  private PushDirectory pushDirectory;

  @Before
  public void setup() {
    fileSystem = new MockFileSystem();
    pushDirectory = new PushDirectory();
    pushDirectory.setFileSystem(fileSystem);
    // Set current Directory to root Directory
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void testPushDirectoryRelativePath() {
    pushDirectory.setArgs(new String[] {"/dirA"});
    pushDirectory.executeCommand();
    Assert.assertEquals("/dirA", fileSystem.getCurrentDirectory().toString());
  }

  @Test
  public void testPushDirectoryAbsolutePath() throws FileNotFoundException {
    fileSystem.setCurrentDirectory(
        Directory.getDirectoryFromPath("/dirA/dirD", fileSystem));
    pushDirectory.setArgs(new String[] {"/dirA/dirD"});
    pushDirectory.executeCommand();
    Assert.assertEquals("/dirA/dirD",
        fileSystem.getCurrentDirectory().toString());
  }

  @Test
  public void testPushDirectoryWithPopDirectory() throws FileNotFoundException {
    fileSystem.setCurrentDirectory(
        Directory.getDirectoryFromPath("/dirA", fileSystem));
    pushDirectory.setArgs(new String[] {"/dirA/dirD"});
    pushDirectory.executeCommand();
    Assert.assertEquals("/dirA/dirD",
        fileSystem.getCurrentDirectory().toString());

    PopDirectory popDirectory = new PopDirectory();
    popDirectory.setFileSystem(fileSystem);
    popDirectory.setArgs(new String[] {});
    popDirectory.executeCommand();
    Assert.assertEquals("/dirA", fileSystem.getCurrentDirectory().toString());
  }
}
