package com.finance.tracker.controller;

import com.finance.tracker.model.User;
import com.finance.tracker.service.AuthService;

public class AuthController {

    private AuthService authService = new AuthService();

    public AuthController() {}

    public boolean login(String username, String password) {
        return authService.login(username, password);
    }

    public boolean register(User user) {
        return authService.register(user);
    }

    public User getLoggedInUser() {
        return authService.getLoggedInUser();
    }

    public void logout() {
        authService.logout();
    }
}
