package com.qticket.concert.infrastructure.repository.concert;

import com.qticket.concert.presentation.concert.dto.ConcertSearchCond;
import com.qticket.concert.presentation.concert.dto.response.ConcertResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConcertQueryRepository {

  Page<ConcertResponse> searchConcert(Pageable page, ConcertSearchCond cond);
}
