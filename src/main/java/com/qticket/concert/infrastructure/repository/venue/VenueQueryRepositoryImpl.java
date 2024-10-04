package com.qticket.concert.infrastructure.repository.venue;

import static com.qticket.concert.domain.venue.QVenue.*;

import com.qticket.concert.application.service.venue.mapper.VenueMapper;
import com.qticket.concert.domain.venue.Venue;
import com.qticket.concert.presentation.venue.VenueSearchCond;
import com.qticket.concert.presentation.venue.dto.response.VenueResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class VenueQueryRepositoryImpl implements VenueQueryRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public VenueQueryRepositoryImpl(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<VenueResponse> searchVenue(Pageable pageable, VenueSearchCond cond) {
    JPAQuery<Venue> common = queryFactory
        .selectFrom(venue)
        .where(
            venueNameLike(cond.getVenueName()),
            venueAddressLike(cond.getVenueAddress()),
            venueSeatCapacity(cond.getSeatCapacity())
        );

    List<Venue> list = common.offset(pageable.getOffset())
        .limit(pageable.getPageSize()).fetch();

    List<VenueResponse> content = list.stream()
        .map(VenueMapper::toResponse).toList();
    return PageableExecutionUtils.getPage(content, pageable, common::fetchCount);
  }

  private BooleanExpression venueNameLike(String venueName) {
    return StringUtils.hasText(venueName) ? venue.venueName.like("%" + venueName + "%") : null;
  }

  private BooleanExpression venueAddressLike(String venueAddress) {
    return StringUtils.hasText(venueAddress) ? venue.venueAddress.like("%" + venueAddress + "%") : null;
  }

  private BooleanExpression venueSeatCapacity(Integer seatCapacity) {
    return seatCapacity != null ? venue.seatCapacity.goe(seatCapacity) : null;
  }
}
