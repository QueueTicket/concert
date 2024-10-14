package com.qticket.concert.presentation.seat.dto.request;

import com.qticket.concert.domain.seat.model.SeatGrade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateSeatRequest {
  @NotNull
  @Min(value = 0, message = "좌석은 0개 이하일 수 없습니다")
  private Integer seatCount;
  @NotNull
  private SeatGrade seatGrade;
}
