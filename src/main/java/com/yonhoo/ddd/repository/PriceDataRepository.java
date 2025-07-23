package com.yonhoo.ddd.repository;



import com.yonhoo.ddd.domain.model.PriceData;

import java.util.List;
import java.util.Map;

public interface PriceDataRepository {
    Map<String, PriceData> queryPriceDataByRoomList(List<String> roomList);
}
