package com.qticket.concert.infrastructure.repository.seat;

import com.qticket.concert.domain.model.Seat;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

}
