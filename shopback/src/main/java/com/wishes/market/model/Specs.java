package com.wishes.market.model;

public class Specs {
    private Long id;
    private String detail;
    private Long commodityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    @Override
    public String toString() {
        return "Specs{" +
                "id=" + id +
                ", detail='" + detail + '\'' +
                ", commodityId=" + commodityId +
                '}';
    }
}
