package nettybook.apiserver.core;

public interface KeyMaker {

  /**
   * 키 생성기로부터 만들어진 키를 반환한다.
   *
   * @return 만들어진 키
   */
  public String getKey();
}
