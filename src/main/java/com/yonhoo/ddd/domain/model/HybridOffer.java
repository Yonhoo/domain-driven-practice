package com.yonhoo.ddd.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
public class HybridOffer {

    private ProductGroups productGroups;

    private String offerNo;

    private List<PriceRule> priceRuleList;

    private Validity validity;


    public BigDecimal calculateItemDiscountedUnitPriceV3(CalculatedContext calculatedContext) {

            return BigDecimal.ONE;

    }

}
