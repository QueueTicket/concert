package com.qticket.concert.presentation.concert.dto.requset;

import com.qticket.concert.domain.seat.model.SeatGrade;
import lombok.Data;

@Data
public class PriceRequest{
  private Integer price;
  private SeatGrade seatGrade;
}
