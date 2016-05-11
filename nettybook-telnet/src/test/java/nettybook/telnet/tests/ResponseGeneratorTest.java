package nettybook.telnet.tests;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseGeneratorTest {

  @Test
  public void testNullString() {
    String request = null;

    ResponseGenerator generator = new ResponseGenerator(request);
    assertThat(generator);

    assertThat(generator.response()).isNotNull();
    assertThat(generator.response()).isEqualTo("명령을 입력해 주세요.\r\n");

    assertThat(generator.isClose()).isFalse();
  }

  @Test
  public void testZeroLengthString() {
    String request = "";

    ResponseGenerator generator = new ResponseGenerator(request);
    assertThat(generator);

    assertThat(generator.response()).isNotNull();
    assertThat(generator.response()).isEqualTo("명령을 입력해 주세요.\r\n");

    assertThat(generator.isClose()).isFalse();
  }

  @Test
  public void testHi() {
    String request = "Hi";

    ResponseGenerator generator = new ResponseGenerator(request);
    assertThat(generator);

    assertThat(generator.response()).isNotEmpty();
    assertThat(generator.response())
        .isEqualTo("입력하신 명령이 '" + request + "' 입니까?\r\n");

    assertThat(generator.isClose()).isFalse();
  }

  @Test
  public void testBye() {
    String request = "BYE";

    ResponseGenerator generator = new ResponseGenerator(request);
    assertThat(generator);

    assertThat(generator.response()).isNotEmpty();
    assertThat(generator.response())
        .isEqualTo("좋은 하루 되세요\r\n");

    assertThat(generator.isClose()).isTrue();
  }
}
