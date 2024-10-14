package com.qticket.concert.presentation.concert.controller;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.common.login.CurrentUser;
import com.qticket.common.login.Login;
import com.qticket.concert.application.service.concert.ConcertService;
import com.qticket.concert.exception.concert.ConcertErrorCode;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ConcertResponse createConcert(@RequestBody CreateConcertRequest request, @Login CurrentUser currentUser) {
    if(isCustomer(currentUser.getCurrentUserRole())){
      throw new QueueTicketException(ConcertErrorCode.UNAUTHORIZED);
    }

    return concertService.createConcert(request);
  }

  @PutMapping("/{concertId}")
  @ResponseStatus(HttpStatus.OK)
  public ConcertResponse updateConcert(
      @RequestBody UpdateConcertRequest request, @PathVariable UUID concertId, @Login CurrentUser currentUser) {
    if(isCustomer(currentUser.getCurrentUserRole())){
      throw new QueueTicketException(ConcertErrorCode.UNAUTHORIZED);
    }

    return concertService.updateConcert(request, concertId);
  }

  @DeleteMapping("/{concertId}")
  public ResponseEntity<String> deleteConcert(@PathVariable UUID concertId, @Login CurrentUser currentUser) {
    if(isCustomer(currentUser.getCurrentUserRole())){
      throw new QueueTicketException(ConcertErrorCode.UNAUTHORIZED);
    }
    // 임시 설정
    Long username = currentUser.getCurrentUserId();
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
    return concertService.getOneConcert(concertId);
}

  private boolean isCustomer(String userRole) {
    return "CUSTOMER".equals(userRole);
  }
}
