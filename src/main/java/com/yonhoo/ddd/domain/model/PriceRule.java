package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PriceRule {
    Long priceRuleId;
    String ruleName;
    Boolean defaultPriceFlag;
    LocalDateTime createdTime;
    List<DiscountDefinition> productPriceDefinitions;


    public BigDecimal getPrice(LocalDate checkInDay, BigDecimal UnitPrice) {
        return BigDecimal.ONE;
    }


    public Long getPriceRuleId() {
        return priceRuleId;
    }

    public void setPriceRuleId(Long priceRuleId) {
        this.priceRuleId = priceRuleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Boolean getDefaultPriceFlag() {
        return defaultPriceFlag;
    }

    public void setDefaultPriceFlag(Boolean defaultPriceFlag) {
        this.defaultPriceFlag = defaultPriceFlag;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public List<DiscountDefinition> getProductPriceDefinitions() {
        return productPriceDefinitions;
    }

    public void setProductPriceDefinitions(List<DiscountDefinition> productPriceDefinitions) {
        this.productPriceDefinitions = productPriceDefinitions;
    }
}
