package com.wishes.market.mapper;

import com.wishes.market.model.Collection;

public interface CollectionMapper {
    /**
     * 新增收藏
     *
     * @param collection 收藏实例
     * @return 影响行数
     */
    int insert(Collection collection);

    /**
     * 取消收藏
     *
     * @param userId      当前用户ID
     * @param commodityId 商品ID
     * @return 影响行数
     */
    int deleteByPrimaryKey(Integer userId, Integer commodityId);

    /**
     * 根据userId 和 commodityId
     * @param userId
     * @param commodityId
     * @return
     */
    Integer queryCollectionByCommodityId(Integer userId, Integer commodityId);
}
