package com.qticket.concert.application.service.concertSeat;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.concert.application.service.concertSeat.redis.RedisSeatService;
import com.qticket.concert.domain.concert.model.Concert;
import com.qticket.concert.domain.concert.model.Price;
import com.qticket.concert.domain.concertSeat.model.ConcertSeat;
import com.qticket.concert.domain.concertSeat.model.SeatStatus;
import com.qticket.concert.domain.seat.model.Seat;
import com.qticket.concert.domain.seat.model.SeatGrade;
import com.qticket.concert.domain.venue.Venue;
import com.qticket.concert.exception.concertSeat.ConcertSeatErrorCode;
import com.qticket.concert.exception.price.PriceErrorCode;
import com.qticket.concert.infrastructure.repository.concertSeat.ConcertSeatRepository;
import com.qticket.concert.infrastructure.repository.redis.RedisRepository;
import com.qticket.concert.presentation.concertSeat.controller.ConcertSeatController;
import com.qticket.concert.presentation.concertSeat.dto.request.UpdateConcertSeatRequest;
import com.qticket.concert.presentation.concertSeat.dto.response.ConcertSeatResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ConcertSeatService {
  private final ConcertSeatRepository concertSeatRepository;
  private final CacheManager cacheManager;
  private final RedisRepository redisRepository;
  private final RedisSeatService redisSeatService;

  public void createConcertSeat(Concert concert, Venue venue) {
    log.info("create ConcertSeat in createConcert");
    List<Price> prices = concert.getPrices();
    List<Seat> seats = venue.getSeats();

    Map<SeatGrade, Price> priceMap =
        prices.stream()
            .collect(Collectors.toMap(Price::getSeatGrade, p -> p));

    List<SeatGrade> seatGrades = Arrays.asList(SeatGrade.R, SeatGrade.S, SeatGrade.A, SeatGrade.B);

    // 좌석과 가격이 둘 다 존재하는 경우에만 처리
    seatGrades.stream()
        .filter(seatGrade -> seatExistsForGrade(seats, seatGrade)) // 해당 좌석 등급이 존재하는지 확인
        .filter(priceMap::containsKey) // 해당 좌석 등급에 대한 가격이 존재하는지 확인
        .forEach(seatGrade -> saveConcertSeatsForGrade(concert, seats, seatGrade, priceMap));

  }

  // 공연 좌석 저장
  private List<ConcertSeat> saveConcertSeatsForGrade(
      Concert concert,
      List<Seat> seats,
      SeatGrade seatGrade,
      Map<SeatGrade, Price> priceMap) {

    List<Seat> seatsByGrade = seats.stream()
        .filter(s -> s.getSeatGrade() == seatGrade)
        .toList();
    Price price = priceMap.get(seatGrade);

    if (price == null) {
      log.error("saveConcertSeatsForGrade");
      throw new QueueTicketException(PriceErrorCode.NOT_FOUND);
    }

    List<ConcertSeat> concertSeats =
        seatsByGrade.stream()
            .map(s ->
                    ConcertSeat.builder()
                        .seat(s)
                        .price(price)
                        .status(SeatStatus.AVAILABLE).build())
            .toList();
    concertSeatRepository.saveAll(concertSeats);
    log.info("concertSeats size : {}", concertSeats.size());
    concertSeats.forEach(cs -> {
      redisRepository.save(cs, concert);
    });
    return concertSeats;
  }

  private boolean seatExistsForGrade(List<Seat> seats, SeatGrade seatGrade) {
    return seats.stream().anyMatch(seat -> seat.getSeatGrade() == seatGrade);
  }

  @Cacheable(cacheNames = "seatsForConcert", key = "#id")
  public List<ConcertSeat> findByConcertId(UUID id) {
    return concertSeatRepository.findByConcertId(id);
  }

  public int deleteWithConcert(UUID concertId) {
    return concertSeatRepository.deleteWithConcert(concertId);
  }

  @CacheEvict(cacheNames = "seatsForConcert", allEntries = true)
  public List<ConcertSeatResponse> changeStatus(UpdateConcertSeatRequest request) {
    List<UUID> concertSeatIds = request.getConcertSeatIds();
    List<ConcertSeat> concertSeats = concertSeatIds.stream()
        .map(id ->
            concertSeatRepository
                .findById(id)
                .orElseThrow(() -> new QueueTicketException(ConcertSeatErrorCode.NOT_FOUND))
        ).toList();
    concertSeats.forEach(cs -> cs.changeStatus(request.getStatus()));

    // 각 공연 좌석에 대한 다중 캐시 put
    Cache cache = cacheManager.getCache("concertSeat");
    if (cache != null) {
      concertSeats.forEach(seat -> {
        // 새로운 상태를 다시 캐시에 저장
        ConcertSeatResponse response = ConcertSeatResponse.fromEntity(seat);
        cache.put(seat.getId(), response);
      });
    }
    return  concertSeats.stream()
        .map(ConcertSeatResponse::fromEntity)
        .toList();
  }

  @Cacheable(cacheNames = "concertSeat", key = "#concertSeatId")
  public ConcertSeatResponse getOneConcertSeat(UUID concertSeatId) {
    ConcertSeat concertSeat = concertSeatRepository.findById(concertSeatId)
        .orElseThrow(() -> new QueueTicketException(ConcertSeatErrorCode.NOT_FOUND));
    return ConcertSeatResponse.fromEntity(concertSeat);
  }

  public Optional<ConcertSeat> getConcertSeatBySeat(UUID seatId) {
    return concertSeatRepository
        .findBySeatId(seatId);
  }

  public List<UUID> selectConcertSeats(List<UUID> concertIds) {
    List<UUID> selectedSeats = new ArrayList<>();
    concertIds.forEach(
        id -> {
          Integer flag = redisRepository.getSeatFlagById(id);
          log.info("flag : {}", flag);
          Long result = redisRepository.selectSeats(id);
          log.info("result : {}", result);
          if (result < 0) {
            redisRepository.cancelSelect(id);
            throw new QueueTicketException(ConcertSeatErrorCode.PREEMPTED);
          } else {
            ConcertSeat concertSeat =
                concertSeatRepository
                    .findById(id)
                    .orElseThrow(() -> new QueueTicketException(ConcertSeatErrorCode.NOT_FOUND));
            concertSeat.changeStatus(SeatStatus.PAYING);
            UUID concertSeatId = concertSeat.getId();
            selectedSeats.add(concertSeatId);
            log.info("Seats are Selected");
          }
        });
    return selectedSeats;
  }

  public List<UUID> selectConcertSeats2(List<UUID> concertIds) {
    List<UUID> selectedSeats = new ArrayList<>();
    for (UUID concertId : concertIds) {
      System.out.println("여기  = " + concertId);
    }
    concertIds.forEach(id ->{
      log.info("select 1 ConcertSeatId : {}", id);
      if(redisSeatService.selectSeat(id)){
        log.info("select 2 ConcertSeatId : {}", id);
        selectedSeats.add(id);
        ConcertSeat concertSeat =
                concertSeatRepository
                    .findById(id)
                    .orElseThrow(() -> new QueueTicketException(ConcertSeatErrorCode.NOT_FOUND));
            concertSeat.changeStatus(SeatStatus.PAYING);
      }
    });
    return selectedSeats;
  }
}
