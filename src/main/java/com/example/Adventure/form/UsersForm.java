package com.example.Adventure.form;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class UsersForm {
        private Integer userId;
        @NotBlank(message="姓を入力してください。")
        private String lastName;
        @NotBlank(message="名を入力してください。")
        private String firstName;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull(message="生年月日を入力してください。")
        private Date birthDate;
        @NotBlank(message="メールアドレスを入力してください。")
        @Email(message="メールアドレスの形式が不正です。")
        private String email;

        @NotBlank(message="パスワードを入力してください。")
        @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+$",message="半角英字大文字、小文字、数字の3種類を1つ必ず使用し、8文字以上16文字以内で入力してください。")
        private String password;
        @NotBlank(message="確認用パスワードを入力してください。")
        @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]+$",message="半角英字大文字、小文字、数字の3種類を1つ必ず使用し、8文字以上16文字以内で入力してください。")
        private String rePassword;

//        @AssertTrue(message="パスワードと確認用パスワードは同一にしてください。")
//        public boolean isPasswordTrue() {
//            if(password == null || password.isEmpty()) {
//                return true;
//            }
//            return password.equals(rePassword);
//        }
    @NotNull(message = "電話番号は必須です。")
        private Integer telephone;
    @NotNull(message = "郵便番号は必須です。")
        private Integer zipCode;
    @NotBlank(message = "住所は必須です。")
        private String address;
    private String paymentMethod;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
