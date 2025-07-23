package com.yonhoo.ddd.repository;



import com.yonhoo.ddd.domain.model.HotelProduct;

import java.util.List;

public interface ProductRepository {
    List<HotelProduct> queryHotelProductByOfferNo(String offerNo);
}
