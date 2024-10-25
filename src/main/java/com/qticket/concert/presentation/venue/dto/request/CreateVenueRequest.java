package com.qticket.concert.presentation.venue.dto.request;

import com.qticket.concert.presentation.seat.dto.request.CreateSeatRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class CreateVenueRequest {
  @NotBlank
  private String venueName;
  @NotBlank
  private String venueAddress;
  @NotNull
  @Min(value = 0, message = "좌석은 0개 이하일 수 없습니다")
  private Integer seatCapacity;
  private List<CreateSeatRequest> seats;
}

