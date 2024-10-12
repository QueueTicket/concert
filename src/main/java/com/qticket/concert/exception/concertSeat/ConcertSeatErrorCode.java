package com.qticket.concert.exception.concertSeat;

import com.qticket.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ConcertSeatErrorCode implements ErrorCode {
  NOT_FOUND(HttpStatus.NOT_FOUND, "CONCERT_SEAT_001", "공연 좌석이 존재하지 않습니다"),
  PREEMPTED(HttpStatus.CONFLICT, "CONCERT_SEAT_001", "공연 좌석이 존재하지 않습니다")
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
