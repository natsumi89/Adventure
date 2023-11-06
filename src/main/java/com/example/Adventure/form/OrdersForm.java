package com.example.Adventure.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OrdersForm {
    private Integer totalPrice;
    private String paymentMethod;
    private Integer userId;
    @NotBlank(message = "電話番号を入力して下さい。")
    @Pattern(regexp = "^0\\d{9,10}$", message = "電話番号の形式で入力してください")
    private String telephone;
    @NotBlank(message = "姓を入力してください。")
    private String lastName;
    @NotBlank(message = "名を入力してください。")
    private String firstName;

    @NotNull(message = "郵便番号は必須です")
    @Pattern(regexp = "\\d{7}", message = "郵便番号は7桁の数字で入力してください")
    private String zipCode;

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
