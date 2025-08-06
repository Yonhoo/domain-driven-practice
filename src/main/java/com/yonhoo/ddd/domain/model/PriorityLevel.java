package com.yonhoo.ddd.domain.model;

/**
 * 优先级等级枚举
 * 定义策略执行的优先级顺序
 */
public enum PriorityLevel {
    LOW(1),          // 低优先级
    MEDIUM(2),       // 中等优先级
    HIGH(3),         // 高优先级
    URGENT(4);       // 紧急优先级
    
    private final int level;
    
    PriorityLevel(int level) {
        this.level = level;
    }
    
    public int getLevel() {
        return level;
    }
    
    public boolean isHigherThan(PriorityLevel other) {
        return this.level > other.level;
    }
}