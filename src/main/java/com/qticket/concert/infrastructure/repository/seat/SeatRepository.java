package com.qticket.concert.infrastructure.repository.seat;

import com.qticket.concert.domain.seat.model.Seat;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SeatRepository extends JpaRepository<Seat, UUID> {

  @Modifying
  @Query("UPDATE Seat s SET s.isDelete = true, s.deletedBy = :username WHERE s.venue = (SELECT v FROM Venue v WHERE v.id = :venueId)")
  void softDeleteWithVenueId(UUID venueId, Long username);
}
