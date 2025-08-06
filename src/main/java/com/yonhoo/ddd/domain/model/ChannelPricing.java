package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;

/**
 * 渠道定价实体
 * 管理基于渠道的价格调整策略
 */
public class ChannelPricing {
    private String pricingId;
    private Channel targetChannel;
    private PricingAdjustmentType adjustmentType;
    private BigDecimal adjustmentValue;

    public boolean matchesChannel(Channel channel) {
        return this.targetChannel == channel;
    }

    public BigDecimal adjustPrice(BigDecimal originalPrice) {
        switch (adjustmentType) {
            case MARKUP:
                return originalPrice.multiply(BigDecimal.ONE.add(adjustmentValue.divide(BigDecimal.valueOf(100))));
            case DISCOUNT:
                return originalPrice.multiply(BigDecimal.ONE.subtract(adjustmentValue.divide(BigDecimal.valueOf(100))));
            case FIXED_PRICE:
                return adjustmentValue;
            default:
                return originalPrice;
        }
    }

    // Getters and setters
    public String getPricingId() {
        return pricingId;
    }

    public void setPricingId(String pricingId) {
        this.pricingId = pricingId;
    }

    public Channel getTargetChannel() {
        return targetChannel;
    }

    public void setTargetChannel(Channel targetChannel) {
        this.targetChannel = targetChannel;
    }

    public PricingAdjustmentType getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(PricingAdjustmentType adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public BigDecimal getAdjustmentValue() {
        return adjustmentValue;
    }

    public void setAdjustmentValue(BigDecimal adjustmentValue) {
        this.adjustmentValue = adjustmentValue;
    }
}