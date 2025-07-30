package com.yonhoo.ddd.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class PricePair {
    private LocalDate day;
    private BigDecimal price;
}
