package com.yonhoo.ddd.domain.service;

import com.yonhoo.ddd.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 用户定价策略选择器
 * 职责：管理多个用户定价策略的选择逻辑和优先级
 */
public class UserPricingStrategySelector {

    /**
     * 策略选择模式枚举
     */
    public enum SelectionMode {
        BEST_PRICE,        // 选择最优价格（价格最低）
        HIGHEST_PRIORITY,  // 选择最高优先级策略
        FIRST_APPLICABLE   // 选择第一个适用的策略
    }

    /**
     * 根据选择模式计算最终用户价格（当前时间）
     */
    public static BigDecimal calculateBestUserPrice(
            BigDecimal basePrice,
            UserContext userContext,
            List<UserPricingStrategy> strategies,
            SelectionMode mode) {
        return calculateBestUserPrice(basePrice, userContext, strategies, mode, LocalDateTime.now());
    }

    /**
     * 根据选择模式计算最终用户价格（指定时间）
     */
    public static BigDecimal calculateBestUserPrice(
            BigDecimal basePrice,
            UserContext userContext,
            List<UserPricingStrategy> strategies,
            SelectionMode mode,
            LocalDateTime checkTime) {

        if (strategies == null || strategies.isEmpty()) {
            return basePrice;
        }

        // 过滤出适用且在有效期内的策略
        List<UserPricingStrategy> applicableStrategies = strategies.stream()
                .filter(strategy -> strategy.isApplicableForUser(userContext, checkTime))
                .toList();

        if (applicableStrategies.isEmpty()) {
            return basePrice;
        }

        return switch (mode) {
            case BEST_PRICE -> selectByBestPrice(basePrice, userContext, applicableStrategies, checkTime);
            case HIGHEST_PRIORITY -> selectByHighestPriority(basePrice, userContext, applicableStrategies, checkTime);
            case FIRST_APPLICABLE -> selectFirstApplicable(basePrice, userContext, applicableStrategies, checkTime);
        };
    }

    /**
     * 选择最优价格策略（价格最低）
     */
    private static BigDecimal selectByBestPrice(
            BigDecimal basePrice,
            UserContext userContext,
            List<UserPricingStrategy> strategies,
            LocalDateTime checkTime) {

        BigDecimal bestPrice = basePrice;
        UserPricingStrategy bestStrategy = null;

        for (UserPricingStrategy strategy : strategies) {
            BigDecimal strategyPrice = strategy.calculateUserDiscount(basePrice, userContext, checkTime);
            if (strategyPrice.compareTo(bestPrice) < 0) {
                bestPrice = strategyPrice;
                bestStrategy = strategy;
            }
        }

        System.out.println("选择策略: " + (bestStrategy != null ? bestStrategy.getStrategyName() : "无")
                + ", 最终价格: " + bestPrice
                + (bestStrategy != null ? ", 剩余有效时间: " + bestStrategy.getRemainingValidHours() + "小时" : ""));
        return bestPrice;
    }

    /**
     * 选择最高优先级策略
     */
    private static BigDecimal selectByHighestPriority(
            BigDecimal basePrice,
            UserContext userContext,
            List<UserPricingStrategy> strategies,
            LocalDateTime checkTime) {

        Optional<UserPricingStrategy> highestPriorityStrategy = strategies.stream()
                .max(Comparator.comparing((UserPricingStrategy s) -> s.getStrategyPriority().getLevel()));

        if (highestPriorityStrategy.isPresent()) {
            UserPricingStrategy strategy = highestPriorityStrategy.get();
            BigDecimal finalPrice = strategy.calculateUserDiscount(basePrice, userContext, checkTime);
            System.out.println("选择最高优先级策略: " + strategy.getStrategyName()
                    + " (优先级: " + strategy.getStrategyPriority() + "), 最终价格: " + finalPrice
                    + ", 剩余有效时间: " + strategy.getRemainingValidHours() + "小时");
            return finalPrice;
        }

        return basePrice;
    }

    /**
     * 选择第一个适用的策略
     */
    private static BigDecimal selectFirstApplicable(
            BigDecimal basePrice,
            UserContext userContext,
            List<UserPricingStrategy> strategies,
            LocalDateTime checkTime) {

        UserPricingStrategy firstStrategy = strategies.get(0);
        BigDecimal finalPrice = firstStrategy.calculateUserDiscount(basePrice, userContext, checkTime);
        System.out.println("选择第一个适用策略: " + firstStrategy.getStrategyName()
                + ", 最终价格: " + finalPrice
                + ", 剩余有效时间: " + firstStrategy.getRemainingValidHours() + "小时");
        return finalPrice;
    }

    /**
     * 获取策略详细分析结果
     */
    public static com.yonhoo.ddd.domain.model.StrategyAnalysisResult analyzeStrategies(
            BigDecimal basePrice,
            UserContext userContext,
            List<UserPricingStrategy> strategies) {

        com.yonhoo.ddd.domain.model.StrategyAnalysisResult result = new com.yonhoo.ddd.domain.model.StrategyAnalysisResult();
        result.setBasePrice(basePrice);
        result.setUserContext(userContext);

        if (strategies == null || strategies.isEmpty()) {
            result.setFinalPrice(basePrice);
            result.setSelectedStrategy(null);
            result.setMessage("无可用策略");
            return result;
        }

        // 分析所有适用策略
        strategies.stream()
                .filter(strategy -> strategy.isApplicableForUser(userContext))
                .filter(UserPricingStrategy::isActive)
                .forEach(strategy -> {
                    BigDecimal strategyPrice = strategy.calculateUserDiscount(basePrice, userContext);
                    result.addStrategyResult(strategy, strategyPrice);
                });

        // 选择最优价格
        BigDecimal bestPrice = selectByBestPrice(basePrice, userContext, result.getApplicableStrategies(), LocalDateTime.now());
        result.setFinalPrice(bestPrice);

        return result;
    }
}