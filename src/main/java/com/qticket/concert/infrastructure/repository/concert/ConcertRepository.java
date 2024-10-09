package com.qticket.concert.infrastructure.repository.concert;

import com.qticket.concert.domain.concert.model.Concert;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, UUID>, ConcertQueryRepository {

}
