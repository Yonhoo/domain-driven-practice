package com.yonhoo.ddd.domain.model;

/**
 * 定价类型枚举
 * 标识价格的计算类型
 */
public enum PricingType {
    STANDARD,        // 标准定价
    HOLIDAY,         // 节假日定价
    FLASH_SALE,      // 限时抢购定价
    SEASONAL,        // 季节性定价
    USER_DISCOUNT    // 用户折扣定价
}