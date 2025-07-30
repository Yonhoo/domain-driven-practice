package com.yonhoo.ddd.domain.model;

import java.util.List;



// product 只感知最后的价格(int)，不引入模型概念，设置上下文边界

public class AttractionProduct {
    private QuantityRange validQuantityRange;
    private List<TicketItem> productItemList;

    public Integer getMinQuantity() {
        return validQuantityRange.getMin();
    }

    public QuantityRange getValidQuantityRange() {
        return validQuantityRange;
    }

    public void setValidQuantityRange(QuantityRange validQuantityRange) {
        this.validQuantityRange = validQuantityRange;
    }

    public List<TicketItem> getProductItemList() {
        return productItemList;
    }

    public void setProductItemList(List<TicketItem> productItemList) {
        this.productItemList = productItemList;
    }
}
