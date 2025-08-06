package com.yonhoo.ddd.domain.service;



/*
*
* 我的思考： https://chatgpt.com/share/688f7664-6818-8003-9a02-70dcbeb186d5
*
* 是很多系统在 DDD 落地过程中遇到的“配置模型 vs 运行时模型”的边界设计问题。
*
| 任务                        | 推荐做法                                    |
| ------------------------   | --------------------------------------- |
| 是否将库存 / 价格传入 HotelOffer？ | ❌ 不建议。会破坏聚合边界                           |
| 如何组织计算逻辑？                | ✅ 抽出 `PricingService`（领域服务）处理跨聚合组合计算    |
| 外部系统接入如何设计？              | ✅ 通过防腐层（ACL）适配外部库存系统和定价系统               |
| HotelOffer 聚合的职责是什么？     | ✅ 管理可配置信息，如组合、规则、可售时间、customerChoice 等  |
| PricingService 的职责是什么？   | ✅ 基于 HotelOffer 配置 + 外部系统数据，返回运行时“报价详情” |

*
*
* Evans 在《领域驱动设计》中说过一段话，概括如下：
如果一个行为是根据聚合自身规则和外部信息共同决定的，那应该让聚合暴露一个方法来完成这个行为，外部信息可以作为参数传入，
* 但聚合不应该把内部数据暴露出来让别人处理。
*
*
* 最终总结：是否应该将外部参数传入聚合根行为？
✅ 是的，可以传，只要遵守这些前提：

外部数据不能变成聚合根的状态；

聚合根仍然负责业务行为和规则表达；

参数应是当前行为执行所需的上下文，不是跨事务的依赖；

接收的值对象应是只读的（Value Object、DTO），不是 Entity。
*
*
* */
//public class PricingService {
//
//    private PriceAdapter priceAdapter;     // ACL 层
//    private InventoryAdapter inventoryAdapter; // ACL 层
//
//    public OfferPricingResult computeOfferDetails(HotelOffer offer, LocalDateTime time) {
//        // 1. 获取外部价格和库存
//        Map<RoomType, Price> priceMap = priceAdapter.getPrices(offer.getProducts(), time);
//        Map<RoomType, Boolean> inventoryMap = inventoryAdapter.checkAvailability(offer.getProducts(), time);
//
//        // 2. 应用规则、筛选可用产品
//        List<RoomType> availableRooms = offer.getProducts().stream()
//                .filter(room -> inventoryMap.getOrDefault(room, false))
//                .collect(Collectors.toList());
//
//        // 3. 根据 customer choice 筛选组合
//        List<RoomType> chosenRooms = CustomerChoiceStrategy.select(offer.getCustomerChoice(), availableRooms);
//
//        // 4. 应用价格规则
//        BigDecimal minPrice = offer.getPriceRuleList().stream()
//                .map(rule -> rule.apply(chosenRooms, priceMap))
//                .min(Comparator.naturalOrder())
//                .orElseThrow(() -> new RuntimeException("No applicable price"));
//
//        return new OfferPricingResult(true, minPrice);
//    }
//
//
//
//
//}
