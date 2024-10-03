package com.qticket.concert.application.service.venue;

import com.qticket.concert.application.service.venue.mapper.VenueMapper;
import com.qticket.concert.domain.seat.model.Seat;
import com.qticket.concert.domain.venue.Venue;
import com.qticket.concert.exception.seat.SeatErrorCode;
import com.qticket.concert.exception.venue.VenueErrorCode;
import com.qticket.concert.exceptionCommon.QueueTicketException;
import com.qticket.concert.infrastructure.repository.seat.SeatRepository;
import com.qticket.concert.infrastructure.repository.venue.VenueRepository;
import com.qticket.concert.presentation.seat.dto.request.CreateSeatRequest;
import com.qticket.concert.presentation.venue.dto.request.CreateVenueRequest;
import com.qticket.concert.presentation.venue.dto.response.VenueResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VenueService {

  private final VenueRepository venueRepository;

  public VenueResponse createVenue(CreateVenueRequest request) {
    Venue venue = VenueMapper.createRequestToEntity(request);
    venueRepository.save(venue);

    List<CreateSeatRequest> seatList = request.getSeats();

    // 전체 좌석 수 계산 및 검증
    int seatCount = request.getSeats().stream()
        .mapToInt(CreateSeatRequest::getSeatCount)
        .sum();

    if (seatCount != request.getSeatCapacity()){
      throw new QueueTicketException(SeatErrorCode.INSUFFICIENT_SEATS);
    }

    for (CreateSeatRequest createSeatRequest : seatList) {
      for(int i=1; i<=createSeatRequest.getSeatCount();i++){
        Seat seat = Seat.builder()
            .seatNumber(i)
            .venue(venue)
            .seatGrade(createSeatRequest.getSeatGrade())
            .build();
        seat.addSeat(venue);
      }
      venueRepository.flush();
    }
    return VenueMapper.toResponse(venue);
  }
}
