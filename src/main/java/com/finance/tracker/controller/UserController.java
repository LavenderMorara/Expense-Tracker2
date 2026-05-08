package com.finance.tracker.controller;

import com.finance.tracker.model.User;
import com.finance.tracker.service.UserService;


public class UserController {

    private final UserService userService;

    public UserController(User loggedInUser) {
       
        this.userService = new UserService(loggedInUser);
    }

    public User getLoggedInUser() {
        return userService.getLoggedInUser();
    }

    public boolean updateUsername(String newUsername) {
        return userService.updateUsername(newUsername);
    }

    public boolean updatePassword(String oldPassword, String newPassword) {
        return userService.updatePassword(oldPassword, newPassword);
    }
}
