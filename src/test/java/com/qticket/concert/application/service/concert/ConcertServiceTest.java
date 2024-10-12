package com.qticket.concert.application.service.concert;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.qticket.common.exception.QueueTicketException;
import com.qticket.concert.application.service.concertSeat.ConcertSeatService;
import com.qticket.concert.application.service.venue.VenueService;
import com.qticket.concert.domain.concert.model.Concert;
import com.qticket.concert.domain.concertSeat.model.ConcertSeat;
import com.qticket.concert.domain.seat.model.SeatGrade;
import com.qticket.concert.exception.concertSeat.ConcertSeatErrorCode;
import com.qticket.concert.infrastructure.repository.concert.ConcertRepository;
import com.qticket.concert.presentation.concert.dto.requset.CreateConcertRequest;
import com.qticket.concert.presentation.concert.dto.requset.PriceRequest;
import com.qticket.concert.presentation.concert.dto.requset.UpdateConcertRequest;
import com.qticket.concert.presentation.concert.dto.response.ConcertResponse;
import com.qticket.concert.presentation.seat.dto.request.CreateSeatRequest;
import com.qticket.concert.presentation.seat.dto.response.SeatResponse;
import com.qticket.concert.presentation.venue.dto.request.CreateVenueRequest;
import com.qticket.concert.presentation.venue.dto.response.VenueResponse;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ConcertServiceTest {

  private static final Logger log = LoggerFactory.getLogger(ConcertServiceTest.class);
  @Autowired
  private ConcertService concertService;
  @Autowired
  private ConcertRepository concertRepository;
  @Autowired
  private VenueService venueService;
  private VenueResponse venue; // 클래스 필드로 선언
  private ConcertResponse concert;
  @Autowired
  private ConcertSeatService concertSeatService;
  @Autowired
  private CacheManager cacheManager;
  @Autowired
  private EntityManager em;

  @BeforeEach
  void setUp() {
    List<CreateSeatRequest> test = new ArrayList<>();
    CreateSeatRequest seat1 = new CreateSeatRequest();
    seat1.setSeatCount(100);
    seat1.setSeatGrade(SeatGrade.R);
    CreateSeatRequest seat2 = new CreateSeatRequest();
    seat2.setSeatCount(50);
    seat2.setSeatGrade(SeatGrade.S);
    CreateSeatRequest seat3 = new CreateSeatRequest();
    seat3.setSeatCount(50);
    seat3.setSeatGrade(SeatGrade.A);
    test.add(seat1);
    test.add(seat2);
    test.add(seat3);

    CreateVenueRequest request = new CreateVenueRequest();
    request.setVenueName("테스트 공연장");
    request.setSeatCapacity(200);
    request.setVenueAddress("테스트 주소");
    request.setSeats(test);
    venue = venueService.createVenue(request);

    UUID venueId = venue.getVenueId();

    PriceRequest price1 = new PriceRequest();
    price1.setSeatGrade(SeatGrade.R);
    price1.setPrice(180000);
    PriceRequest price2 = new PriceRequest();
    price2.setSeatGrade(SeatGrade.S);
    price2.setPrice(150000);
    PriceRequest price3 = new PriceRequest();
    price3.setSeatGrade(SeatGrade.A);
    price3.setPrice(120000);
    PriceRequest price4 = new PriceRequest();
    price4.setSeatGrade(SeatGrade.B);
    price4.setPrice(120000);

    List<PriceRequest> prices = new ArrayList<>();
    prices.add(price1);
    prices.add(price2);
    prices.add(price3);


    CreateConcertRequest req = new CreateConcertRequest();
    req.setVenueId(venueId);
    req.setConcertTitle("테스트 공연");
    req.setConcertStartTime(LocalDateTime.now().plusHours(3));
    req.setDescription("테스트 공연 설명");
    req.setPlaytime(100);
    req.setPrices(prices);
    //when
    concert = concertService.createConcert(req);
  }

  @Test
  void createConcertTest() throws Exception{
    UUID venueId = venue.getVenueId();
    List<ConcertSeat> concertSeats = concertSeatService.findByConcertId(concert.getConcertId());
    for (ConcertSeat concertSeat : concertSeats) {
      concertSeat.getSeat().getSeatGrade();
    }
    Cache cacheValue = cacheManager.getCache("concertAllCache");
    //then
    assertThat(concert.getConcertTitle()).isEqualTo("테스트 공연");
    assertThat(concert.getVenueId()).isEqualTo(venueId);
    assertThat(concert.getPlaytime()).isEqualTo(100);
    assertThat(concert.getPrices().size()).isEqualTo(3);
    assertThat(concert.getPrices().get(0).getPrice()).isEqualTo(180000);
    assertThat(concert.getPrices().get(0).getSeatGrade()).isEqualTo(SeatGrade.R);
    assertThat(concert.getPrices().get(1).getPrice()).isEqualTo(150000);
    assertThat(concert.getPrices().get(1).getSeatGrade()).isEqualTo(SeatGrade.S);
    assertThat(concert.getPrices().get(2).getPrice()).isEqualTo(120000);
    assertThat(concert.getPrices().get(2).getSeatGrade()).isEqualTo(SeatGrade.A);

    assertThat(concertSeats.size()).isEqualTo(200);
    assertThat(concertSeats.stream()
        .filter(cs -> cs.getSeat().getSeatGrade() == SeatGrade.R)
        .toList().size()).isEqualTo(100);
    assertThat(concertSeats.stream()
        .filter(cs -> cs.getSeat().getSeatGrade() == SeatGrade.S)
        .toList().size()).isEqualTo(50);
    assertThat(concertSeats.stream()
        .filter(cs -> cs.getSeat().getSeatGrade() == SeatGrade.A)
        .toList().size()).isEqualTo(50);
    assertThat(concertSeats.stream()
        .filter(cs -> cs.getSeat().getSeatGrade() == SeatGrade.R)
        .findFirst().get().getPrice().getPrice()).isEqualTo(180000);
    assertThat(concertSeats.stream()
        .filter(cs -> cs.getSeat().getSeatGrade() == SeatGrade.S)
        .findFirst().get().getPrice().getPrice()).isEqualTo(150000);
    assertThat(concertSeats.stream()
        .filter(cs -> cs.getSeat().getSeatGrade() == SeatGrade.A)
        .findFirst().get().getPrice().getPrice()).isEqualTo(120000);
    assertThatThrownBy(
        () -> concertSeats.stream()
            .filter(cs -> cs.getSeat().getSeatGrade() == SeatGrade.B)
            .findFirst()
            .orElseThrow(() -> new QueueTicketException(ConcertSeatErrorCode.NOT_FOUND)))
        .isInstanceOf(QueueTicketException.class);
  }

  @Test
  void updateConcert() throws Exception{
    //given
    UUID venueId = venue.getVenueId();

    PriceRequest updatePrice1 = new PriceRequest();
    updatePrice1.setSeatGrade(SeatGrade.R);
    updatePrice1.setPrice(190000);
    PriceRequest updatePrice2 = new PriceRequest();
    updatePrice2.setSeatGrade(SeatGrade.S);
    updatePrice2.setPrice(160000);
    PriceRequest updatePrice3 = new PriceRequest();
    updatePrice3.setSeatGrade(SeatGrade.A);
    updatePrice3.setPrice(130000);
    PriceRequest updatePrice4 = new PriceRequest();
    updatePrice4.setSeatGrade(SeatGrade.B);
    updatePrice4.setPrice(100000);

    List<PriceRequest> UpdatedPrices = new ArrayList<>();
    UpdatedPrices.add(updatePrice1);
    UpdatedPrices.add(updatePrice2);
    UpdatedPrices.add(updatePrice3);
    UpdatedPrices.add(updatePrice4);

    UpdateConcertRequest updateConcertRequest = new UpdateConcertRequest();
    updateConcertRequest.setConcertTitle("테스트 공연 수정");
    updateConcertRequest.setConcertStartTime(LocalDateTime.now());
    updateConcertRequest.setDescription("테스트 공연 설명 수정");
    updateConcertRequest.setPlaytime(150);
    updateConcertRequest.setPrices(UpdatedPrices);

    //when
    ConcertResponse updatedConcert = concertService.updateConcert(updateConcertRequest,
        concert.getConcertId());
    //then
    assertThat(updatedConcert.getConcertTitle()).isEqualTo("테스트 공연 수정");
    assertThat(updatedConcert.getDescription()).isEqualTo("테스트 공연 설명 수정");
    assertThat(updatedConcert.getPlaytime()).isEqualTo(150);
    assertThat(updatedConcert.getPrices().size()).isEqualTo(3);
    assertThat(updatedConcert.getPrices().get(0).getPrice()).isEqualTo(190000);
    assertThat(updatedConcert.getPrices().get(0).getSeatGrade()).isEqualTo(SeatGrade.R);
    assertThat(updatedConcert.getPrices().get(1).getPrice()).isEqualTo(160000);
    assertThat(updatedConcert.getPrices().get(1).getSeatGrade()).isEqualTo(SeatGrade.S);
    assertThat(updatedConcert.getPrices().get(2).getPrice()).isEqualTo(130000);
    assertThat(updatedConcert.getPrices().get(2).getSeatGrade()).isEqualTo(SeatGrade.A);
    assertThatThrownBy(() -> updatedConcert.getPrices().get(3)).isInstanceOf(Exception.class);

  }

  @Test
  void deleteConcertTest() throws Exception{
    //given
    Concert concert1 = concertRepository.findById(concert.getConcertId()).get();
    List<ConcertSeat> concertSeats = concertSeatService.findByConcertId(concert1.getId());
    //when
    concertService.deleteConcert(concert1.getId(), "test");
    em.flush();
    em.clear();
    List<ConcertSeat> after = concertSeatService.findByConcertId(concert1.getId());
    //then
    assertThat(concert1.isDelete()).isTrue();
    concert1.getPrices().forEach(p -> assertThat(p.isDelete()).isTrue());
    after.forEach(cs -> assertThat(cs.isDelete()).isTrue());
  }

  @Test
  void 동시성제어() throws Exception{
    SeatResponse response = venue.getSeats().get(0);
    UUID seatId = response.getSeatId();
    log.info("seatId : {}", seatId);
    List<ConcertSeat> concertSeats = concertSeatService.findByConcertId(concert.getConcertId());
    ConcertSeat concertSeat = concertSeats.get(0);

    List<UUID> concertIds = new ArrayList<>();
    concertIds.add(concertSeat.getId());

    // 동시성 제어를 위한 쓰레드 풀과 CountDownLatch 생성
    int threadCount = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(32);
    CountDownLatch latch = new CountDownLatch(threadCount);

    List<UUID> selectedSeatId = new ArrayList<>(); // 한 번만 성공해야 하므로 ID 하나만 저장
    int[] successCount = {0};  // 성공 횟수를 세기 위한 변수
    List<Exception> exceptionList = Collections.synchronizedList(new ArrayList<>());  // 예외를 기록할 리스트
    log.info("안되냐1");

    for (int i = 0; i < threadCount; i++) {
      executorService.submit(() -> {
        try {
          // 여기에 좌석 감소시키는 로직 실행
          log.info("뭐가 문젠데 도대체");
          List<UUID> uuids = concertSeatService.selectConcertSeats(concertIds);
          selectedSeatId.addAll(uuids);
          log.info("uuids.size : {}", uuids.size());
          System.out.println("");
        } catch (Exception e){
          exceptionList.add(e);
        }finally {
          latch.countDown();
        }
      });
    }

    // 모든 스레드가 완료될 때까지 대기
    latch.await();

    // 성공한 좌석이 하나인지 확인 (1개만 성공하고 나머지는 실패해야 함)
    assertThat(selectedSeatId.size()).isEqualTo(1);
    // 나머지 스레드에서 예외가 발생했는지 확인 (9개의 실패가 있어야 함)
//    assertThat(exceptionList.size()).isEqualTo(99);
//    assertThat(selectedSeatId.size()).isEqualTo(1);

    // ExecutorService 종료
    executorService.shutdown();
  }

}