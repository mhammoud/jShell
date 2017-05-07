package commands;

import directoryFileFoldersPackage.File;
import exceptions.FileNotFoundException;
import writer.Writer;

/**
 * This class consists of the methods and instance variables required for any
 * Command that will be executed by the JShell.
 * 
 * @author Brendan Zhang
 *
 */
public abstract class Command {

  /**
   * The arguments of the command. This is an array of String with each String
   * being a separate argument.
   */
  private String[] args;
  /**
   * The output of the command. Initialize as empty String.
   */
  private String output = "";
  /**
   * The String that contains the path of the File to output to. If null, this
   * Command outputs to standard output.
   */
  private File outputTo = null;
  /**
   * Boolean variable that contains whether to append the output. If true, the
   * command appends the output. Otherwise, this command writes the output.
   */
  private boolean appendOutput = false;
  /**
   * The error message that is received by the execution of this Command.
   */
  private String errorMessage = "";
  /**
   * The Writer that will be used to write the output of this Command.
   */
  private Writer writer;

  /**
   * Executes the command with the current arguments and sets the output of the
   * command after executing.
   * 
   * @throws FileNotFoundException
   */
  public abstract void executeCommand();

  /**
   * Method to get the man message of a command, designed for subclasses to
   * override it
   * 
   * @return The manual string for the command
   */
  public static String getManMessage() {
    return "";
  }

  /**
   * Returns the arguments of the command.
   * 
   * @return the arguments of the command
   */
  public String[] getArgs() {
    return args;
  }

  /**
   * Sets the arguments of the command.
   * 
   * @param args The arguments to set to.
   */
  public void setArgs(String[] args) {
    this.args = args;
  }

  /**
   * Returns the output of the command.
   * 
   * @return the output of the command.
   */
  public String getOutput() {
    return output;
  }

  /**
   * Sets the output of the command.
   * 
   * @param output The output to set to.
   */
  public void setOutput(String output) {
    this.output = output;
  }

  /**
   * @return the outputTo
   */
  public File getOutputTo() {
    return outputTo;
  }

  /**
   * @param outputTo the outputTo to set
   */
  public void setOutputTo(File outputTo) {
    this.outputTo = outputTo;
  }

  /**
   * @return the appendOutput
   */
  public boolean isAppendOutput() {
    return appendOutput;
  }

  /**
   * @param appendOutput the appendOutput to set
   */
  public void setAppendOutput(boolean appendOutput) {
    this.appendOutput = appendOutput;
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
   * @return the writer
   */
  public Writer getWriter() {
    return writer;
  }

  /**
   * @param writer the writer to set
   */
  public void setWriter(Writer writer) {
    this.writer = writer;
  }

}
