package com.qtsneaker.common.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tbl_customers")
public class Customer extends AbstractAddressWithProvince {
    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;
    private boolean enabled;

    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    // Đánh dấu trường enum sẽ được ánh xạ vào cột trong cơ sở dữ liệu
    // EnumType.STRING giá trị của enum sẽ được lưu trữ dưới  (string) trong cơ sở dữ liệu
    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    public Customer() {
    }
    public Customer(Integer id) {
        this.id = id;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
