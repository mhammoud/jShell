package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.Concatenate;
import directoryFileFoldersPackage.FileSystem;
import mocks.MockFileSystem;

public class ConcatenateTest {

  private FileSystem fileSystem;
  private Concatenate concatenate;

  @Before
  public void setup() {
    fileSystem = new MockFileSystem();
    concatenate = new Concatenate();
    concatenate.setFileSystem(fileSystem);
    // Set current Directory to root Directory
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void concatenateSingleBlankFile() {
    concatenate.setArgs(new String[] {"dirA/fileThree"});
    concatenate.executeCommand();
    Assert.assertEquals("", concatenate.getOutput());
  }

  @Test
  public void concatenateSingleFile() {
    concatenate.setArgs(new String[] {"dirA/fileOne"});
    concatenate.executeCommand();
    Assert.assertEquals("contents of fileOne", concatenate.getOutput());
  }

  @Test
  public void concatenateSingleFileMultipleLines() {
    concatenate.setArgs(new String[] {"dirA/dirD/fileFour"});
    concatenate.executeCommand();
    Assert.assertEquals("Line one.\ncontents of fileFour",
        concatenate.getOutput());
  }

  @Test
  public void concatenateMultipleFiles() {
    concatenate.setArgs(new String[] {"dirA/fileOne", "dirA/dirD/fileFour"});
    concatenate.executeCommand();
    Assert.assertEquals("contents of fileOne" + "\n\n\n" + "Line one." + "\n"
        + "contents of fileFour", concatenate.getOutput());
  }

  @Test
  public void concatenateMultipleFilesWithInvalidFile() {
    concatenate.setArgs(
        new String[] {"dirA/fileOne", "invalidFile", "dirA/dirD/fileFour"});
    concatenate.executeCommand();
    Assert.assertEquals("contents of fileOne" + "\n\n\n" + "Line one." + "\n"
        + "contents of fileFour", concatenate.getOutput());
    Assert.assertEquals("invalidFile: No such file or directory",
        concatenate.getErrorMessage());
  }
}
