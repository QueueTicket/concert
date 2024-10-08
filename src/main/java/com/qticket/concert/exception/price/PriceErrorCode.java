package com.qticket.concert.exception.price;

import com.qticket.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PriceErrorCode implements ErrorCode {
  NOT_FOUND(HttpStatus.NOT_FOUND, "PRICE_001", "가격이 존재하지 않습니다"),
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
