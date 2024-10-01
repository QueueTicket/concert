package com.qticket.concert.presentation.concert.controller;

import com.qticket.concert.application.service.concert.ConcertService;
import com.qticket.concert.presentation.concert.dto.requset.CreateConcertRequest;
import com.qticket.concert.presentation.concert.dto.requset.UpdateConcertRequest;
import com.qticket.concert.presentation.concert.dto.response.ConcertResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}
