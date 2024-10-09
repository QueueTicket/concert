package com.qticket.concert.application.service.concert.mapper;

import com.qticket.concert.domain.concert.model.Concert;
import com.qticket.concert.domain.concert.model.Price;
import com.qticket.concert.domain.venue.Venue;
import com.qticket.concert.presentation.concert.dto.requset.CreateConcertRequest;
import com.qticket.concert.presentation.concert.dto.response.ConcertResponse;
import com.qticket.concert.presentation.concert.dto.response.PriceResponse;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

public class ConcertMapper {

  public static Concert requestToConcert(CreateConcertRequest request, Venue venue){
    return Concert.builder()
        .concertTitle(request.getConcertTitle())
        .venue(venue)
        .description(request.getDescription())
        .playTime(request.getPlaytime())
        .concertStartTime(request.getConcertStartTime())
        .prices(new ArrayList<>())
        .build();
  }

  public static ConcertResponse toConcertResponse(Concert concert){
    ConcertResponse response = new ConcertResponse();
    response.setConcertTitle(concert.getConcertTitle());
    response.setDescription(concert.getDescription());
    response.setPlaytime(concert.getPlayTime());
    response.setConcertId(concert.getId());
    response.setVenueId(concert.getVenue().getId());
    response.setConcertStartTime(concert.getConcertStartTime());
    response.setPrices(concert.getPrices().stream()
        .map(ConcertMapper::toPriceResponse).toList());
    return response;
  }

  public static PriceResponse toPriceResponse(Price price){
    PriceResponse response = new PriceResponse();
    response.setPrice(price.getPrice());
    response.setSeatGrade(price.getSeatGrade());
    return response;
  }
}


