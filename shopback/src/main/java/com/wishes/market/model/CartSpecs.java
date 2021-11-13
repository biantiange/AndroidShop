package com.wishes.market.model;

public class CartSpecs {
    private Long id;
    private String detail;
    private Long cartId;

    public CartSpecs(Long id, String detail, Long cartId) {
        this.id = id;
        this.detail = detail;
        this.cartId = cartId;
    }

    public CartSpecs() {
    }

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

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    @Override
    public String toString() {
        return "CartSpecs{" +
                "id=" + id +
                ", detail='" + detail + '\'' +
                ", cartId=" + cartId +
                '}';
    }
}
