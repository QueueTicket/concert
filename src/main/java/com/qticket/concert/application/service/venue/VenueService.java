package com.qticket.concert.application.service.venue;

import com.qticket.concert.infrastructure.repository.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VenueService {

  private final VenueRepository venueRepository;
}
