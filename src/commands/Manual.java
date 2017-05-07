package commands;

import java.lang.reflect.Method;

import utils.CommandTable;
import writer.ConsoleWriter;

/**
 * This class has the methods and variables to execute the manual command which
 * outputs the manual entry for the given command.
 * 
 * @author Julia Yan
 * @author Brendan Zhang
 */
public class Manual extends Command {

  public Manual() {
    setArgs(null);
    setOutput("");
    setWriter(new ConsoleWriter());
  }

  /**
   * Constructor for the manual
   * 
   * @param The arguments for using the command
   */
  public Manual(String[] args) {
    setArgs(args);
    setOutput("");
    setWriter(new ConsoleWriter());
  }

  /**
   * Gets the man message for this command
   * 
   * @return The manual string for the Manual command
   */
  public static String getManMessage() {
    return "NAME" + "\n\t" + "man - display a digital manual page" + "\n\n"
        + "SYNOPSIS" + "\n\t" + "man [COMMAND NAME]..." + "\n\n" + "DESCRIPTION"
        + "\n\t"
        + "Given a command as an argument, man or Manual returns an\n\t"
        + "an instruction manual detailing the function of a command.\n\n"
        + "EXAMPLES" + "\n\t" + "Inputting 'man man' will return this manual.";
  }

  /**
   * Gets the output message for the given command. If there is no manual entry
   * for the given command or it is not a recognized command, writes
   * "No manual entry for CMD"
   * 
   * @param command String that is the keyword for a Command
   * @return a String that contains the manual entry for the given Command
   */
  private String outputMessage(String command) {
    String message;
    try {
      // Get the type
      String cmdType = CommandTable.getCommandTable().get(command);

      // Get the method getManMessage from this type and execute it
      Method manMessageMethod =
          Class.forName(cmdType).getMethod("getManMessage");
      message = (String) manMessageMethod.invoke(null);
    } catch (Exception e) {
      message = "";
    }
    // If the manual entry is empty
    if (message.equals("")) {
      message = "No manual entry for " + command;
    }
    return message;
  }

  @Override
  /**
   * Executes the given manual command. If there are no arguments, asks for
   * them. If there is more then one, then only runs on the first command
   */
  public void executeCommand() {
    // If there is no command given for a manual page
    if (getArgs().length == 0) {
      setOutput("What manual page do you want?");
    } else {
      setOutput(outputMessage(getArgs()[0]));
    }
    // Write output of command
    getWriter().setToWrite(getOutput());
    getWriter().write();
  }
}
