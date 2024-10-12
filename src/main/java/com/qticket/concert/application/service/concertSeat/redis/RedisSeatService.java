package com.qticket.concert.application.service.concertSeat.redis;

import com.esotericsoftware.minlog.Log;
import com.qticket.common.exception.QueueTicketException;
import com.qticket.concert.exception.concertSeat.ConcertSeatErrorCode;
import com.qticket.concert.infrastructure.repository.redis.RedisRepository;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSeatService {

  private final RedissonClient redissonClient;
  private final RedisRepository redisRepository;
  private final String CONCERT_SEAT_PREFIX = "concertSeat:";

  public boolean selectSeat(UUID seatId) {
    String redisKey = CONCERT_SEAT_PREFIX + seatId;
    String lockKey = "lock:" + redisKey;  // 락을 위한 별도 키 생성
    RLock lock = redissonClient.getLock(lockKey);  // 좌석별로 고유한 락 생성

    try {
      // 락 획득 시도 (10초 동안 시도, 5초 후 자동 해제)
      if (lock.tryLock(5, 2, TimeUnit.SECONDS)) {
        // 좌석을 확인하고 선택 가능한지 확인
        Integer seatStatus = redisRepository.getSeatFlagById(seatId);
        log.info("seatStatus : {}", seatStatus);
        if (seatStatus != null && seatStatus == 1) {
          // 좌석 선택 성공, 상태 변경
          redisRepository.selectSeats(seatId);
          return true;
        } else {
          return false;  // 이미 선택된 좌석
        }
      } else {
        throw new QueueTicketException(ConcertSeatErrorCode.PREEMPTED);
      }
    } catch (InterruptedException e) {
      throw new QueueTicketException(ConcertSeatErrorCode.PREEMPTED);
    } finally {
      // 락 해제
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }
}
