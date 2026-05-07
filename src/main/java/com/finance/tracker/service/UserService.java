package com.finance.tracker.service;

import com.finance.tracker.model.User;
import com.finance.tracker.repository.UserRepository;

public class UserService {

    private UserRepository userRepository = new UserRepository();

    private User loggedInUser;

    public UserService(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public boolean updateUsername(String newUsername) {

        if (loggedInUser == null) return false;

        if (newUsername == null || newUsername.trim().isEmpty()) return false;

        loggedInUser.setUsername(newUsername);

        userRepository.updateUsername(loggedInUser);

        return true;
    }

    public boolean updatePassword(String oldPassword, String newPassword) {

        if (loggedInUser == null) return false;

        if (!loggedInUser.getPassword().equals(oldPassword)) {
            return false;
        }

        if (newPassword == null || newPassword.length() < 4) {
            return false;
        }

        loggedInUser.setPassword(newPassword);

        userRepository.updatePassword(loggedInUser);

        return true;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
