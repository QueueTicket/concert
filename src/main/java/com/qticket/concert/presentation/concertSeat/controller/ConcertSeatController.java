package com.qticket.concert.presentation.concertSeat.controller;

import com.qticket.concert.application.service.concertSeat.ConcertSeatService;
import com.qticket.concert.domain.concertSeat.model.ConcertSeat;
import com.qticket.concert.presentation.concertSeat.dto.request.UpdateConcertSeatRequest;
import com.qticket.concert.presentation.concertSeat.dto.response.ConcertSeatResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concert-seats")
@Slf4j
public class ConcertSeatController {
  private final ConcertSeatService concertSeatService;

  // 공연별 공연좌석 조회
  // 좌석은 보통 한 페이지에 다 보여주는 것 같아서 페이징 처리 X
  @GetMapping("/concert/{concertId}")
  @ResponseStatus(HttpStatus.OK)
  public List<ConcertSeatResponse> getConcertSeat(@PathVariable UUID concertId) {
    List<ConcertSeat> concertSeats = concertSeatService.findByConcertId(concertId);
    log.info("ConcertSeats with concertId: {}, seatsCount : {}", concertId, concertSeats.size());
    return concertSeats.stream()
        .map(ConcertSeatResponse::fromEntity).toList();
  }

  @GetMapping("/{concertSeatId}")
  @ResponseStatus(HttpStatus.OK)
  public ConcertSeatResponse getOneConcertSeat(@PathVariable UUID concertSeatId) {
    return concertSeatService.getOneConcertSeat(concertSeatId);
  }

  // 좌석 상태 수정
  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ConcertSeatResponse> changeStatus(@RequestBody UpdateConcertSeatRequest request) {
    return concertSeatService.changeStatus(request);
  }
}
