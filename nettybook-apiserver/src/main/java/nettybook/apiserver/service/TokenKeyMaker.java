package nettybook.apiserver.service;

import io.netty.util.CharsetUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nettybook.apiserver.core.KeyMaker;
import nettybook.apiserver.utils.MurmurHash3;

@Slf4j
@Getter
public class TokenKeyMaker implements KeyMaker {

  static final int SEED_MURMURHASH = 0x1234ABCD;

  private final String email;
  private final long issueDate;
  private final byte[] bytes;

  public TokenKeyMaker(String email, long issueDate) {
    this.email = email;
    this.issueDate = issueDate;

    String source = email + String.valueOf(issueDate);
    this.bytes = source.getBytes(CharsetUtil.UTF_8);
  }

  @Override
  public String getKey() {
    return Long.toString(MurmurHash3.murmurhash3x8632(bytes, 0, bytes.length, SEED_MURMURHASH));
  }
}
