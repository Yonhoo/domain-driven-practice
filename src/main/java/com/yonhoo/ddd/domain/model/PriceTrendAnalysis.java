package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * 价格趋势分析值对象
 * 分析一段时间内的价格变化趋势
 */
public class PriceTrendAnalysis {
    private List<DailyPriceInfo> dailyPrices;
    private BigDecimal lowestPrice;
    private BigDecimal highestPrice;
    private LocalDate bestDealDate;

    public PriceTrendAnalysis(List<DailyPriceInfo> dailyPrices) {
        this.dailyPrices = dailyPrices;
        calculateStatistics();
    }

    private void calculateStatistics() {
        if (dailyPrices.isEmpty()) return;

        this.lowestPrice = dailyPrices.stream()
                .map(DailyPriceInfo::getFinalPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        this.highestPrice = dailyPrices.stream()
                .map(DailyPriceInfo::getFinalPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        this.bestDealDate = dailyPrices.stream()
                .min(Comparator.comparing(DailyPriceInfo::getFinalPrice))
                .map(DailyPriceInfo::getDate)
                .orElse(null);
    }

    // Getters
    public List<DailyPriceInfo> getDailyPrices() { return dailyPrices; }
    public BigDecimal getLowestPrice() { return lowestPrice; }
    public BigDecimal getHighestPrice() { return highestPrice; }
    public LocalDate getBestDealDate() { return bestDealDate; }
}