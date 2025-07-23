package com.yonhoo.ddd.domain.model;

public class SalesCondition {
    ConditionType type;
    String value;

    public ConditionType getType() {
        return type;
    }

    public void setType(ConditionType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
