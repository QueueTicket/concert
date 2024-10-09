package com.qticket.concert.presentation.concertSeat.dto.request;

import com.qticket.concert.domain.concertSeat.model.SeatStatus;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class UpdateConcertSeatRequest {
  private List<UUID> concertSeatIds;
  private SeatStatus status;
}
