package com.qticket.concert.presentation.concertSeat.dto.response;

import com.qticket.concert.domain.concertSeat.model.ConcertSeat;
import com.qticket.concert.domain.concertSeat.model.SeatStatus;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class ConcertSeatResponse implements Serializable {
  private UUID concertSeatId;
  private UUID seatId;
  private UUID priceId;
  private SeatStatus status;

  public static ConcertSeatResponse fromEntity(ConcertSeat concertSeat){
    ConcertSeatResponse response = new ConcertSeatResponse();
    response.setConcertSeatId(concertSeat.getId());
    response.setSeatId(concertSeat.getSeat().getId());
    response.setPriceId(concertSeat.getPrice().getId());
    response.setStatus(concertSeat.getStatus());
    return response;
  }
}
