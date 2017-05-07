package mocks;

import java.util.ArrayList;

import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.File;
import directoryFileFoldersPackage.FileSystem;
import exceptions.InvalidNameException;
import utils.DirectoryStack;

public class MockFileSystem implements FileSystem {

  private Directory rootDirectory;
  private Directory currentDirectory;
  private DirectoryStack savedDirectories;
  private ArrayList<String> inputLog;

  public MockFileSystem() {

    this.rootDirectory = Directory.createEmptyRootDirectory();
    this.currentDirectory = rootDirectory;
    this.savedDirectories = new DirectoryStack();
    this.inputLog = new ArrayList<String>();

    try {
      /*
       * Creates three directories dirA, dirB, and dirC in the rootDirectory.
       * Creates a directory dirD in directory dirA.
       */
      Directory directoryA =
          Directory.createSubDirectory("dirA", rootDirectory);
      Directory.createSubDirectory("dirB", rootDirectory);
      Directory.createSubDirectory("dirC", rootDirectory);
      Directory directoryD = Directory.createSubDirectory("dirD", directoryA);
      Directory.createSubDirectory("dirE", directoryD);

      File fileOne = new File("fileOne", directoryA);
      fileOne.setContents("contents of fileOne");
      File fileTwoA = new File("fileTwo", directoryA);
      fileTwoA.setContents("fileTwo from A");
      File fileTwoD = new File("fileTwo", directoryD);
      fileTwoD.setContents("fileTwo from D");
      new File("fileThree", directoryA);
      File fileFour = new File("fileFour", directoryD);
      fileFour.setContents("Line one.\ncontents of fileFour");
    } catch (InvalidNameException ine) {
      System.out.println(ine.getMessage());
    }
  }

  public Directory getCurrentDirectory() {
    return currentDirectory;
  }

  public void setCurrentDirectory(Directory currentDirectory) {
    this.currentDirectory = currentDirectory;
  }

  public Directory getRootDirectory() {
    return rootDirectory;
  }

  public DirectoryStack getSavedDirectoryStack() {
    return this.savedDirectories;
  }

  public ArrayList<String> getInputLog() {
    return inputLog;
  }

  public void setInputLog(ArrayList<String> inputLog) {
    this.inputLog = inputLog;
  }
}
