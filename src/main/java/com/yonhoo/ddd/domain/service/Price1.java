package com.yonhoo.ddd.domain.service;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal apply(RoomType room, Price basePrice, PricingContext context);
}


public class UserGradePricingStrategy implements PricingStrategy {
    @Override
    public BigDecimal apply(RoomType room, Price basePrice, PricingContext context) {
        UserGrade grade = context.getUserProfile().getGrade();
        switch (grade) {
            case PLATINUM -> {
                return basePrice.amount().multiply(BigDecimal.valueOf(0.8));
            }
            case GOLD -> {
                return basePrice.amount().multiply(BigDecimal.valueOf(0.9));
            }
            default -> {
                return basePrice.amount();
            }
        }
    }
}
