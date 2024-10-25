package com.qticket.concert.infrastructure.config;

import com.qticket.concert.application.service.queue.UserQueueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ConcertInterceptor implements HandlerInterceptor {

    private final UserQueueService userQueueService;

    @Value("${gateway.base.url}")
    private String gatewayBaseUrl;

    @Autowired
    public ConcertInterceptor(UserQueueService userQueueService) {
        this.userQueueService = userQueueService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        Long userId = Long.valueOf(request.getHeader("X-USER-ID"));

        if (requestURI.equals("/concerts") || requestURI.equals("/venues")) {
            return true;  // 해당 엔드포인트는 waiting-room을 거치지 않고 통과
        }

        // 대기열 체크 로직 수행
        boolean isAllowed = userQueueService.isAllowed("default", userId).block();

        if (isAllowed) {
            // 대기열 통과 시 요청을 계속 진행
            return true;
        } else {
            // 대기열 통과하지 못한 경우 대기실로 리다이렉트
            response.sendRedirect(gatewayBaseUrl + "/waiting-room?user_id=" + userId);
            return false;
        }
    }
}

