package test;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.Concatenate;
import commands.Curl;
import directoryFileFoldersPackage.FileSystem;
import mocks.MockFileSystem;
import mocks.MockURLConnection;

public class CurlTest {

  private FileSystem fileSystem;
  private Curl curl;
  private Concatenate cat;

  @Before
  public void setup() {
    fileSystem = new MockFileSystem();
    curl = new Curl();
    curl.setFileSystem(fileSystem);
    cat = new Concatenate();
    cat.setFileSystem(fileSystem);
    // Set current Directory to root Directory
    fileSystem.setCurrentDirectory(fileSystem.getRootDirectory());
  }

  @Test
  public void testNoConnection() {
    curl.setArgs(new String[] {"http://www.ub.edu/gilcub/SIMPLE/simple.html"});
    curl.executeCommand();
    cat.setArgs(new String[] {"simple.html"});
    cat.executeCommand();
    Assert.assertEquals("", cat.getOutput());
  }

  @Test
  public void testSampleSite() throws MalformedURLException {
    curl.setArgs(new String[] {"http://www.samplesite.com/simple.txt"});
    curl.setUrlConnection(
        new MockURLConnection(new URL("http://www.samplesite.com/simple.txt")));
    curl.executeCommand();
    cat.setArgs(new String[] {"simple.txt"});
    cat.executeCommand();
    Assert.assertEquals("This is line one\nThis is line two.", cat.getOutput());
  }

  @Test
  public void testEmptyStringSite() throws MalformedURLException {
    curl.setArgs(new String[] {"http://www.samplesite.com/simple2.html"});
    curl.setUrlConnection(new MockURLConnection(
        new URL("http://www.samplesite.com/simple2.html")));
    curl.executeCommand();
    cat.setArgs(new String[] {"simple2.html"});
    cat.executeCommand();
    Assert.assertEquals("", cat.getOutput());
  }

  @Test
  public void testInvalidType() throws MalformedURLException {
    curl.setArgs(new String[] {"http://www.samplesite.com/simple3.exe"});
    curl.setUrlConnection(new MockURLConnection(
        new URL("http://www.samplesite.com/simple3.exe")));
    curl.executeCommand();
    Assert.assertEquals("", curl.getOutput());
    Assert.assertEquals("simple3.exe: Invalid file type. Must be .html or .txt",
        curl.getErrorMessage());
    cat.setArgs(new String[] {"simple3.exe"});
    cat.executeCommand();
    Assert.assertEquals("", cat.getOutput());
    Assert.assertEquals("simple3.exe: No such file or directory",
        cat.getErrorMessage());
  }

}
