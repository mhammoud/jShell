package commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.File;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.FileNotFoundException;
import writer.ConsoleWriter;

/**
 * <p>
 * This class contains the variables and methods to perform the "ls" command.
 * This command prints the contents (file and directories) of the current
 * directory if no paths are given as arguments.
 * 
 * Otherwise, for each path p given as an argument, either:
 * <ul>
 * <li>If p specifies a file, print p</li>
 * <li>If p specifies a directory, print p, a colon and the the contents of that
 * directory, then a new line</li>
 * <li>If p does not exist, print a suitable error message.</li>
 * <li>If -R is provided as an argument, all sub-directories are printed.
 * 
 * </ul>
 * 
 * @author Brendan Zhang
 * 
 */
public class ListSegments extends Command {

  /**
   * A FileSystem that the ListSegments command will be using
   */
  private FileSystem fileSystem;

  public ListSegments() {
    setArgs(null);
    setOutput("");
    setFileSystem(JFileSystem.getFileSystem());
    setWriter(new ConsoleWriter());
  }

  public ListSegments(String[] args) {
    setArgs(args);
    setOutput("");
    setWriter(new ConsoleWriter());
  }

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the ListSegments command
   */
  public static String getManMessage() {
    return "LS" + "\t" + "COMMAND" + "\n\n" + "NAME" + "\n\t"
        + "ls - list directory/file contents" + "\n\n" + "SYNOPSIS" + "\n\t"
        + "ls [PATH]..." + "\n\n" + "DESCRIPTION" + "\n\t"
        + "Lists information about the specified paths "
        + "(the current directory by default)." + "\n\t"
        + "Sorts the entries alphabetically." + "\n";
  }

  /**
   * Executes the "ls" command
   */
  @Override
  public void executeCommand() {
    // If there is -R or -r provided;
    if ((getArgs().length >= 1) && (getArgs()[0].toUpperCase().equals("-R"))) {
      // If there is only one args (-R), no file paths provided.
      // ls -R is then performed on the current working directory.
      if (getArgs().length == 1) {
        Directory currDir = fileSystem.getCurrentDirectory();
        this.listSegmentRecursiveCurrentDirectory(currDir);
      }

      // If there are multiple args provided (files and folders)
      else {
        ArrayList<String> files = new ArrayList<>();
        // For each args (excluding the -r)
        for (int i = 1; i < getArgs().length; i++) {
          String path = getArgs()[i];
          // If this args is a directory.
          if (Directory.doesDirectoryExist(path, fileSystem)) {
            Directory cd;

            // Check if this path leads to a valid directory, and if it does
            // perform an ls -R on that subdirectory.
            try {
              cd = Directory.getDirectoryFromPath(path, fileSystem);
              this.listSegmentRecursiveCurrentDirectory(cd);
            } catch (FileNotFoundException fnfe) {
              setErrorMessage(fnfe.getMessage());
              System.out.println(getErrorMessage());
            }
          }

          // If this args is a file;
          else {
            // hold onto it for now, since this must be printed at the end
            // of an ls output.
            files.add(getArgs()[i]);
          }
        }

        // Print out valid files.
        for (String pathToFile : files) {
          String currOut = getOutput();
          this.listSegmentFile(pathToFile);
          setOutput(currOut + "\n" + getOutput());
        }
      }
    }

    // If there are no arguments, print the contents of the working directory
    else if (getArgs().length == 0) {
      this.listSegmentCurrentDirectory();
    }

    else {
      // For each path given
      for (String path : getArgs()) {
        // Path is a path to a Directory
        if (Directory.doesDirectoryExist(path, fileSystem)) {
          this.listSegmentDirectory(path);
        } else {
          this.listSegmentFile(path);
        }
      }
    }

    getWriter().setToWrite(getOutput());
    getWriter().write();
  }

  /**
   * Gets the FileSystem.
   * 
   * @return fileSystem
   */
  public FileSystem getFileSystem() {
    return fileSystem;
  }

  /**
   * Sets the FileSystem.
   * 
   * @param fileSystem FileSystem to set to
   */
  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * Prints the path to the File. If the File doesn't exist or path is wrong,
   * create PathError and set output.
   * 
   * @param filePath String that contains the File path
   */
  private void listSegmentFile(String filePath) {
    // Set all file data
    String directoryOfFile =
        Directory.seperateInnerMostAbstractFileFromPath(filePath)[0];
    String fileName =
        Directory.seperateInnerMostAbstractFileFromPath(filePath)[1];

    // Outputs the file path
    try {
      if (Directory.getDirectoryFromPath(directoryOfFile, fileSystem)
          .doesFileExist(fileName)) {
        setOutput(filePath);
      }
    } catch (FileNotFoundException fnfe) {
      // Print error message if file doesn't exist
      setErrorMessage(fnfe.getMessage());
      System.out.println(getErrorMessage());
    }
  }

  /**
   * Sets the output to the directory path and the contents of the directory in
   * alphabetical order. If the directory doesn't exist or path is wrong, create
   * PathError and set output.
   * 
   * @param directoryPath String that contains the the directory path
   */
  private void listSegmentDirectory(String directoryPath) {
    // Checks directory exists, outputs all the files in directory
    Directory directory;
    try {
      directory = Directory.getDirectoryFromPath(directoryPath, fileSystem);
      ArrayList<File> files = directory.getFiles();
      ArrayList<Directory> childFolders = directory.getChildDirectories();
      setOutput(directoryPath + ":" + getContents(files, childFolders));
    } catch (FileNotFoundException fnfe) {
      // Print error message if Directory doesn't exist
      setErrorMessage(fnfe.getMessage());
      System.out.println(getErrorMessage());
    }
  }

  /**
   * Returns the files and directories in a Directory, in alphabetical order.
   * 
   * @param files Files of a Directory
   * @param childDirectories Children Directories of a Directory
   * @return String that represents the contents of a Directory
   */
  private String getContents(ArrayList<File> files,
      ArrayList<Directory> childDirectories) {
    String stringOutput = "";
    ArrayList<String> contents = addContentAndSort(files, childDirectories);
    int lineChars = 0;

    // For each item, outputs it with proper formatting
    for (String item : contents) {
      // go to the next line if passes 80 characters
      if ((lineChars + item.length()) > 80) {
        stringOutput += "\n";
      }
      lineChars += item.length();
      stringOutput += item + " ";
    }
    if (stringOutput.length() > 0) {
      return stringOutput.substring(0, stringOutput.length() - 1);
    } else {
      // Handle empty String. No space to remove.
      return stringOutput;
    }
  }

  /**
   * Sets the output to the Files and child Directories of the current Directory
   * in alphabetical order, with each content on its own line.
   */
  private void listSegmentCurrentDirectory() {
    // Get all file data
    String stringOutput = "";
    List<String> contents =
        addContentAndSort(fileSystem.getCurrentDirectory().getFiles(),
            fileSystem.getCurrentDirectory().getChildDirectories());

    // Go thru each file and add it to output
    for (int i = 0; i < contents.size(); i++) {
      stringOutput += contents.get(i);
      if (i != contents.size() - 1) {
        stringOutput += "\n";
      }
    }

    this.setOutput(stringOutput);
  }

  /**
   * Returns the Files and children Directories, sorted alphabetically.
   * 
   * @param files Files to add and sort
   * @param childDirectories Directories to add and sort
   * @return ArrayList<String> that represents the Files and Directories
   */
  private ArrayList<String> addContentAndSort(ArrayList<File> files,
      ArrayList<Directory> childDirectories) {

    ArrayList<String> contents = new ArrayList<String>();

    // For each file adds it to a list
    for (File file : files) {
      contents.add(file.getName());
    }

    // For each directory adds it to a list
    for (Directory dir : childDirectories) {
      contents.add(dir.getName());
    }

    // Sort the contents
    Collections.sort(contents);
    return contents;
  }

  /**
   * Returns a String output containing the sub-directories from the current
   * directory onwards.
   * 
   * @return
   */
  private String listSegmentRecursiveCurrDirHelper(Directory cur) {

    String op = "";
    ArrayList<Directory> childFolders = cur.getChildDirectories();
    ArrayList<File> files = cur.getFiles();

    Collections.sort(childFolders);
    Collections.sort(files);

    // base case - no more child folders.
    if (childFolders.size() == 0) {
      // get the output of this directory.
      op += cur.toString() + ": ";

      for (File f : files) {
        op += f.getName() + " ";
      }

      op += "\n";
    }

    // otherwise, recursively go through each child folder and ls.
    else {
      // For each folder in childfolders.
      // Set it as output, then check recursively in each folder.
      String directoryPathParent = cur.toString();
      op += directoryPathParent + ": ";

      // returns the name of each child folder into the output.
      for (Directory childFolder : childFolders) {
        op += childFolder.getName() + " ";

        // get all files belonging to this child folder.
        for (File f : files) {
          op += f.getName() + " ";
        }
      }

      op += "\n";

      // proceed to the next childfolder recursively - note
      // this can only happen after the above loop is completed.
      for (Directory childFolder : childFolders) {
        op += this.listSegmentRecursiveCurrDirHelper(childFolder);
      }
    }

    return op;

  }

  /**
   * Recursively returns the ls of the current directory.
   */
  private void listSegmentRecursiveCurrentDirectory(Directory currDir) {

    // Gets the output of the ls -r for the given directory.
    String op = this.listSegmentRecursiveCurrDirHelper(currDir);
    if (op.length() > 0) {
      op = op.substring(0, op.length() - 1);
    }
    setOutput(op);
  }
}
