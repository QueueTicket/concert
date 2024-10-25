package com.qticket.concert.presentation.concert.dto.response;

import com.qticket.concert.domain.seat.model.SeatGrade;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class PriceResponse implements Serializable {
  private UUID priceId;
  private Integer price;
  private SeatGrade seatGrade;
}
