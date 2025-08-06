package com.yonhoo.ddd.domain.model;

/**
 * 策略类型枚举
 * 定义不同的营销策略类型
 */
public enum StrategyType {
    HOLIDAY_PRICING,     // 节假日定价
    FLASH_SALE,          // 限时抢购
    SEASONAL_PRICING,    // 季节性定价
    COMBINED             // 组合策略
}