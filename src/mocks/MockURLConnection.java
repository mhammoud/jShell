package mocks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * This class implements the URLConnection interface and is a mock URL
 * Connection, used for unit testing the Curl command.
 * 
 * @author bzhan
 *
 */
public class MockURLConnection extends URLConnection {

  private InputStream inputStream;

  /**
   * Class constructor that sets inputStream to null.
   * 
   * @param url The URL to set to
   */
  public MockURLConnection(URL url) {
    super(url);
    this.inputStream = null;
  }

  @Override
  public void connect() throws IOException {
    if (getURL().equals(new URL("http://www.samplesite.com/simple.txt"))) {
      // The mock "raw html" of the URL
      String rawText = "This is line one\n" + "This is line two.";
      // Create input stream from String and encode as UTF-8
      inputStream =
          new ByteArrayInputStream(rawText.getBytes(StandardCharsets.UTF_8));
    }
    if (getURL().equals(new URL("http://www.samplesite.com/simple2.html"))) {
      // The mock "raw html" of the URL
      String rawText = "";
      // Create input stream from String and encode as UTF-8
      inputStream =
          new ByteArrayInputStream(rawText.getBytes(StandardCharsets.UTF_8));
    }
    if (getURL().equals(new URL("http://www.samplesite.com/simple3.exe"))) {
      // The mock "raw html" of the URL
      String rawText = "Invalid type";
      // Create input stream from String and encode as UTF-8
      inputStream =
          new ByteArrayInputStream(rawText.getBytes(StandardCharsets.UTF_8));
    }
  }

  /**
   * @return the inputStream
   */
  public InputStream getInputStream() {
    return inputStream;
  }

  /**
   * @param inputStream the inputStream to set
   */
  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }
}
