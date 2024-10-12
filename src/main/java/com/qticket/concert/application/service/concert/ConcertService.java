package com.qticket.concert.application.service.concert;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.concert.application.service.concert.mapper.ConcertMapper;
import com.qticket.concert.application.service.concertSeat.ConcertSeatService;
import com.qticket.concert.domain.concert.model.Concert;
import com.qticket.concert.domain.concert.model.Price;
import com.qticket.concert.domain.seat.model.Seat;
import com.qticket.concert.domain.seat.model.SeatGrade;
import com.qticket.concert.domain.venue.Venue;
import com.qticket.concert.exception.concert.ConcertErrorCode;
import com.qticket.concert.exception.venue.VenueErrorCode;
import com.qticket.concert.infrastructure.repository.concert.ConcertRepository;
import com.qticket.concert.infrastructure.repository.venue.VenueRepository;
import com.qticket.concert.presentation.concert.dto.ConcertSearchCond;
import com.qticket.concert.presentation.concert.dto.requset.CreateConcertRequest;
import com.qticket.concert.presentation.concert.dto.requset.UpdateConcertRequest;
import com.qticket.concert.presentation.concert.dto.response.ConcertResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ConcertService {

  private final ConcertRepository concertRepository;
  private final VenueRepository venueRepository;
  private final ConcertSeatService concertSeatService;

  // 공연 생성 시, 가격과 공연의 좌석 까지 전부 생성
  @CacheEvict(cacheNames = "concertAllCache", allEntries = true)
  public ConcertResponse createConcert(CreateConcertRequest request) {
    Venue venue =
        venueRepository
            .findById(request.getVenueId())
            .orElseThrow(() -> new QueueTicketException(VenueErrorCode.NOT_FOUND));
    Concert concert = ConcertMapper.requestToConcert(request, venue);

    // 해당 공연장에 존재하는 좌석 등급으로만 가격 등록
    Map<SeatGrade, List<Seat>> seatMap = venue.getSeats().stream()
        .collect(Collectors.groupingBy(Seat::getSeatGrade));
    request.getPrices().stream()
        .filter(pr -> seatMap.containsKey(pr.getSeatGrade()))
        .map(pr -> Price.builder()
            .seatGrade(pr.getSeatGrade())
            .price(pr.getPrice())
            .build())
        .forEach(p -> p.addConcert(concert));

    Concert savedConcert = concertRepository.save(concert);
    // 공연 좌석 생성
    concertSeatService.createConcertSeat(savedConcert, venue);
    return ConcertMapper.toConcertResponse(savedConcert);
  }

  @CachePut(cacheNames = "concertCache", key = "#concertId")
  @CacheEvict(cacheNames = "concertAllCache", allEntries = true)
  public ConcertResponse updateConcert(UpdateConcertRequest request, UUID concertId) {
    Concert concert =
        concertRepository
            .findById(concertId)
            .orElseThrow(() -> new QueueTicketException(ConcertErrorCode.NOT_FOUND));

    concert.update(request);
    return ConcertMapper.toConcertResponse(concert);
  }

  @CacheEvict(cacheNames = "concertAllCache", allEntries = true)
  public void deleteConcert(UUID concertId, String username) {
    Concert concert =
        concertRepository
            .findById(concertId)
            .orElseThrow(() -> new QueueTicketException(ConcertErrorCode.NOT_FOUND));
    // 공연 좌석 삭제
    int count = concertSeatService.deleteWithConcert(concertId);
    log.info("soft delete {} lines in concertSeat", count);
    // 공연 삭제
    concert.softDelete(username);
    // 가격 삭제
    concert.getPrices()
        .forEach(p -> p.softDelete(username));
  }

  @Cacheable(cacheNames = "concertAllCache", key = "{#page.pageNumber, #page.pageSize, #cond.hashCode()}")
  @Transactional(readOnly = true)
  public Page<ConcertResponse> getAllConcerts(Pageable page, ConcertSearchCond cond) {
    return concertRepository.searchConcert(page, cond);
  }

  @Cacheable(cacheNames = "concertCache", key = "#concertId")
  @Transactional(readOnly = true)
  public ConcertResponse getOneConcert(UUID concertId) {
    Concert concert =
        concertRepository
            .findById(concertId)
            .orElseThrow(() -> new QueueTicketException(ConcertErrorCode.NOT_FOUND));
    return ConcertMapper.toConcertResponse(concert);
  }
}
