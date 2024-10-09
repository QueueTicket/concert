package com.qticket.concert.presentation.seat.dto.request;

import com.qticket.concert.domain.seat.model.SeatGrade;
import java.util.UUID;
import lombok.Data;

@Data
public class CreateSeatRequest {
  private Integer seatCount;
  private SeatGrade seatGrade;
}
