package com.qticket.concert.application.service.seat.mapper;

import com.qticket.concert.domain.seat.model.Seat;
import com.qticket.concert.presentation.seat.dto.response.SeatResponse;

public class SeatMapper {
  public static SeatResponse toSeatResponse(Seat seat) {
    SeatResponse response = new SeatResponse();
    response.setVenueId(seat.getVenue().getId());
    response.setSeatGrade(seat.getSeatGrade());
    response.setSeatNumber(seat.getSeatNumber());
    response.setSeatId(seat.getId());
    return response;
  }
}
