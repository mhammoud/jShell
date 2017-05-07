package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import directoryFileFoldersPackage.File;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.FileNotFoundException;
import exceptions.InvalidArgumentsException;
import exceptions.InvalidFileTypeException;
import exceptions.InvalidNameException;
import utils.CommandTable;
import writer.ConsoleWriter;

/**
 * This class contains the methods and variables required to execute the Curl
 * Command.
 * <p>
 * The Curl command gets the contents of a file from a URL and adds it as a File
 * into the current working Directory. The added File will have the same name as
 * the File from the URL. If the URL is invalid or the URL can't be connected
 * to, the File is not created.
 * 
 * @author Brendan Zhang
 *
 */
public class Curl extends Command {

  /**
   * The FileSystem to be used by this Curl command
   */
  private FileSystem fileSystem;
  /**
   * The URLConnection to the URL in the arguments of this Curl command.
   */
  private URLConnection urlConnection;

  /**
   * Default class constructor. Sets command arguments to null, output to empty
   * String and the fileSystem to {@link JFileSystem#getFileSystem()}.
   */
  public Curl() {
    this.setArgs(null);
    this.setOutput("");
    this.setFileSystem(JFileSystem.getFileSystem());
    setWriter(new ConsoleWriter());
  }

  /**
   * Executes the curl command.
   */
  @Override
  public void executeCommand() {
    try {
      curl(getArgs());
    } catch (InvalidArgumentsException iae) {
      setErrorMessage(iae.getMessage());
      System.out.println(iae.getMessage());
    }
    getWriter().setToWrite(getOutput());
    getWriter().write();
  }

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the Curl command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "curl - transfer a URL" + "\n\n" + "SYNOPSIS"
        + "\n\t" + "curl URL" + "\n\n" + "DESCRIPTION" + "\n\t"
        + "Gets a file (either .txt or .html) from the given URL and adds it "
        + "to the current working directory." + "\n\n" + "EXAMPLE" + "\n\t"
        + "The following will add file1.txt from "
        + "the given URL to the current directory:" + "\n\t"
        + "curl http://somewebsite.org/file1.txt";
  }

  /**
   * Gets the contents of a file from a URL and adds it as a File into the
   * current working Directory. The added File will have the same name as the
   * File from the URL. If the URL is invalid or the URL can't be connected to,
   * the File is not created.
   * 
   * @param args The arguments for this Curl command
   * @throws InvalidArgumentsException If the number of arguments is not equal
   *         to one
   */
  public void curl(String[] args) throws InvalidArgumentsException {
    if (args.length != 1) {
      // Throw exception if number of arguments is not equal to one
      throw InvalidArgumentsException
          .createInvalidArgumentsException(CommandTable.CURL, args);
    }
    try {
      String toWrite = getStringToWrite();
      try {
        File file = getFileToWriteTo(args[0]);
        file.write(toWrite);
      } catch (InvalidNameException ine) {
        setErrorMessage(ine.getMessage());
        System.out.println(ine.getMessage());
      } catch (InvalidFileTypeException ife) {
        setErrorMessage(ife.getMessage());
        System.out.println(ife.getMessage());
      }
    } catch (MalformedURLException e) {
      // new URL() failed
      // There is something wrong with the URL
      setErrorMessage("The URL was invalid: " + getArgs()[0]);
      System.out.println("The URL was invalid: " + getArgs()[0]);
    } catch (IOException ioe) {
      // openConnection() failed
      // Could not connect to the URL
      setErrorMessage("Could not connect to the URL: " + getArgs()[0]);
      System.out.println("Could not connect to the URL: " + getArgs()[0]);
    }
  }

  /**
   * Gets the String to write to the File to write to for this Curl command,
   * using the command arguments of this Curl command. It gets the String by
   * creating a connection to the URL of the File to get and reading it line by
   * line.
   * 
   * @return String to write to the File
   * @throws MalformedURLException, IOException If the URL is invalid or the URL
   *         can't be connected to.
   */
  private String getStringToWrite() throws MalformedURLException, IOException {

    String toWrite = "";
    // Create new URLConnection from web address String
    if (urlConnection == null) {
      URL myURL = new URL(getArgs()[0]);
      urlConnection = myURL.openConnection();
    }
    urlConnection.connect();
    // Read input and store in toWrite
    BufferedReader in = new BufferedReader(
        new InputStreamReader(urlConnection.getInputStream()));
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      if (toWrite.equals("")) {
        toWrite = inputLine;
      } else {
        toWrite = (toWrite + "\n" + inputLine);
      }
    }
    in.close();

    return toWrite;
  }

  /**
   * Given a String that contains the URL of the html/txt File, returns the File
   * to output to. If the File doesn't exist, create it.
   * 
   * @param url String that contains the URL of the File to write to.
   * @return The File to output to.
   * @throws InvalidNameException If the name of the File to make is invalid.
   * @throws InvalidFileTypeException If the File isn't of .html or .txt.
   */
  private File getFileToWriteTo(String url)
      throws InvalidNameException, InvalidFileTypeException {
    String fileName = url.substring(url.lastIndexOf("/") + 1);

    // Throw exception if File isn't .html or .txt
    if (!fileName.endsWith(".html") && !fileName.endsWith(".txt")) {
      throw new InvalidFileTypeException(fileName);
    }

    try {
      return File.getFileFromPath(
          fileSystem.getCurrentDirectory().toString() + "/" + fileName,
          fileSystem);
    } catch (FileNotFoundException e) {
      // If the file doesn't exist, create it.
      return new File(fileName, fileSystem.getCurrentDirectory());
    }
  }

  /**
   * 
   * @return the FileSystem of this Curl command
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * 
   * @param fileSystem the FileSystem to set to
   */
  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * @return the urlConnection
   */
  public URLConnection getUrlConnection() {
    return urlConnection;
  }

  /**
   * @param urlConnection the urlConnection to set
   */
  public void setUrlConnection(URLConnection urlConnection) {
    this.urlConnection = urlConnection;
  }

}
