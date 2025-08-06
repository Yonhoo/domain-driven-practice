package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 节假日定价实体
 */
public class HolidayPricing {
    private String holidayId;
    private String holidayName;
    private List<LocalDate> holidayDates;
    private DateRange holidayPeriod;
    private PricingAdjustmentType adjustmentType;
    private BigDecimal adjustmentValue;

    public boolean isApplicableDate(LocalDate targetDate) {
        return (holidayDates != null && holidayDates.contains(targetDate)) ||
               (holidayPeriod != null && holidayPeriod.contains(targetDate));
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
    public String getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(String holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }
}

/**
 * 限时活动实体
 */
class FlashSaleActivity {
    private String activityId;
    private String activityName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<LocalDate> applicableDates;
    private AtomicInteger totalQuota;
    private AtomicInteger usedQuota;
    private BigDecimal discountPercentage;
    private BigDecimal maxDiscountAmount;

    public boolean isActive(LocalDateTime currentTime, LocalDate targetDate) {
        return currentTime.isAfter(startTime) && 
               currentTime.isBefore(endTime) &&
               (applicableDates == null || applicableDates.contains(targetDate));
    }

    public boolean hasAvailableQuota() {
        return usedQuota.get() < totalQuota.get();
    }

    public BigDecimal calculateSalePrice(BigDecimal originalPrice) {
        BigDecimal discountAmount = originalPrice.multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100));
        
        if (maxDiscountAmount != null && discountAmount.compareTo(maxDiscountAmount) > 0) {
            discountAmount = maxDiscountAmount;
        }
        
        return originalPrice.subtract(discountAmount);
    }

    public boolean reserveQuota(int quantity) {
        while (true) {
            int currentUsed = usedQuota.get();
            int newUsed = currentUsed + quantity;
            
            if (newUsed > totalQuota.get()) {
                return false; // 库存不足
            }
            
            if (usedQuota.compareAndSet(currentUsed, newUsed)) {
                return true; // 预留成功
            }
            // CAS失败，重试
        }
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}

/**
 * 季节性定价实体
 */
class SeasonalPricing {
    private String seasonId;
    private String seasonName;
    private DateRange seasonPeriod;
    private PricingAdjustmentType adjustmentType;
    private BigDecimal adjustmentValue;

    public boolean isInSeason(LocalDate targetDate) {
        return seasonPeriod.contains(targetDate);
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