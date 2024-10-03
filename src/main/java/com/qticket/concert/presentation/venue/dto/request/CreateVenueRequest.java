package com.qticket.concert.presentation.venue.dto.request;

import com.qticket.concert.presentation.seat.dto.request.CreateSeatRequest;
import java.util.List;
import lombok.Data;

@Data
public class CreateVenueRequest {
  private String venueName;
  private String venueAddress;
  private Integer seatCapacity;
  private List<CreateSeatRequest> seats;
}

