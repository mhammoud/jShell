package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.PopDirectory;
import directoryFileFoldersPackage.FileSystem;
import mocks.MockFileSystem;

public class PopDirectoryTest {

  private FileSystem fileSystem;
  private PopDirectory popDirectory;

  @Before
  public void setup() {
    fileSystem = new MockFileSystem();
    fileSystem.getSavedDirectoryStack().push("/dirA");
    fileSystem.getSavedDirectoryStack().push("/dirA/dirD");
    popDirectory = new PopDirectory();
    popDirectory.setFileSystem(fileSystem);
    // Set current Directory to root Directory
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void testPopDirectory() {
    popDirectory.setArgs(new String[] {""});
    popDirectory.executeCommand();
    Assert.assertEquals("/dirA/dirD",
        fileSystem.getCurrentDirectory().toString());
  }

  @Test
  public void testPopDirectoryMultipleTimes() {
    popDirectory.setArgs(new String[] {""});
    popDirectory.executeCommand();
    Assert.assertEquals("/dirA/dirD",
        fileSystem.getCurrentDirectory().toString());
    popDirectory.executeCommand();
    Assert.assertEquals("/dirA", fileSystem.getCurrentDirectory().toString());
  }

  @Test
  public void testPopDirectoryEmptyStack() {
    popDirectory.setArgs(new String[] {""});
    popDirectory.executeCommand();
    popDirectory.executeCommand();
    popDirectory.executeCommand();
    Assert.assertEquals("", popDirectory.getOutput());
  }
}
