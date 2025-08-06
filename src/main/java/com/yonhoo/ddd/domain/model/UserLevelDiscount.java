package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 用户等级折扣实体
 */
public class UserLevelDiscount {
    private String discountId;
    private UserLevel targetLevel;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minOrderAmount;

    public boolean matchesLevel(UserLevel userLevel) {
        return this.targetLevel == userLevel;
    }

    public BigDecimal applyDiscount(BigDecimal originalPrice) {
        if (originalPrice.compareTo(minOrderAmount) < 0) {
            return originalPrice;
        }

        BigDecimal discountAmount;
        
        switch (discountType) {
            case PERCENTAGE:
                discountAmount = originalPrice.multiply(discountValue).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                if (maxDiscountAmount != null && discountAmount.compareTo(maxDiscountAmount) > 0) {
                    discountAmount = maxDiscountAmount;
                }
                break;
            case FIXED_AMOUNT:
                discountAmount = discountValue;
                break;
            default:
                return originalPrice;
        }

        BigDecimal finalPrice = originalPrice.subtract(discountAmount);
        return finalPrice.max(BigDecimal.ZERO);
    }

    // Getters and Setters
    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public UserLevel getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(UserLevel targetLevel) {
        this.targetLevel = targetLevel;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }
}

/**
 * 优先级规则值对象
 */
class PriorityRule {
    private boolean applyUserLevel;
    private boolean applyRegion;
    private boolean applyChannel;
    private int userLevelPriority;
    private int regionPriority;
    private int channelPriority;

    public boolean shouldApplyUserLevel() {
        return applyUserLevel;
    }

    public boolean shouldApplyRegion() {
        return applyRegion;
    }

    public boolean shouldApplyChannel() {
        return applyChannel;
    }

    // Getters and setters
    public void setApplyUserLevel(boolean applyUserLevel) {
        this.applyUserLevel = applyUserLevel;
    }

    public void setApplyRegion(boolean applyRegion) {
        this.applyRegion = applyRegion;
    }

    public void setApplyChannel(boolean applyChannel) {
        this.applyChannel = applyChannel;
    }

    public int getUserLevelPriority() {
        return userLevelPriority;
    }

    public void setUserLevelPriority(int userLevelPriority) {
        this.userLevelPriority = userLevelPriority;
    }

    public int getRegionPriority() {
        return regionPriority;
    }

    public void setRegionPriority(int regionPriority) {
        this.regionPriority = regionPriority;
    }

    public int getChannelPriority() {
        return channelPriority;
    }

    public void setChannelPriority(int channelPriority) {
        this.channelPriority = channelPriority;
    }
}

/**
 * 地域定价实体
 */
class RegionPricing {
    private String pricingId;
    private Region targetRegion;
    private PricingAdjustmentType adjustmentType;
    private BigDecimal adjustmentValue;

    public boolean matchesRegion(Region region) {
        return this.targetRegion == region;
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

    // Getters and setters omitted for brevity
}

/**
 * 渠道定价实体
 */
class ChannelPricing {
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

    // Getters and setters omitted for brevity
}

enum PricingAdjustmentType {
    MARKUP, DISCOUNT, FIXED_PRICE
} 