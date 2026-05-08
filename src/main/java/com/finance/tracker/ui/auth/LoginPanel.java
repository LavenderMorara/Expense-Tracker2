package com.finance.tracker.ui.auth;

import com.finance.tracker.controller.AuthController;
import com.finance.tracker.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private AuthFrame authFrame;
    private AuthController authController;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(AuthFrame authFrame, AuthController authController) {

        this.authFrame = authFrame;
        this.authController = authController;

        setLayout(null);
        setBackground(Color.WHITE);

        // ================= TITLE =================
        JLabel title = new JLabel("Login");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(180, 30, 200, 40);
        add(title);

        // ================= USERNAME =================
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(60, 110, 100, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(60, 140, 320, 35);
        add(usernameField);

        // ================= PASSWORD =================
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(60, 190, 100, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(60, 220, 320, 35);
        add(passwordField);

        // ================= LOGIN BUTTON =================
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(60, 280, 320, 40);
        loginBtn.setBackground(new Color(11, 31, 59));
        loginBtn.setForeground(Color.WHITE);
        add(loginBtn);

        // ================= SIGNUP SWITCH =================
        JButton signupBtn = new JButton("Create Account");
        signupBtn.setBounds(60, 340, 320, 35);
        signupBtn.setBackground(Color.LIGHT_GRAY);
        add(signupBtn);

        // ================= ACTIONS =================

        loginBtn.addActionListener(e -> handleLogin());

        signupBtn.addActionListener(e -> authFrame.showSignup());
    }

    private void handleLogin() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = authController.login(username, password);

        if (success) {

            User user = authController.getLoggedInUser();

            JOptionPane.showMessageDialog(this,
                    "Login successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            authFrame.onLoginSuccess(user);

        } else {

            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}