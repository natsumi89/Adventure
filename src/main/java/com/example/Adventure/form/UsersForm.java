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
        @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9?&%]{8,16}+$",message="パスワードの形式が不正です。大文字小文字数字が必須です。")
        private String password;
        @NotBlank(message="確認用パスワードを入力してください。")
        @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9?&%]{8,16}+$",message="パスワードの形式が不正です。大文字小文字数字が必須です。")
        private String rePassword;

        @AssertTrue(message="パスワードと確認用パスワードは同一にしてください。")
        public boolean isPasswordTrue() {
            if(password == null || password.isEmpty()) {
                return true;
            }
            return password.equals(rePassword);
        }

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
}
