package com.qticket.concert.service;

import com.qticket.concert.application.service.concert.ConcertService;
import com.qticket.concert.config.EmbeddedRedis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@SpringBootTest
@Import({EmbeddedRedis.class})
@ActiveProfiles("test")
class UserQueueServiceTest {

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @BeforeEach
    public void beforeEach() {
        ReactiveRedisConnection reactiveConnection = reactiveRedisTemplate.getConnectionFactory().getReactiveConnection();
        reactiveConnection.serverCommands().flushAll().subscribe();
    }

    @Test
    void registerWaitQueue() {
        StepVerifier.create(concertService.registerWaitQueue(100L))
                .expectNext(1L)
                .verifyComplete();
        StepVerifier.create(concertService.registerWaitQueue(101L))
                .expectNext(2L)
                .verifyComplete();
        StepVerifier.create(concertService.registerWaitQueue(102L))
                .expectNext(3L)
                .verifyComplete();
    }

//    @Test
//    void alreadyRegisterWaitQueue() {
//        StepVerifier.create(concertService.registerWaitQueue(100L))
//                .expectNext(1L)
//                .verifyComplete();
//        StepVerifier.create(concertService.registerWaitQueue(100L))
//                .expectError(RuntimeException.class)
//                .verify();
//    }

    @Test
    void emptyAllowUser() {
        StepVerifier.create(concertService.allowUser(3L))
                .expectNext(0L)
                .verifyComplete();
    }

    @Test
    void allowUser() {
        StepVerifier.create(concertService.registerWaitQueue(100L)
                        .then(concertService.registerWaitQueue(101L))
                        .then(concertService.registerWaitQueue(102L))
                        .then(concertService.allowUser(2L)))
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    void allowUser2() {
        StepVerifier.create(concertService.registerWaitQueue(100L)
                        .then(concertService.registerWaitQueue(101L))
                        .then(concertService.registerWaitQueue(102L))
                        .then(concertService.allowUser(5L)))
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    void allowUserAfterRegisterWaitQueue() {
        StepVerifier.create(concertService.registerWaitQueue(100L)
                        .then(concertService.registerWaitQueue(101L))
                        .then(concertService.registerWaitQueue(102L))
                        .then(concertService.allowUser(3L))
                        .then(concertService.registerWaitQueue(200L)))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void isNotAllowed() {
        StepVerifier.create(concertService.isAllowed(100L))
                .expectNext(false)
                .verifyComplete();
    }
    @Test
    void isNotAllowed2() {
        StepVerifier.create(concertService.registerWaitQueue(100L)
                        .then(concertService.allowUser(3L))
                        .then(concertService.isAllowed(101L)))
                .expectNext(false)
                .verifyComplete();
    }
    @Test
    void isAllowed() {
        StepVerifier.create(concertService.registerWaitQueue(100L)
                        .then(concertService.allowUser(3L))
                        .then(concertService.isAllowed(100L)))
                .expectNext(true)
                .verifyComplete();
    }
}