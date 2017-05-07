package test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import directoryFileFoldersPackage.File;
import exceptions.InvalidNameException;

public class FileTest {
  private File file;
  private String expected;
  private String actual;

  @Before
  public void setUp() throws InvalidNameException {
    file = new File("file");
  }

  @Test
  public void testAppendToFile() {
    String content = "This is new stuff to add to the file";
    file.append(content);

    content = "This is more new stuff to add to the file";
    file.append(content);

    expected = "This is new stuff to add to the file\nThis is more new"
        + " stuff to add to the file";
    actual = file.getContents();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testWriteToFile() {
    file.setContents("This is some random content to be overwritten");

    String content = "This is new stuff to overwrite the file";
    file.write(content);

    expected = "This is new stuff to overwrite the file";
    actual = file.getContents();
    Assert.assertEquals(expected, actual);
  }

}
