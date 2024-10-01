package com.qticket.concert.presentation.controller.seat;

import com.qticket.concert.application.service.seat.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seats")
@Slf4j
public class SeatController {

  private final SeatService seatService;
}
