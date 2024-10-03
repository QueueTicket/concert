package com.qticket.concert.presentation.concert.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ConcertResponse implements Serializable {
  private UUID concertId;
  private UUID venueId;
  private String concertTitle;
  private LocalDateTime concertStartTime;
  private Integer playtime;
  private String description;
  private List<PriceResponse> prices;
}

