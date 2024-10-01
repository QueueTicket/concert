package com.qticket.concert.presentation.concert.dto.requset;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class UpdateConcertRequest {

  private String concertTitle;
  private LocalDateTime concertStartTime;
  private Integer playtime;
  private String description;
  private List<PriceRequest> prices;
}
