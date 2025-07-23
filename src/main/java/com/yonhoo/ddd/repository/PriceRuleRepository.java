package com.yonhoo.ddd.repository;



import com.yonhoo.ddd.domain.model.PriceRule;

import java.util.List;

public interface PriceRuleRepository {
    List<PriceRule> queryPriceRuleByOfferNo(String offerNo);
}
