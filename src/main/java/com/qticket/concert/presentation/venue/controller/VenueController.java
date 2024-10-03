package com.qticket.concert.presentation.venue.controller;

import com.qticket.concert.application.service.venue.VenueService;
import com.qticket.concert.presentation.venue.dto.request.CreateVenueRequest;
import com.qticket.concert.presentation.venue.dto.response.VenueResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
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
}
