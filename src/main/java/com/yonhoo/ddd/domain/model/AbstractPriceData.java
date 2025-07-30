package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class AbstractPriceData {

    public BigDecimal getMinPriceByDay(LocalDate day) {
        return null;
    }
}
