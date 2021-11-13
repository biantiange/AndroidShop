package com.wishes.market.mapper;

import com.wishes.market.model.CartSpecs;

public interface CartSpecsMapper {
    int insert(CartSpecs CartSpecs);

    int updateByCartId(Long CartId,String detail);

    CartSpecs findCartSpecsByCartId(Long CartId);
}
