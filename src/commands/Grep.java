package commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import directoryFileFoldersPackage.Directory;
import directoryFileFoldersPackage.File;
import directoryFileFoldersPackage.FileSystem;
import directoryFileFoldersPackage.JFileSystem;
import exceptions.FileNotFoundException;
import exceptions.InvalidArgumentsException;
import exceptions.InvalidRegexSyntax;
import utils.CommandTable;
import writer.ConsoleWriter;

/**
 * This class consists of methods and attributes that are required to execute
 * the "grep" command.
 * 
 * The grep command is a command in the form of grep [-R] REGEX PATH ...
 * 
 * If -R is not supplied, the command returns REGEX in PATH, which will always
 * be a file.
 * 
 * If -R is supplied, and PATH is a directory, the PATH is traversed
 * recursively, and for all lines in all files that contain REGEX, a path to the
 * file is returned, along with the line that contained the regex.
 * 
 * @author MohamedHammoud
 *
 */
public class Grep extends Command {

  private FileSystem fileSystem;
  Pattern regex;


  /**
   * Default class constructor that sets the output and printToFile to empty
   * String, sets toWhere and the command arguments to null, and sets fileSystem
   * to {@link JFileSystem#getFileSystem()}.
   * 
   * @param arguments The arguments to set to
   * @param fileSystem The FileSystem to set to
   */
  public Grep() {
    this.setArgs(null);
    this.setFileSystem(JFileSystem.getFileSystem());
    setWriter(new ConsoleWriter());
  }

  /**
   * Sets the file system.
   * 
   * @param fileSystem
   */
  public void setFileSystem(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }
  
  /**
   * Gets the man message for the grep command
   * @return the man message for the grep command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "grep" + "\n\n" + "SYNOPSIS" + "\n\t"
        + "grep [-R] REGEX PATH ..." + "\n\n" + "DESCRIPTION" + "\n\t" 
        + "Print lines contains REGEX in PATH where PATH is a "
        + "file." + "\n\n\t" + "The following options are available:" + "\n\n\t"
        + "-R" + "\n\t\t" + "If PATH is a directory then search recursivly " 
        + "through the directory and" + "\n\t\t" + "print the path to the file "
        + "with the line that contains the REGEX" + "\n\n" + "EXAMPLE" + "\n\t"
        + "The current working directory contains dirA and file1. Move file1 "
        + "to be in dirA." + "\n\t" + "Both ways are accepted:" + "\n\t\t"
        + "mv ./file1 ./dirA" + "\n\t\t" + "mv ./file1 ./dirA/file1\n";
  }
  
  @Override
  public void executeCommand() {

    try {
      this.grep();
    } catch (InvalidRegexSyntax ire) {
      setErrorMessage(ire.getMessage());
      System.out.println(ire.getMessage());
    } catch (InvalidArgumentsException iae) {
      setErrorMessage(iae.getMessage());
      System.out.println(iae.getMessage());
    } catch (FileNotFoundException fnfe) {
      setErrorMessage(fnfe.getMessage());
      System.out.println(fnfe.getMessage());
    }

    // Write output of command.
    getWriter().setToWrite(getOutput());
    getWriter().write();
  }

  /**
   * Recursively goes through subdirectories, examining each file to see if it a
   * line in file matches regex.
   * 
   * @param dir
   * @param regex
   */
  public void grepFileRecursively(Directory dir, Pattern regex) {

    ArrayList<Directory> childFolders = dir.getChildDirectories();

    ArrayList<File> files = dir.getFiles();

    // Base Case: No more files in directory and no child folders.
    if ((childFolders.size() == 0) && (files.size() == 0)) {
      return;
    }

    // We have more directories to search!
    else {
      // For each file in this directory, perform a grep on it.
      for (File file : files) {
        grepFile(file, regex, true);
      }

      // Repeat for every set of files in every childfolder.
      for (Directory childFolder : childFolders) {
        grepFileRecursively(childFolder, regex);
      }
    }
  }


  /**
   * Given a file and a regex statement, a file is checked if it matches a regex
   * string.
   * 
   * @param file
   * @param regex
   */
  public void grepFile(File file, Pattern regex, boolean isRecursive) {

    String[] fileContents = file.getContents().split("\\r?\\n");
    Matcher matchLine;
    String nl = "";

    /*
     * For each line in the file, check if the line has an occurrence (anywhere
     * in the line) that matches the regex. If it does, this line is returned to
     * stdout.
     */
    for (String line : fileContents) {
      matchLine = regex.matcher(line);
      if (matchLine.find()) {
        
        // Add new line after first line.
        if (getOutput().length() > 0) {
          nl = "\n";
        }

        // output differs based on if we're doing a recursive call on files
        // or if we're simply checking a file.
        if (!isRecursive) {
          setOutput(getOutput() + nl + line);
        } else {
          setOutput(getOutput() + nl + file.toString() + ": " + line);
        } 
      }
    }
    // Remove additional \n.
    if (getOutput().length() > 0) {
      setOutput(getOutput().substring(0, getOutput().length()));
    }
  }

  /**
   * Performs grep command on given arguments.
   * 
   * @throws InvalidArgumentsException If the number of arguments is equal to
   *         zero.
   * @throws InvalidRegexSyntax If the regex is invalid.
   * @throws FileNotFoundException If the File doesn't exist.
   */
  public void grep() throws InvalidArgumentsException, FileNotFoundException,
      InvalidRegexSyntax {
    // If no argument is given, this throws an error.
    if (getArgs().length == 0) {
      throw InvalidArgumentsException
          .createInvalidArgumentsException(CommandTable.GREP, getArgs());
    }

    if (getArgs()[0].toUpperCase().equals("-R")) {
      // If -R is supplied: Recurse through directories for grep.
      grepDirectories();
    } else {
      grepFiles();
    }
  }

  /**
   * Performs the grep command for Files (without -R option).
   * 
   * @throws InvalidRegexSyntax If the regex is invalid.
   * @throws FileNotFoundException If the File doesn't exist.
   * @throws InvalidArgumentsException 
   */
  public void grepFiles() throws FileNotFoundException, InvalidRegexSyntax,
  InvalidArgumentsException {
 
    if (getArgs().length < 2) {
      throw InvalidArgumentsException
            .createInvalidArgumentsException(CommandTable.GREP, getArgs());
    }
   
    String[] allDirectories =
        Arrays.copyOfRange(getArgs(), 1, getArgs().length);
   
    String regexAsStr = null;

    // Check if quotations are supplied.
    if ((getArgs()[0].charAt(0) == '"')
        && (getArgs()[0].charAt(getArgs()[0].length() - 1) == '"')) {
      regexAsStr = getArgs()[0].substring(1, getArgs()[0].length() - 1);
    } else {
      throw InvalidRegexSyntax
          .createInvalidRegexSyntaxError("Missing quotations");

    }
    
    // Check if regex is valid.
    try {
      this.regex = Pattern.compile(regexAsStr);
    } catch (PatternSyntaxException pse) {
      throw InvalidRegexSyntax.createInvalidRegexSyntaxError(regexAsStr);
    }

    for (String directory : allDirectories) {
      // Get the file and path string args.
      String[] filePath =
          Directory.seperateInnerMostAbstractFileFromPath(directory);
      Directory dir = null;
      File file = null;

      // Get path to file
      try {
        dir = Directory.getDirectoryFromPath(filePath[0], fileSystem);
      } catch (FileNotFoundException fnfe) {
        setErrorMessage(fnfe.getMessage());
        System.out.println(fnfe.getMessage());
      }

      // check if file doesn't exist.
      if (dir.getFile(filePath[1]) == null) {
        throw new FileNotFoundException(filePath[0]);
      } else {
        file = dir.getFile(filePath[1]);
      }

      // perform grep on the file.
      if ((regex != null) && (file != null)) {
        grepFile(file, regex, false);
      }
    }

  }

  /**
   * Performs the grep command for Directories (-R option).
   * 
   * @throws InvalidRegexSyntax If the regex is invalid.
   * @throws FileNotFoundException If the Directory doesn't exist.
   * @throws InvalidArgumentsException 
   */
  public void grepDirectories()
      throws InvalidRegexSyntax, FileNotFoundException, InvalidArgumentsException {
    
    // Check if we have the correct number of arguments.
    if (getArgs().length <= 2) {
      throw InvalidArgumentsException
            .createInvalidArgumentsException(CommandTable.GREP, getArgs());
    }

    String[] allDirectories =
        Arrays.copyOfRange(getArgs(), 2, getArgs().length);
    String regexAsStr = null;
    // Check if quotes are supplied.
    if ((getArgs()[1].charAt(0) == '"')
        && (getArgs()[1].charAt(getArgs()[1].length() - 1) == '"')) {
      regexAsStr = getArgs()[1].substring(1, getArgs()[1].length() - 1);
    } else {
      throw InvalidRegexSyntax
          .createInvalidRegexSyntaxError("Missing quotation marks");
    }

    // Compiling the regex statement to confirm it is valid.
    try {
      this.regex = Pattern.compile(regexAsStr);
    } catch (PatternSyntaxException pse) {
      throw InvalidRegexSyntax.createInvalidRegexSyntaxError(regexAsStr);
    }

    // for each given directory in args, we check if it is correct dir.
    for (String directory : allDirectories) {
      // provided the path is a proper directory, grep is performed.
      if (Directory.doesDirectoryExist(directory, fileSystem)) {
        // Perform a grep on this directory.
        Directory dir = Directory.getDirectoryFromPath(directory, fileSystem);
        this.grepFileRecursively(dir, regex);
      } else {
        throw new FileNotFoundException(directory);
      }
    }
  }
}
