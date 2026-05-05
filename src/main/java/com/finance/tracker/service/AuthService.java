package com.finance.tracker.service;

import com.finance.tracker.model.User;
import com.finance.tracker.repository.UserRepository;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();

    private User loggedInUser;

    public AuthService() {}

    public boolean login(String username, String password) {

        User user = userRepository.logInUser(username, password);

        if (user != null) {
            loggedInUser = user;
            return true;
        }

        return false;
    }

    public boolean register(User user) {

    boolean inserted = userRepository.registerUser(user);

    if (!inserted) {
        return false;
    }

    User logged = userRepository.logInUser(
            user.getUsername(),
            user.getPassword()
    );

    if (logged != null) {
        loggedInUser = logged;
        return true;
    }

    return false;
}
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void logout() {
        loggedInUser = null;
    }
}
