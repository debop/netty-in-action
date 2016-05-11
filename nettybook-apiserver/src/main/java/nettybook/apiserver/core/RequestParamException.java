package nettybook.apiserver.core;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class RequestParamException extends RuntimeException {

  public RequestParamException() {}
  public RequestParamException(String message) {
    super(message);
  }
  public RequestParamException(String message, Throwable cause) {
    super(message, cause);
  }
  public RequestParamException(Throwable cause) {
    super(cause);
  }
  protected RequestParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
