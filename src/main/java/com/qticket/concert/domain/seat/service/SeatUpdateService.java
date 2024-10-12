package com.qticket.concert.domain.seat.service;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.concert.application.service.concertSeat.ConcertSeatService;
import com.qticket.concert.domain.concert.model.Concert;
import com.qticket.concert.domain.concert.model.Price;
import com.qticket.concert.domain.concertSeat.model.ConcertSeat;
import com.qticket.concert.domain.seat.model.Seat;
import com.qticket.concert.domain.venue.Venue;
import com.qticket.concert.exception.price.PriceErrorCode;
import com.qticket.concert.presentation.seat.dto.request.UpdateSeatRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SeatUpdateService {

  private final ConcertSeatService concertSeatService;
  private final CacheManager cacheManager;

  public void updateSeatGradeAndPrice(Seat seat, UpdateSeatRequest request, Venue venue){

    Optional<ConcertSeat> concertSeatOptional = concertSeatService.getConcertSeatBySeat(seat.getId());
    if(concertSeatOptional.isPresent()){
      ConcertSeat concertSeat = concertSeatOptional.get();
      Concert concert = concertSeat.getPrice().getConcert();
    // 좌석이 등급 변경 시
      if (!seat.getSeatGrade().equals(request.getSeatGrade())) {
        Price updatePrice = concert.getPrices().stream()
            .filter(price -> price.getSeatGrade().equals(request.getSeatGrade()))
            .findFirst()
            .orElseThrow(() -> new QueueTicketException(PriceErrorCode.NOT_FOUND));
        log.info("updatePrice {}", updatePrice.getPrice());
        // 공연 좌석 가격 업데이트
        concertSeat.updateConcertSeat(null, updatePrice);
        int maxSeatNumber = getMaxSeatNumber(request, venue);
        seat.updateSeat(request.getSeatGrade(), maxSeatNumber + 1);
    } else {
      // 좌석 등급이 동일하면 좌석 번호만 업데이트
      seat.updateSeat(request.getSeatGrade(), request.getSeatNumber());
    }
    cacheManager.getCache("seatsForConcert").evictIfPresent(concert.getId());
    } else {
      log.info("공연에 할당 된 공연이 없습니다");
      if (!seat.getSeatGrade().equals(request.getSeatGrade())){
        int maxSeatNumber = getMaxSeatNumber(request, venue);
        seat.updateSeat(request.getSeatGrade(), maxSeatNumber + 1);
      } else {
        seat.updateSeat(request.getSeatGrade(), request.getSeatNumber());
      }
    }
  }

  private static int getMaxSeatNumber(UpdateSeatRequest r, Venue venue) {
    int maxNumber = venue.getSeats().stream()
        .filter(s -> s.getSeatGrade().equals(r.getSeatGrade()))
        .mapToInt(Seat::getSeatNumber)
        .max()
        .orElse(0);// 바꾸려는 등급 좌석이 없으면 0부터 시작
    log.info("venue with Id {} , have {} seats", venue.getId(), maxNumber);
    return maxNumber;
  }
}
