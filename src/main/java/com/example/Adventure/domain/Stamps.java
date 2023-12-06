package com.example.Adventure.domain;

import java.util.Date;

public class Stamps {
    private Integer stampId;
    private Integer userId;
    private Integer regionId;
//    private Date stampDate;
    private Integer stamps;
    private Integer cardNumber;
    private Date stampDate = new Date();
    private Integer orderId;

    public Integer getStampId() {
        return stampId;
    }

    public void setStampId(Integer stampId) {
        this.stampId = stampId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getStamps() {
        return stamps;
    }

    public void setStamps(Integer stamps) {
        this.stamps = stamps;
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getStampDate() {
        return stampDate;
    }

    public void setStampDate(Date stampDate) {
        this.stampDate = stampDate;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}


