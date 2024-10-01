package com.qticket.concert.presentation.controller.concert;

import com.qticket.concert.application.service.concert.ConcertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts")
@Slf4j
public class ConcertController {

  private final ConcertService concertService;




}
