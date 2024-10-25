package com.qticket.concert.presentation.queue.controller;

import com.qticket.concert.application.service.queue.KafkaMessageStore;
import com.qticket.concert.application.service.queue.UserQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class WaitingRoomController {
    private final UserQueueService userQueueService;
    private final KafkaMessageStore messageStore;

    @ModelAttribute("queue")
    public String queue(@RequestParam(name = "queue", defaultValue = "default") String queue) {
        return queue;
    }

    @ModelAttribute("userId")
    public Long userId(@RequestParam(name = "user_id") Long userId) {
        return userId;
    }

    @GetMapping("/waiting-room")
    public Mono<String> waitingRoomPage(@RequestParam(name = "queue", defaultValue = "default") String queue,
                                        @RequestParam(name = "user_id") Long userId,
                                        Model model) {
        //String redirectUrl = messageStore.getRedirectUrl();
        String redirectUrl = "http://localhost:5173/performance/ad232c80-596a-4df2-b6ad-c487de534a90/order"; // test
        return userQueueService.isAllowed(queue, userId)
                .filter(allowed -> allowed)
                .flatMap(allowed -> {
                    return Mono.just("redirect:" + redirectUrl);
                })
                .switchIfEmpty(userQueueService.registerWaitQueue(queue, userId)
                        .onErrorResume(ex -> userQueueService.getRank(queue, userId))
                        .map(rank -> {
                            model.addAttribute("number", rank);
                            return "waiting-room";
                        }));
    }
}
