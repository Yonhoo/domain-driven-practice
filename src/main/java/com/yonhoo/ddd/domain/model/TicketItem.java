package com.yonhoo.ddd.domain.model;

import java.math.BigDecimal;
import java.util.Collection;

public class TicketItem {

    String productNumber;
    String ticketCode;
    Collection<String> themeParks;
    String name;
    String description;
    BigDecimal unitPrice;


    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public Collection<String> getThemeParks() {
        return themeParks;
    }

    public void setThemeParks(Collection<String> themeParks) {
        this.themeParks = themeParks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
