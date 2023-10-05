package com.example.Adventure.domain;

public class ShoppingCarts {
    private Integer cartsId;
    private Integer userId;
    private Integer productId;
    private Integer quantity;

    public Integer getCartsId() {
        return cartsId;
    }

    public void setCartsId(Integer cartsId) {
        this.cartsId = cartsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
