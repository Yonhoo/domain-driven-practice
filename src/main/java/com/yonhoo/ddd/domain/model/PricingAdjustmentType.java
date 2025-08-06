package com.yonhoo.ddd.domain.model;

/**
 * 定价调整类型枚举
 * 定义价格调整的不同方式
 */
public enum PricingAdjustmentType {
    MARKUP,        // 加价
    DISCOUNT,      // 折扣
    FIXED_PRICE    // 固定价格
}