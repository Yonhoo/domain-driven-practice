package com.yonhoo.ddd.domain.model;

import java.util.List;

public class AttractionProduct {
    private QuantityRange validQuantityRange;
    private CustomerChoice customerChoice;
    private List<TicketItem> productItemList;


    public QuantityRange getValidQuantityRange() {
        return validQuantityRange;
    }

    public void setValidQuantityRange(QuantityRange validQuantityRange) {
        this.validQuantityRange = validQuantityRange;
    }

    public CustomerChoice getCustomerChoice() {
        return customerChoice;
    }

    public void setCustomerChoice(CustomerChoice customerChoice) {
        this.customerChoice = customerChoice;
    }

    public List<TicketItem> getProductItemList() {
        return productItemList;
    }

    public void setProductItemList(List<TicketItem> productItemList) {
        this.productItemList = productItemList;
    }
}
