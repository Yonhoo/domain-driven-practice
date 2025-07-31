package com.yonhoo.ddd.repository;


import com.yonhoo.ddd.domain.model.PriceData;
import com.yonhoo.ddd.domain.model.PriceDataV2;

import java.util.List;
import java.util.Map;

public interface PriceDataRepository {
    Map<String, PriceData> queryPriceDataByRoomList(List<String> roomList);

    Map<String, PriceDataV2> queryPriceDataV2ByRoomList(List<String> roomList);

    Map<String, PriceDataV2> queryPriceDataV2ByTicketList(List<String> roomList);
}
