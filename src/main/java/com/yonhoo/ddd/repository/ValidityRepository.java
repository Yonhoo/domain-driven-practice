package com.yonhoo.ddd.repository;


import com.yonhoo.ddd.domain.model.Validity;

public interface ValidityRepository {
    Validity queryValidityByOfferNo(String offerNo);
}
