package com.qticket.concert.application.service.venue.mapper;

import com.qticket.concert.application.service.seat.mapper.SeatMapper;
import com.qticket.concert.domain.venue.Venue;
import com.qticket.concert.presentation.venue.dto.request.CreateVenueRequest;
import com.qticket.concert.presentation.venue.dto.response.VenueResponse;
import java.util.ArrayList;

public class VenueMapper {
  public static Venue createRequestToEntity(CreateVenueRequest request) {
    return Venue.builder()
        .seatCapacity(request.getSeatCapacity())
        .venueName(request.getVenueName())
        .venueAddress(request.getVenueAddress())
        .seats(new ArrayList<>())
        .build();
  }

  public static VenueResponse toResponse(Venue venue) {
    VenueResponse venueResponse = new VenueResponse();
    venueResponse.setVenueId(venue.getId());
    venueResponse.setSeatCapacity(venue.getSeatCapacity());
    venueResponse.setVenueName(venue.getVenueName());
    venueResponse.setVenueAddress(venue.getVenueAddress());
    venueResponse.setSeats(venue.getSeats()
        .stream()
        .map(SeatMapper::toSeatResponse)
        .toList());
    return venueResponse;
  }
}
