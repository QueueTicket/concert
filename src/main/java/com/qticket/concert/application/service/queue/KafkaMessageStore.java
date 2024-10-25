package com.qticket.concert.application.service.queue;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageStore {
    private String redirectUrl = "https://www.google.com";  // 기본 URL

    public synchronized void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public synchronized String getRedirectUrl() {
        return this.redirectUrl;
    }
}

