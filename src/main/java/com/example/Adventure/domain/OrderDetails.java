package com.example.Adventure.domain;

public class OrderDetails {
    private Integer orderDetailId;
    private Integer orderId;
    private Integer productId;
    private Integer purchaseCount;
    private Integer quantity;
    private Integer subTotalPrice;

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getSubTotalPrice() {
        return subTotalPrice;
    }

    public void setSubTotalPrice(Integer subTotalPrice) {
        this.subTotalPrice = subTotalPrice;
    }

    public Integer getPurchaseCount() {
        return purchaseCount;
    }

    public void setPurchaseCount(Integer purchaseCount) {
        this.purchaseCount = purchaseCount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
