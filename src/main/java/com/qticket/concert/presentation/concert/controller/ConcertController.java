package com.qticket.concert.presentation.concert.controller;

import com.qticket.common.dto.ResponseDto;
import com.qticket.common.login.Login;
import com.qticket.concert.application.service.concert.ConcertService;
import com.qticket.concert.presentation.concert.dto.ConcertSearchCond;
import com.qticket.concert.presentation.concert.dto.requset.CreateConcertRequest;
import com.qticket.concert.presentation.concert.dto.requset.UpdateConcertRequest;
import com.qticket.concert.presentation.concert.dto.response.ConcertResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts")
@Slf4j
public class ConcertController {

  private final ConcertService concertService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ConcertResponse createConcert(@RequestBody CreateConcertRequest request) {
    return concertService.createConcert(request);
  }

  @PutMapping("/{concertId}")
  @ResponseStatus(HttpStatus.OK)
  public ConcertResponse updateConcert(
      @RequestBody UpdateConcertRequest request, @PathVariable UUID concertId) {
    return concertService.updateConcert(request, concertId);
  }

  @DeleteMapping("/{concertId}")
  public ResponseEntity<String> deleteConcert(@PathVariable UUID concertId) {
    // 임시 설정
    String username = "admin";
    concertService.deleteConcert(concertId, username);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<ConcertResponse> getAllConcerts(Pageable pageable, ConcertSearchCond cond){
    return concertService.getAllConcerts(pageable, cond);
  }

  @GetMapping("/{concertId}")
  @ResponseStatus(HttpStatus.OK)
  public ConcertResponse getOneConcerts(@PathVariable UUID concertId){
    System.out.println("redirect 요청 !!!!!");
    return concertService.getOneConcert(concertId);
}

  @GetMapping("/test")
  public ResponseDto<?> test(@RequestHeader("X-USER-ROLE") String userRole) {
    return ResponseDto.success("UserRole : " + userRole);
  }
}
