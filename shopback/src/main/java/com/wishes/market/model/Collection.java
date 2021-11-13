package com.wishes.market.model;

public class Collection {
    private Integer UserId;
    private Integer commodityId;
    private Integer deleteFlag;

    public Collection() {
    }

    public Collection(Integer userId, Integer commodityId, Integer deleteFlag) {
        UserId = userId;
        this.commodityId = commodityId;
        this.deleteFlag = deleteFlag;
    }

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "UserId=" + UserId +
                ", commodityId=" + commodityId +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}
