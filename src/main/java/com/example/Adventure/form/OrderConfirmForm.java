package com.example.Adventure.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OrderConfirmForm {

        @NotBlank(message = "氏名を入力して下さい。")
        private String name;

        @NotBlank(message = "住所を入力して下さい。")
        private String address;

        @NotBlank(message = "郵便番号を入力して下さい。")
        @Pattern(regexp = "^[0-9]{3}[0-9]{4}$", message = "郵便番号の形式が不正です。")
        private String zipCode;

        @NotBlank(message = "電話番号を入力して下さい。")
        @Pattern(regexp = "^0\\d{9,10}$", message = "電話番号の形式で入力してください")
        private String telephone;

        private String pay;

        @Override
        public String toString() {
            return "OrderComfirmForm [name=" + name + ", address=" + address + ", zipCode=" + zipCode + ", telephone="
                    + telephone + ", pay=" + pay + "]";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getPay() {
            return pay;
        }

        public void setPay(String pay) {
            this.pay = pay;
        }

    }
