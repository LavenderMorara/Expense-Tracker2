package com.finance.tracker.ui.auth;

import com.finance.tracker.controller.AuthController;
import com.finance.tracker.model.User;

import javax.swing.*;
import java.awt.*;

public class SignupPanel extends JPanel {

    private AuthFrame authFrame;
    private AuthController authController;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignupPanel(AuthFrame authFrame, AuthController authController) {

        this.authFrame = authFrame;
        this.authController = authController;

        setLayout(null);
        setBackground(Color.WHITE);

        // ================= TITLE =================
        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(120, 20, 300, 40);
        add(title);

        // ================= NAME =================
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setBounds(60, 80, 150, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(60, 105, 320, 35);
        add(nameField);

        // ================= EMAIL =================
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(60, 150, 150, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(60, 175, 320, 35);
        add(emailField);

        // ================= USERNAME =================
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(60, 220, 150, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(60, 245, 320, 35);
        add(usernameField);

        // ================= PASSWORD =================
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(60, 290, 150, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(60, 315, 320, 35);
        add(passwordField);

        // ================= SIGNUP BUTTON =================
        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(60, 370, 320, 40);
        signupBtn.setBackground(new Color(11, 31, 59));
        signupBtn.setForeground(Color.WHITE);
        add(signupBtn);

        // ================= BACK TO LOGIN =================
        JButton loginBtn = new JButton("Already have an account? Login");
        loginBtn.setBounds(60, 420, 320, 30);
        loginBtn.setBackground(Color.LIGHT_GRAY);
        add(loginBtn);

        // ================= ACTIONS =================
        signupBtn.addActionListener(e -> handleSignup());

        loginBtn.addActionListener(e -> authFrame.showLogin());
    }

    private void handleSignup() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // ================= VALIDATION =================
        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 4 characters",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ================= CREATE USER =================
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        // ================= REGISTER =================
        boolean registered = authController.register(user);

        if (registered) {

            JOptionPane.showMessageDialog(this,
                    "Account created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // auto-login after signup
            User loggedUser = authController.getLoggedInUser();

            authFrame.onLoginSuccess(loggedUser);

        } else {

            JOptionPane.showMessageDialog(this,
                    "Signup failed. Try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}