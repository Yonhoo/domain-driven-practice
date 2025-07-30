package com.yonhoo.ddd.domain.model;

public class QuantityRange {

    Integer min;
    Integer max;

    public QuantityRange(int minValue, int maxValue) {
        this.min = minValue;
        this.max = maxValue;
    }


    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public static QuantityRange defaultQuantityRange() {
        return new QuantityRange(1, Integer.MAX_VALUE);
    }
}
