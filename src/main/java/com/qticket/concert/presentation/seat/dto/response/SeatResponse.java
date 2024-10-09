package com.qticket.concert.presentation.seat.dto.response;

import com.qticket.concert.domain.seat.model.SeatGrade;
import java.util.UUID;
import lombok.Data;

@Data
public class SeatResponse {
  private UUID seatId;
  private UUID venueId;
  private Integer seatNumber;
  private SeatGrade seatGrade;
}
