package com.yonhoo.ddd.domain.model;

public class DiscountDefinition {
    private DiscountType discountType;
    private Double value;

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
