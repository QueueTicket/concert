package com.qticket.concert.presentation.venue.controller;

import com.qticket.concert.application.service.venue.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venues")
@Slf4j
public class VenueController {

  private final VenueService venueService;
}
