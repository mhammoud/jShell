package commands;

import driver.JShell;

/**
 * This class consists of methods and variables required to execute the "exit"
 * command to exit the JShell.
 * 
 * The Exit command stops the JShell from running.
 * 
 * @author Brendan Zhang
 * 
 */
public class Exit extends Command {
  /**
   * The JShell to exit
   */
  JShell shell;

  public Exit() {
    this.shell = null;
  }

  /**
   * Class constructor that sets the JShell to exit.
   * 
   * @param shell
   */
  public Exit(JShell shell) {
    this.shell = shell;
  }

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the Exit command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "exit" + "\n\n" + "SYNOPSIS" + "\n\t" + "exit"
        + "\n\n" + "DESCRIPTION" + "\n\t"
        + "Quits the current instance of the JShell. "
        + "All files made and changed will be lost.\n";
  }

  /**
   * Executes the "exit" command.
   * <p>
   * Stops the JShell from running.
   */
  @Override
  public void executeCommand() {
    shell.stopRunning();
  }

}
