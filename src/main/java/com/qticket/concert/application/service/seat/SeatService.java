package com.qticket.concert.application.service.seat;

import com.qticket.concert.infrastructure.repository.seat.SeatRepository;
import com.qticket.concert.infrastructure.repository.venue.VenueRepository;
import com.qticket.concert.presentation.seat.dto.response.SeatResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SeatService {

  private final SeatRepository seatRepository;
  private final VenueRepository venueRepository;

  public void delete(UUID venueId, String username) {
    seatRepository.softDeleteWithVenueId(venueId,username);
  }

}
