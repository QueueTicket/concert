package com.qticket.concert.infrastructure.repository.venue;

import com.qticket.concert.presentation.venue.VenueSearchCond;
import com.qticket.concert.presentation.venue.dto.response.VenueResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VenueQueryRepository {

  Page<VenueResponse> searchVenue(Pageable pageable, VenueSearchCond cond);
}
