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