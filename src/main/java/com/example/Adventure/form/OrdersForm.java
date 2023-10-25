package com.example.Adventure.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OrdersForm {
        private Integer totalPrice;
        private String paymentMethod;
        private Integer userId;
        private Integer telephone;

    @NotNull(message = "郵便番号は必須です")
    @Pattern(regexp = "\\d{7}", message = "郵便番号は7桁の数字で入力してください")
    private Integer zipCode;

    @NotNull(message = "住所は必須です")
    @Size(max = 255, message = "住所は255文字以内で入力してください")
    private String address;

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
