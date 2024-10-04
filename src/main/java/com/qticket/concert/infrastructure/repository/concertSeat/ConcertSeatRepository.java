package com.qticket.concert.infrastructure.repository.concertSeat;

import com.qticket.concert.domain.concertSeat.model.ConcertSeat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, UUID> {

  @Query("select cs from ConcertSeat cs where cs.price.concert.id = :id")
  List<ConcertSeat> findByConcertId(@Param("id")UUID id);

  // 쿼리가 너무 많이 나가서 bulk성 쿼리로 전환
  @Modifying
  @Query("update ConcertSeat cs set cs.isDelete = true where cs.price.concert.id = :concertId")
  int deleteWithConcert(UUID concertId);

  @Query("select cs from ConcertSeat cs where cs.seat.id = :seatId")
  Optional<ConcertSeat> findBySeatId(UUID seatId);
}
