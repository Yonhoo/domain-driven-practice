package com.yonhoo.ddd.domain.model;

import java.util.Arrays;

public enum CustomerChoice {
    MULTIPLE("Multiple"),
    SINGLE("Single"),
    FIXED("Fixed");

    private final String code;

    CustomerChoice(String code) {
        this.code = code;
    }

    public static CustomerChoice from(String customerChoice) {
        return Arrays.stream(CustomerChoice.values())
                .filter(value -> value.code.equals(customerChoice))
                .findFirst()
                .orElse(null);
    }


    public String getCode() {
        return code;
    }
}
