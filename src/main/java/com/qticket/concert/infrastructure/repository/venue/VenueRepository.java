package com.qticket.concert.infrastructure.repository.venue;

import com.qticket.concert.domain.venue.Venue;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, UUID> {

}
