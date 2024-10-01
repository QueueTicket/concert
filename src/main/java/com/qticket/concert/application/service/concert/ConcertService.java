package com.qticket.concert.application.service.concert;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.concert.application.service.concert.mapper.ConcertMapper;
import com.qticket.concert.domain.concert.model.Concert;
import com.qticket.concert.domain.concert.model.Price;
import com.qticket.concert.exception.concert.ConcertErrorCode;
import com.qticket.concert.infrastructure.repository.concert.ConcertRepository;
import com.qticket.concert.presentation.concert.dto.requset.CreateConcertRequest;
import com.qticket.concert.presentation.concert.dto.requset.UpdateConcertRequest;
import com.qticket.concert.presentation.concert.dto.response.ConcertResponse;
import com.qticket.concert.presentation.concert.dto.requset.PriceRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ConcertService {

  private final ConcertRepository concertRepository;

  public ConcertResponse createConcert(CreateConcertRequest request) {
    Concert concert = ConcertMapper.requestToConcert(request);

    for (PriceRequest priceRequest : request.getPrices()) {
      Price price =
          Price.builder()
              .seatGrade(priceRequest.getSeatGrade())
              .price(priceRequest.getPrice())
              .build();
      price.addConcert(concert);
    }

    Concert savedConcert = concertRepository.save(concert);
    return ConcertMapper.toConcertResponse(savedConcert);
  }

  public ConcertResponse updateConcert(UpdateConcertRequest request, UUID concertId) {
    Concert concert =
        concertRepository
            .findById(concertId)
            .orElseThrow(() -> new QueueTicketException(ConcertErrorCode.NOT_FOUND));

    concert.update(request);
    return ConcertMapper.toConcertResponse(concert);
  }
}
