package com.yonhoo.ddd.repository;

import com.yonhoo.ddd.domain.model.UserContext;
import com.yonhoo.ddd.domain.model.UserPricingStrategy;

import java.util.List;

/**
 * 用户定价策略仓储接口
 */
public interface UserPricingStrategyRepository {
    
    /**
     * 查询适用于指定用户的策略
     */
    List<UserPricingStrategy> queryApplicableStrategies(UserContext userContext);
    
    /**
     * 根据策略ID查询策略
     */
    UserPricingStrategy queryStrategyById(String strategyId);
    
    /**
     * 查询所有激活的策略
     */
    List<UserPricingStrategy> queryActiveStrategies();
    
    /**
     * 保存或更新策略
     */
    void saveStrategy(UserPricingStrategy strategy);
    
    /**
     * 删除策略
     */
    void deleteStrategy(String strategyId);
} 