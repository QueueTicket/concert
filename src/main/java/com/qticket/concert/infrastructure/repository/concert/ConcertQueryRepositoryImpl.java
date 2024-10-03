package com.qticket.concert.infrastructure.repository.concert;

import static com.qticket.concert.domain.concert.model.QConcert.*;

import com.qticket.concert.application.service.concert.mapper.ConcertMapper;
import com.qticket.concert.domain.concert.model.Concert;
import com.qticket.concert.presentation.concert.dto.ConcertSearchCond;
import com.qticket.concert.presentation.concert.dto.response.ConcertResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ConcertQueryRepositoryImpl implements ConcertQueryRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public ConcertQueryRepositoryImpl(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page<ConcertResponse> searchConcert(Pageable page, ConcertSearchCond cond) {
    JPAQuery<Concert> common = queryFactory
        .selectFrom(concert)
        .where(
            concertTitleLike(cond.getConcertTitle()),
            concertStartTimePresentOrFuture(cond.getConcertStartTime())
        );
    List<Concert> list = common.offset(page.getOffset())
        .limit(page.getPageSize()).fetch();

    List<ConcertResponse> content = list.stream()
        .map(ConcertMapper::toConcertResponse).toList();
    return PageableExecutionUtils.getPage(content, page, common::fetchCount);
  }

  private BooleanExpression concertTitleLike(String concertTitle) {
    return StringUtils.hasText(concertTitle) ? concert.concertTitle.like("%" + concertTitle + "%") : null;
  }

  private BooleanExpression concertStartTimePresentOrFuture(LocalDateTime concertStartTime) {
    return concertStartTime != null ? concert.concertStartTime.goe(concertStartTime) : null;
  }
}
