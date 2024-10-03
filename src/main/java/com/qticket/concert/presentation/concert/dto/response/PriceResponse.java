package com.qticket.concert.presentation.concert.dto.response;

import com.qticket.concert.domain.seat.model.SeatGrade;
import java.io.Serializable;
import lombok.Data;

@Data
public class PriceResponse implements Serializable {
  private Integer price;
  private SeatGrade seatGrade;
}
