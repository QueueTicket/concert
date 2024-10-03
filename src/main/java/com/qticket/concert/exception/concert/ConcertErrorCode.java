package com.qticket.concert.exception.concert;

import com.qticket.concert.exceptionCommon.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ConcertErrorCode implements ErrorCode {
  NOT_FOUND(HttpStatus.NOT_FOUND, "CONCERT_001", "콘서트가 존재하지 않습니다")
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;

  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
