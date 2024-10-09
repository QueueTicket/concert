package com.qticket.concert.presentation.venue.dto.response;

import com.qticket.concert.presentation.seat.dto.response.SeatResponse;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class VenueResponse implements Serializable {
  private UUID venueId;
  private String venueName;
  private String venueAddress;
  private Integer seatCapacity;
  private List<SeatResponse> seats;
}
