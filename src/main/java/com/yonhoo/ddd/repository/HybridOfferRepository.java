package com.yonhoo.ddd.repository;

import com.yonhoo.ddd.domain.model.HotelOffer;
import com.yonhoo.ddd.domain.model.HybridOffer;

public interface HybridOfferRepository {
    HybridOffer queryHybridOfferByOfferNo(String offerNo);
}
