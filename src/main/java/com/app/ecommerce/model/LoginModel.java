package com.app.ecommerce.model;

import javax.validation.constraints.NotBlank;

public class LoginModel {
    @NotBlank
    private String emailPhone;

    @NotBlank
    private String password;

    public String getEmailPhone() {
        return emailPhone;
    }

    public void setEmailPhone(String emailPhone) {
        this.emailPhone = emailPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
