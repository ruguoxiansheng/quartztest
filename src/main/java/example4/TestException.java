package example4;

/**
 * @ Author     ：Jet.wu [jet.wu@kpmg.com]
 * @ Date       ：Created in 9:45 上午 29/04/2019
 * @ Description：${description}
 * @since jdk1.8
 */
public class TestException extends RuntimeException {

  public TestException() {
  }

  public TestException(String message) {
    super(message);
  }

  public TestException(String message, Throwable cause) {
    super(message, cause);
  }

  public TestException(Throwable cause) {
    super(cause);
  }
}
