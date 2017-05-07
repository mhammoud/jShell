package test;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.Concatenate;
import directoryFileFoldersPackage.File;
import directoryFileFoldersPackage.FileSystem;
import mocks.MockFileSystem;
import writer.FileWriter;

public class FileWriterTest {
  private FileSystem fileSystem;
  private FileWriter fileWriter;
  private Concatenate cat;

  @Before
  public void setup() {
    fileSystem = new MockFileSystem();
    cat = new Concatenate();
    cat.setFileSystem(fileSystem);
    // Set current Directory to root Directory
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void testWriteNewFile() {
    fileWriter = new FileWriter("abc\ndef", "dirA/test.txt", false, fileSystem);
    fileWriter.write();
    cat.setArgs(new String[] {"dirA/test.txt"});
    cat.executeCommand();
    Assert.assertEquals("abc\ndef", cat.getOutput());
  }

  @Test
  public void testAppendNewFile() {
    fileWriter = new FileWriter("abc\ndef", "dirA/test.txt", true, fileSystem);
    fileWriter.write();
    cat.setArgs(new String[] {"dirA/test.txt"});
    cat.executeCommand();
    Assert.assertEquals("abc\ndef", cat.getOutput());
  }

  @Test
  public void testAppendExistingFile() {
    fileWriter = new FileWriter("abc\ndef", "dirA/fileOne", true, fileSystem);
    fileWriter.write();
    cat.setArgs(new String[] {"dirA/fileOne"});
    cat.executeCommand();
    Assert.assertEquals("contents of fileOne" + "\n" + "abc\ndef",
        cat.getOutput());
  }

  @Test
  public void testWriteEmptyString() {
    fileWriter = new FileWriter("", "dirA/empty.txt", false, fileSystem);
    fileWriter.write();
    Assert.assertFalse(File.doesFileExist("dirA/empty.txt", fileSystem));
  }
}
