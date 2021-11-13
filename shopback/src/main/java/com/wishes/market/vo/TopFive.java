package com.wishes.market.vo;

public class TopFive{
    public com.wishes.market.model.CommodityDo commodityDo;
    public Integer count;

    public TopFive(com.wishes.market.model.CommodityDo commodityDo, Integer count) {
        this.commodityDo = commodityDo;
        this.count = count;
    }
}
