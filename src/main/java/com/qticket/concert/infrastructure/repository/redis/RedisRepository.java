package com.qticket.concert.infrastructure.repository.redis;

import com.qticket.concert.domain.concert.model.Concert;
import com.qticket.concert.domain.concertSeat.model.ConcertSeat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisRepository {

  private final RedisTemplate<String, String> redisTemplate;
  private final RedisTemplate<String, Integer> redisIntegerTemplate;
  private final String CONCERT_SEAT_PREFIX = "concertSeat:";

  public void save(ConcertSeat concertSeat, Concert concert){
    Duration duration = getTTl(concert.getConcertStartTime());
    log.info("Redis Repo save -> concertSeatId : {}", concertSeat.getId());
    redisIntegerTemplate.opsForValue()
        .set(CONCERT_SEAT_PREFIX + concertSeat.getId(), 1, duration);
}

  private Duration getTTl(LocalDateTime concertStartTime) {
    LocalDateTime ttlTime = concertStartTime.minusHours(1);
    return Duration.between(LocalDateTime.now(), ttlTime);
  }

  public Integer getSeatFlagById(UUID concertSeatId) {
    return redisIntegerTemplate.opsForValue()
        .get(CONCERT_SEAT_PREFIX + concertSeatId);
  }

  public Long selectSeats(UUID concertSeatId) {
    return redisIntegerTemplate.opsForValue()
        .decrement(CONCERT_SEAT_PREFIX + concertSeatId);
  }

  public void cancelSelect(UUID concertSeatId) {
    redisIntegerTemplate.opsForValue()
        .increment(CONCERT_SEAT_PREFIX + concertSeatId);
  }
}

