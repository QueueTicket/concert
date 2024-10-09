package com.qticket.concert.presentation.concert.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ConcertSearchCond {
  private String concertTitle;
  private LocalDateTime concertStartTime;

}
