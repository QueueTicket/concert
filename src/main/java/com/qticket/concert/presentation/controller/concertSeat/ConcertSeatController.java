package com.qticket.concert.presentation.controller.concertSeat;

import com.qticket.concert.application.service.concertSeat.ConcertSeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concert-seats")
@Slf4j
public class ConcertSeatController {
  private final ConcertSeatService concertSeatService;
}
