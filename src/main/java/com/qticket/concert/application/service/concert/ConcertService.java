package com.qticket.concert.application.service.concert;

import com.qticket.concert.infrastructure.repository.concert.ConcertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcertService {

  private final ConcertRepository concertRepository;
}
