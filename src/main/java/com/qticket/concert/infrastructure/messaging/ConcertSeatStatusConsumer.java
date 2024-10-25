//package com.qticket.concert.infrastructure.messaging;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.qticket.concert.application.service.concertSeat.ConcertSeatService;
//import com.qticket.concert.domain.concertSeat.model.SeatStatus;
//import com.qticket.concert.presentation.concertSeat.dto.request.UpdateConcertSeatRequest;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.UUID;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//@Slf4j(topic = "ConcertSeatStatusConsumer in ConcertServer")
//public class ConcertSeatStatusConsumer {
//  private final ConcertSeatService concertSeatService;
//  private final KafkaTemplate<String, String> kafkaTemplate;
//
//  @KafkaListener(topics = "order_complete", groupId = "change-status")
//  public void changeStatusReserved(
//      @Header(name = "kafka_receivedMessageKey") String key,
//      @Payload List<LinkedHashMap<String, Object>> payload
//  ){
//    List<UUID> list = getConcertSeatIds(key, payload);
//    UpdateConcertSeatRequest request = new UpdateConcertSeatRequest();
//    request.setConcertSeatIds(list);
//    request.setStatus(SeatStatus.RESERVED);
//    try{
//    concertSeatService.changeStatus(request);
//    } catch (Exception e) {
//      kafkaTemplate.send("error_in_change_status", e.getMessage());
//      log.error("Error occurred while change status : {}", e.getMessage());
//    }
//  }
//
//  private List<UUID> getConcertSeatIds
//      (String key, List<LinkedHashMap<String, Object>> payload) {
//    // 키로 하나씩 온다 ?
//    UUID concertSeatId = UUID.fromString(key);
//    List<UUID> list = new ArrayList<>();
//    list.add(concertSeatId);
//    // payload 에 담겨서 온다
//    List<UUID> ids = getIdsByPayload(payload);
//    list.addAll(ids);
//    return list;
//  }
//
//  // 데이터 어떻게 올 지 몰라 일단 구현
//  private List<UUID> getIdsByPayload(
//      List<LinkedHashMap<String, Object>> payload) {
//    return payload.stream()
//        .map(
//            map -> {
//              ObjectMapper mapper = new ObjectMapper();
//              return mapper.convertValue(map, UUID.class);
//            })
//        .toList();
//  }
//
//
//
//
//}
