package com.qticket.concert.presentation.concert.dto.requset;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CreateConcertRequest {
  @NotNull
  private String concertTitle;
  @NotNull
  @FutureOrPresent(message = "공연 시작 시간은 과거일 수 없습니다")
  private LocalDateTime concertStartTime;
  @NotNull
  @Min(value = 1, message = "공연 시간은 1분보다 적을 수 없습니다")
  private Integer playtime;
  private String description;
  private List<PriceRequest> prices;
}
