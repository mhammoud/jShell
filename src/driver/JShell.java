// **********************************************************
// Assignment2:
// Student1: Brendan Zhang
// UTORID user_name: zhangbr6
// UT Student #: 1001356356
// Author:
//
// Student2: Mohamed Hammoud
// UTORID user_name: hammoudm
// UT Student #:999173415
// Author:
//
// Student3: Julia Yan
// UTORID user_name: yanjuli1
// UT Student #: 1002353491
// Author:
//
// Student4: Adam Ah-Chong
// UTORID user_name: chongada
// UT Student #: 1002771182
// Author:
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************

package driver;

import commands.Command;
import directoryFileFoldersPackage.JFileSystem;
import utils.InputParser;

/**
 * This class contains the methods and variables for a JShell which is an
 * instance of an in-memory command line simulator. The JShell accepts commands
 * from the user through keyboard input and executes the commands, along with
 * printing Error messages if a Command has an error such as an invalid argument
 * or path.
 * 
 * @author bzhan
 *
 */
public class JShell {
  /**
   * Determines whether this JShell is still running
   */
  private boolean running;

  /**
   * Default class constructor that sets this JShell to start up running.
   */
  public JShell() {
    this.running = true;
  }

  /**
   * Stops this JShell from running.
   */
  public void stopRunning() {
    this.running = false;
  }

  /**
   * Returns whether this JShell is running or not.
   * 
   * @return <tt>true</tt> if this JShell is running
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * Main method that creates the JShell and InputParser.
   * 
   * @param args Command line arguments that are simply ignored
   */
  public static void main(String[] args) {
    // Create new JShell
    JShell jshell = new JShell();
    // Create InputParser to parse user input
    InputParser inputParser = new InputParser(jshell);
    while (jshell.running) {
      // PC: prefix is for styling. Prints name of current Directory after each
      // command
      System.out.print("PC: "
          + JFileSystem.getFileSystem().getCurrentDirectory().getName() + " ");
      // Get command with inputParser
      Command currentCommand = inputParser.getCommand();
      // Execute if valid command keyword
      if (currentCommand != null) {
        currentCommand.executeCommand();
      }
    }
  }
}
