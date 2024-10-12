package com.qticket.concert.application.service.venue;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.concert.application.service.concertSeat.ConcertSeatService;
import com.qticket.concert.application.service.seat.SeatService;
import com.qticket.concert.application.service.seat.mapper.SeatMapper;
import com.qticket.concert.application.service.venue.mapper.VenueMapper;
import com.qticket.concert.domain.concert.model.Concert;
import com.qticket.concert.domain.concert.model.Price;
import com.qticket.concert.domain.concertSeat.model.ConcertSeat;
import com.qticket.concert.domain.seat.model.Seat;
import com.qticket.concert.domain.seat.service.SeatUpdateService;
import com.qticket.concert.domain.venue.Venue;
import com.qticket.concert.exception.price.PriceErrorCode;
import com.qticket.concert.exception.seat.SeatErrorCode;
import com.qticket.concert.exception.venue.VenueErrorCode;
import com.qticket.concert.infrastructure.repository.venue.VenueRepository;
import com.qticket.concert.presentation.seat.dto.request.CreateSeatRequest;
import com.qticket.concert.presentation.seat.dto.request.UpdateSeatRequest;
import com.qticket.concert.presentation.seat.dto.response.SeatResponse;
import com.qticket.concert.presentation.venue.VenueSearchCond;
import com.qticket.concert.presentation.venue.dto.request.CreateVenueRequest;
import com.qticket.concert.presentation.venue.dto.request.UpdateVenueRequest;
import com.qticket.concert.presentation.venue.dto.response.VenueResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VenueService {

  private final VenueRepository venueRepository;
  private final SeatService seatService;
  private final SeatUpdateService seatUpdateService;

  public VenueResponse createVenue(CreateVenueRequest request) {
    Venue venue = VenueMapper.createRequestToEntity(request);
    venueRepository.save(venue);
    List<CreateSeatRequest> seatList = request.getSeats();

    // 전체 좌석 수 계산 및 검증
    int seatCount = request.getSeats().stream().mapToInt(CreateSeatRequest::getSeatCount).sum();
    if (seatCount != request.getSeatCapacity()) {
      throw new QueueTicketException(SeatErrorCode.INSUFFICIENT_SEATS);
    }

    for (CreateSeatRequest createSeatRequest : seatList) {
      for (int i = 1; i <= createSeatRequest.getSeatCount(); i++) {
        Seat.createSeat(createSeatRequest, i, venue);
      }
      venueRepository.flush();
    }
    return VenueMapper.toResponse(venue);
  }

  public VenueResponse updateVenueAndSeats(UUID venueId, UpdateVenueRequest request) {
    Venue venue = getVenue(venueId);
    venue.update(request);

    venue.getSeats().forEach(seat ->
        request.getSeatRequests().stream()
            .filter(r -> r.getSeatId().equals(seat.getId()))
            .forEach(r -> {
                  seatUpdateService.updateSeatGradeAndPrice(seat, r, venue);
            }));
    log.info("venue and Seats updated");
    return VenueMapper.toResponse(venue);
  }

  public void deleteVenue(UUID venueId) {
    Venue venue = getVenue(venueId);
    String username = "username";
    seatService.delete(venueId, username);
    venue.softDelete(username);
    log.info("delete venue complete venueId : {} by username : {}", venueId, username);
  }

  @Transactional(readOnly = true)
  public Page<VenueResponse> searchVenues(Pageable pageable, VenueSearchCond cond) {
    return venueRepository.searchVenue(pageable, cond);
  }

  @Transactional(readOnly = true)
  public VenueResponse getOneVenue(UUID venueId) {
    Venue venue = getVenue(venueId);
    return VenueMapper.toResponse(venue);
  }

  private Venue getVenue(UUID venueId) {
    return venueRepository.findById(venueId)
        .orElseThrow(() -> new QueueTicketException(VenueErrorCode.NOT_FOUND));
  }

  public SeatResponse getOneSeat(UUID venueId, UUID seatId) {
    Venue venue = getVenue(venueId);
    Seat seat = venue.getSeats().stream()
        .filter(s -> s.getId().equals(seatId))
        .findFirst()
        .orElseThrow(() -> new QueueTicketException(SeatErrorCode.NOT_FOUND));
    return SeatMapper.toSeatResponse(seat);
  }

  public VenueResponse addSeats(UUID venueId, CreateSeatRequest request) {
    Venue venue = getVenue(venueId);
    int maxSeatCount = venue.getSeats().stream()
        .filter(seat -> seat.getSeatGrade().equals(request.getSeatGrade()))
        .mapToInt(Seat::getSeatNumber)
        .max()
        .orElse(0);
    for (int i = 1; i <= request.getSeatCount(); i++) {
      Seat seat = Seat.builder()
          .seatNumber(i + maxSeatCount)
          .venue(venue)
          .seatGrade(request.getSeatGrade())
          .build();
      venue.getSeats().add(seat);
    }
    Venue savedVenue = venueRepository.save(venue);
    log.info("add Seat complete");
    return VenueMapper.toResponse(savedVenue);
  }
}
