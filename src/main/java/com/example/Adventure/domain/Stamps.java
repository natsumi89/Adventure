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

    public Stamps() {
        this.stamps = 0; // 初期値は0
        this.cardNumber = 0; // 初期値は0
    }
    public void stampPressed() {
        this.stamps++;
        System.out.println("Stamps: " + this.stamps);

        // スタンプが10の倍数になるごとにcardNumberを増加させる
        if (this.stamps % 10 == 0) {
            this.cardNumber++;
            System.out.println("Card Number: " + this.cardNumber);
        }
    }

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

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer RegionId) {
        this.regionId = regionId;
    }

    @Override
    public String toString() {
        return "Stamps{" +
                "stampId=" + stampId +
                ", stampDate=" + stampDate +
                ", userId=" + userId +
                ", regionId=" + regionId +
                ", stamps=" + stamps +
                ", cardNumber=" + cardNumber +
                '}';
    }

}


