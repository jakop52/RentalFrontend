package com.jakop52.apartmentrent.android.dto;

import com.jakop52.apartmentrent.android.model.User;

public class LoginResponse {
    private boolean success;
    private String message;
    private User user;

    public LoginResponse(boolean success) {
        this.success = success;
    }

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
