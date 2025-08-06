package com.yonhoo.ddd.domain.service;

import com.yonhoo.ddd.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

/**
 * 综合定价领域服务
 * 职责：协调 HotelOffer 基础价格、用户策略定价、营销策略定价，计算最终价格
 */
public class ComprehensivePricingDomainService {

    /**
     * 计算综合最终价格
     * 
     * @param hotelOffer 酒店产品聚合根
     * @param checkInDay 入住日期
     * @param roomPriceData 外部价格数据
     * @param userContext 用户上下文
     * @param marketingContext 营销上下文
     * @param userPricingStrategies 用户定价策略列表
     * @param marketingPricingStrategies 营销定价策略列表
     * @return 最终价格结果
     */
    public static PricingResult calculateFinalPrice(
            HotelOffer hotelOffer,
            LocalDate checkInDay,
            Map<String, ? extends AbstractPriceData> roomPriceData,
            UserContext userContext,
            MarketingContext marketingContext,
            List<UserPricingStrategy> userPricingStrategies,
            List<MarketingPricingStrategy> marketingPricingStrategies) {

        // 1. 计算基础价格 (HotelOffer)
        BigDecimal basePrice = HotelPricingDomainService.calculateMinPrice(
            hotelOffer, checkInDay, roomPriceData);

        // 2. 应用用户策略定价
        BigDecimal userDiscountedPrice = applyUserPricingStrategies(
            basePrice, userContext, userPricingStrategies);

        // 3. 应用营销策略定价
        BigDecimal marketingPrice = applyMarketingPricingStrategies(
            userDiscountedPrice, checkInDay, marketingContext, marketingPricingStrategies);

        // 4. 构建定价结果
        return buildPricingResult(basePrice, userDiscountedPrice, marketingPrice, 
            checkInDay, userContext, marketingContext);
    }

    /**
     * 应用用户定价策略
     * 使用策略选择器来管理多策略选择逻辑
     */
    private static BigDecimal applyUserPricingStrategies(
            BigDecimal basePrice, 
            UserContext userContext, 
            List<UserPricingStrategy> strategies) {

        // 使用策略选择器，默认选择最优价格策略
        return UserPricingStrategySelector.calculateBestUserPrice(
                basePrice, 
                userContext, 
                strategies, 
                UserPricingStrategySelector.SelectionMode.BEST_PRICE
        );
    }

    /**
     * 应用营销定价策略
     */
    private static BigDecimal applyMarketingPricingStrategies(
            BigDecimal userDiscountedPrice,
            LocalDate checkInDay,
            MarketingContext marketingContext,
            List<MarketingPricingStrategy> strategies) {

        if (strategies == null || strategies.isEmpty()) {
            return userDiscountedPrice;
        }

        // 按优先级排序策略
        strategies.sort((s1, s2) -> Integer.compare(s2.getPriorityLevel().getLevel(), s1.getPriorityLevel().getLevel()));

        BigDecimal bestMarketingPrice = userDiscountedPrice;

        // 应用营销策略，选择最优价格
        for (MarketingPricingStrategy strategy : strategies) {
            if (strategy.isEffective(checkInDay)) {
                BigDecimal strategyPrice = strategy.calculateMarketingPrice(
                    userDiscountedPrice, checkInDay, marketingContext);
                
                if (strategyPrice.compareTo(bestMarketingPrice) < 0) {
                    bestMarketingPrice = strategyPrice;
                }
            }
        }

        return bestMarketingPrice;
    }

    /**
     * 构建定价结果
     */
    private static PricingResult buildPricingResult(
            BigDecimal basePrice,
            BigDecimal userDiscountedPrice, 
            BigDecimal finalPrice,
            LocalDate checkInDay,
            UserContext userContext,
            MarketingContext marketingContext) {

        PricingResult result = new PricingResult();
        result.setBasePrice(basePrice);
        result.setUserDiscountedPrice(userDiscountedPrice);
        result.setFinalPrice(finalPrice);
        
        // 计算折扣金额
        result.setUserDiscountAmount(basePrice.subtract(userDiscountedPrice));
        result.setMarketingDiscountAmount(userDiscountedPrice.subtract(finalPrice));
        result.setTotalDiscountAmount(basePrice.subtract(finalPrice));
        
        // 计算折扣率
        if (basePrice.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountRate = result.getTotalDiscountAmount()
                    .divide(basePrice, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            result.setDiscountRate(discountRate);
        }

        result.setCheckInDay(checkInDay);
        result.setUserId(userContext.getUserId());
        result.setUserLevel(userContext.getUserLevel());
        result.setCalculationTime(marketingContext.getCurrentTime());

        return result;
    }

    /**
     * 检查价格变化趋势（用于动态定价建议）
     */
    public static PriceTrendAnalysis analyzePriceTrend(
            HotelOffer hotelOffer,
            DateRange dateRange,
            UserContext userContext,
            List<UserPricingStrategy> userStrategies,
            List<MarketingPricingStrategy> marketingStrategies) {

        List<DailyPriceInfo> dailyPrices = new ArrayList<>();
        
        dateRange.toStream().forEach(date -> {
            // 这里需要模拟获取每日价格数据的逻辑
            // 在实际应用中，这可能需要调用外部服务
            MarketingContext context = new MarketingContext(
                date.atStartOfDay(), 
                "trend-analysis", 
                1, 
                "PRICE_ANALYSIS_SYSTEM"
            );
            
            // 简化示例：使用固定的价格数据进行趋势分析
            // 实际场景中需要获取真实的每日价格数据
        });

        return new PriceTrendAnalysis(dailyPrices);
    }
}

 