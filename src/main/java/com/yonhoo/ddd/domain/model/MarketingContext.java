package com.yonhoo.ddd.domain.model;

import java.time.LocalDateTime;

/**
 * 营销上下文值对象
 * 包含营销活动所需的上下文信息
 */
public class MarketingContext {
    private final LocalDateTime currentTime;
    private final String sessionId;
    private final int requestedQuantity;
    private final String sourceSystem;

    public MarketingContext(LocalDateTime currentTime, String sessionId, int requestedQuantity, String sourceSystem) {
        this.currentTime = currentTime;
        this.sessionId = sessionId;
        this.requestedQuantity = requestedQuantity;
        this.sourceSystem = sourceSystem;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }
} 