package com.qticket.concert.presentation.venue;

import lombok.Data;

@Data
public class VenueSearchCond {
  private String venueName;
  private String venueAddress;
  private Integer seatCapacity;
}
