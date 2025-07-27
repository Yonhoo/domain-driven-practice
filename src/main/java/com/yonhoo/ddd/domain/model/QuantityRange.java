package com.yonhoo.ddd.domain.model;

public class QuantityRange {

    Integer min;
    Integer max;

    public QuantityRange(int minValue, int maxValue) {
        this.min = minValue;
        this.max = maxValue;
    }

    public static QuantityRange defaultQuantityRange() {
        return new QuantityRange(1, Integer.MAX_VALUE);
    }
}
