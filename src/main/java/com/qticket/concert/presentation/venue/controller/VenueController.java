package com.qticket.concert.presentation.venue.controller;

import com.qticket.concert.application.service.venue.VenueService;
import com.qticket.concert.presentation.seat.dto.request.CreateSeatRequest;
import com.qticket.concert.presentation.seat.dto.response.SeatResponse;
import com.qticket.concert.presentation.venue.VenueSearchCond;
import com.qticket.concert.presentation.venue.dto.request.CreateVenueRequest;
import com.qticket.concert.presentation.venue.dto.request.UpdateVenueRequest;
import com.qticket.concert.presentation.venue.dto.response.VenueResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venues")
@Slf4j
public class VenueController {

  private final VenueService venueService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public VenueResponse createVenue(@RequestBody CreateVenueRequest request){
    return venueService.createVenue(request);
  }

  // 좌석 추가
  @PostMapping("/{venueId}/seats")
  public VenueResponse addSeats(@PathVariable UUID venueId, @RequestBody CreateSeatRequest request){
    return venueService.addSeats(venueId, request);
}

  @PutMapping("/{venueId}")
  @ResponseStatus(HttpStatus.OK)
  public VenueResponse updateVenue(@PathVariable UUID venueId, @RequestBody UpdateVenueRequest request){
    return venueService.updateVenueAndSeats(venueId, request);
  }

  @DeleteMapping("/{venueId}")
  public ResponseEntity<Void> deleteVenue(@PathVariable UUID venueId){
    venueService.deleteVenue(venueId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<VenueResponse> searchVenues(Pageable pageable, VenueSearchCond cond){
    return venueService.searchVenues(pageable, cond);
  }

  @GetMapping("/{venueId}")
  @ResponseStatus(HttpStatus.OK)
  public VenueResponse getOneVenue(@PathVariable UUID venueId){
    return venueService.getOneVenue(venueId);
  }

  @GetMapping("/{venueId}/seats/{seatId}")
  @ResponseStatus(HttpStatus.OK)
  public SeatResponse getOneSeat(@PathVariable UUID venueId, @PathVariable UUID seatId){
    return venueService.getOneSeat(venueId, seatId);
  }
}
