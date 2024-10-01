package com.qticket.concert.application.service.concertSeat;

import com.qticket.concert.infrastructure.repository.concertSeat.ConcertSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcertSeatService {
  private final ConcertSeatRepository concertSeatRepository;
}
