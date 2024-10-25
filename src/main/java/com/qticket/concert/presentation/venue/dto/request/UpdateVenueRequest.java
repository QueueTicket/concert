package com.qticket.concert.presentation.venue.dto.request;

import com.qticket.concert.presentation.seat.dto.request.UpdateSeatRequest;
import java.util.List;
import lombok.Data;

@Data
public class UpdateVenueRequest {
  private String venueName;
  private String venueAddress;
  private List<UpdateSeatRequest> seatRequests;
}
