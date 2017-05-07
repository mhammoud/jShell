package writer;

public class ConsoleWriter implements Writer {

  private String toWrite;

  public ConsoleWriter() {
    this.toWrite = "";
  }

  public ConsoleWriter(String toWrite) {
    this.toWrite = toWrite;
  }

  @Override
  public void write() {
    if (toWrite != null) {
      if (!toWrite.equals("")) {
        System.out.println(toWrite);
      }
    }

  }

  /**
   * @param toWrite the String to write
   */
  @Override
  public void setToWrite(String toWrite) {
    // TODO Auto-generated method stub
    this.toWrite = toWrite;
  }
}
