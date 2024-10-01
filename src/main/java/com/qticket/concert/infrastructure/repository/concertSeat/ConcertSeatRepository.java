package com.qticket.concert.infrastructure.repository.concertSeat;

import com.qticket.concert.domain.model.ConcertSeat;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSeatRepository extends JpaRepository<ConcertSeat, UUID> {

}
