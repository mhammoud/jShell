package writer;

import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.File;
import directoryFileFoldersPackage.FileSystem;
import exceptions.FileNotFoundException;
import exceptions.InvalidNameException;

public class FileWriter implements Writer {

  private String toWrite;
  private boolean append;
  private String whereToWrite;
  private FileSystem fileSystem;
  private String errorMessage;

  public FileWriter(String toWrite, String whereToWrite, boolean append,
      FileSystem fileSystem) {
    this.toWrite = toWrite;
    this.append = append;
    this.whereToWrite = whereToWrite;
    this.fileSystem = fileSystem;
    this.errorMessage = "";
  }

  public FileWriter(String whereToWrite, boolean append,
      FileSystem fileSystem) {

    this.toWrite = "";
    this.append = append;
    this.whereToWrite = whereToWrite;
    this.fileSystem = fileSystem;
    this.errorMessage = "";
  }

  @Override
  public void write() {
    if (!toWrite.equals("")) {
      File fileToWriteTo;
      String fileName =
          Directory.seperateInnerMostAbstractFileFromPath(whereToWrite)[1];
      // Check the fileName is valid
      if (InvalidNameException.isNameInvalid(fileName)) {
        errorMessage = new InvalidNameException(whereToWrite).getMessage();
        System.out.println(errorMessage);
      } else {
        try {
          Directory directoryOfFile = Directory.getDirectoryFromPath(
              Directory
                  .seperateInnerMostAbstractFileFromPath(whereToWrite)[0],
              fileSystem);
          if (!File.doesFileExist(whereToWrite, fileSystem)) {
            fileToWriteTo = new File(fileName, directoryOfFile);
          } else {
            fileToWriteTo = File.getFileFromPath(whereToWrite, fileSystem);
          }
          if (append) {
            fileToWriteTo.append(toWrite);
          } else {
            fileToWriteTo.write(toWrite);
          }
        } catch (FileNotFoundException fnfe) {
          errorMessage = fnfe.getMessage();
          System.out.println(errorMessage);
        } catch (InvalidNameException ine) {
          errorMessage = ine.getMessage();
          System.out.println(errorMessage);;
        }
      }
    }

  }

  /**
   * @return the fileSystem
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * @param fileSystem the fileSystem to set
   */
  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * @return the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param errorMessage the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * @return the toWrite
   */
  public String getToWrite() {
    return toWrite;
  }

  /**
   * @param toWrite the String to write
   */
  public void setToWrite(String toWrite) {
    this.toWrite = toWrite;
  }

  /**
   * @return the append
   */
  public boolean isAppend() {
    return append;
  }

  /**
   * @param append the append to set
   */
  public void setAppend(boolean append) {
    this.append = append;
  }

  /**
   * @return the whereToWrite
   */
  public String getWhereToWrite() {
    return whereToWrite;
  }

  /**
   * @param whereToWrite the whereToWrite to set
   */
  public void setWhereToWrite(String whereToWrite) {
    this.whereToWrite = whereToWrite;
  }

}
