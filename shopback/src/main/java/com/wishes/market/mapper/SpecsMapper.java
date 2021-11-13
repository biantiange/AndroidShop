package com.wishes.market.mapper;

import com.wishes.market.model.Specs;

public interface SpecsMapper {
    int insert(Specs specs);

    int updateByCommodityId(Long commodityId,String detail);

    Specs findSpecsByCommodityId(Long commodityId);
}
