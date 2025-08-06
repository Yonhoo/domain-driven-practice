package com.yonhoo.ddd.repository;

import com.yonhoo.ddd.domain.model.MarketingPricingStrategy;

import java.time.LocalDate;
import java.util.List;

/**
 * 营销定价策略仓储接口
 */
public interface MarketingPricingStrategyRepository {
    
    /**
     * 查询在指定日期有效的策略
     */
    List<MarketingPricingStrategy> queryEffectiveStrategies(LocalDate targetDate, String offerNo);
    
    /**
     * 查询在指定日期范围内的策略
     */
    List<MarketingPricingStrategy> queryStrategiesInDateRange(LocalDate startDate, LocalDate endDate, String offerNo);
    
    /**
     * 根据策略ID查询策略
     */
    MarketingPricingStrategy queryStrategyById(String strategyId);
    
    /**
     * 查询所有激活的策略
     */
    List<MarketingPricingStrategy> queryActiveStrategies();
    
    /**
     * 根据策略类型查询策略
     */
    List<MarketingPricingStrategy> queryStrategiesByType(com.yonhoo.ddd.domain.model.StrategyType strategyType);
    
    /**
     * 保存或更新策略
     */
    void saveStrategy(MarketingPricingStrategy strategy);
    
    /**
     * 删除策略
     */
    void deleteStrategy(String strategyId);
} 