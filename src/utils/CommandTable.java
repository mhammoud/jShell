package utils;

import java.util.Hashtable;

public class CommandTable {

  // Keys for the commands
  public static final String MAKE_DIRECTORY = "mkdir";
  public static final String CHANGE_DIRECTORY = "cd";
  public static final String LIST_SEGMENTS = "ls";
  public static final String EXIT = "exit";
  public static final String PRESENT_WORKING_DIRECTORY = "pwd";
  public static final String ECHO = "echo";
  public static final String MANUAL = "man";
  public static final String POP_DIRECTORY = "popd";
  public static final String PUSH_DIRECTORY = "pushd";
  public static final String HISTORY = "history";
  public static final String CONCATENATE = "cat";
  public static final String MOVE = "mv";
  public static final String CURL = "curl";
  public static final String COPY = "cp";
  public static final String GREP = "grep";
  public static Hashtable<String, String> commandTable = null;

  /**
   * Gets an instance of the commandTable.
   * 
   * @return The Hashtable that contains the command keywords as keys and the
   *         class name of the Command as values
   */
  public static Hashtable<String, String> getCommandTable() {
    if (commandTable == null) {
      commandTable = new Hashtable<String, String>();
      commandTable.put(MAKE_DIRECTORY, "commands.MakeDirectory");
      commandTable.put(CHANGE_DIRECTORY, "commands.ChangeDirectory");
      commandTable.put(LIST_SEGMENTS, "commands.ListSegments");
      commandTable.put(EXIT, "commands.Exit");
      commandTable.put(PRESENT_WORKING_DIRECTORY,
          "commands.PresentWorkingDirectory");
      commandTable.put(ECHO, "commands.Echo");
      commandTable.put(MANUAL, "commands.Manual");
      commandTable.put(POP_DIRECTORY, "commands.PopDirectory");
      commandTable.put(PUSH_DIRECTORY, "commands.PushDirectory");
      commandTable.put(HISTORY, "commands.History");
      commandTable.put(CONCATENATE, "commands.Concatenate");
      commandTable.put(MOVE, "commands.Move");
      commandTable.put(CURL, "commands.Curl");
      commandTable.put(COPY, "commands.Copy");
      commandTable.put(GREP, "commands.Grep");
    }
    return commandTable;
  }
}


