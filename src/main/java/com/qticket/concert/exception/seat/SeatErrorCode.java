package com.qticket.concert.exception.seat;

import com.qticket.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SeatErrorCode implements ErrorCode {
  NOT_FOUND(HttpStatus.NOT_FOUND, "SEAT_001", "좌석이 존재하지 않습니다"),
  INSUFFICIENT_SEATS(HttpStatus.BAD_REQUEST, "SEAT_002", "좌석 수가 부족합니다"),
  DUPLICATE_SEATNUMBER(HttpStatus.BAD_REQUEST, "SEAT_003", "좌석 번호가 중복됩니다")
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
