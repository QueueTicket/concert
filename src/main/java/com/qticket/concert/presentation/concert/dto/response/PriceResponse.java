package com.qticket.concert.presentation.concert.dto.response;

import com.qticket.concert.domain.seat.model.SeatGrade;
import lombok.Data;

@Data
public class PriceResponse {
  private Integer price;
  private SeatGrade seatGrade;
}
