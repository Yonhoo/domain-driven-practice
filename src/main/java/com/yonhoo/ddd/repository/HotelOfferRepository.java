package com.yonhoo.ddd.repository;


import com.yonhoo.ddd.domain.model.HotelOffer;
import com.yonhoo.ddd.domain.model.HotelOfferV2;

public interface HotelOfferRepository {
    HotelOffer queryHotelOfferByOfferNo(String offerNo);

    HotelOfferV2 queryHotelOfferV2ByOfferNo(String offerNo);


}
