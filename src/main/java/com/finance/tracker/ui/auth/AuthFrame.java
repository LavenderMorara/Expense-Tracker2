package com.finance.tracker.ui.auth;

import javax.swing.*;
import java.awt.*;

import com.finance.tracker.controller.AuthController;
import com.finance.tracker.model.User;
import com.finance.tracker.ui.main.MainFrame;

public class AuthFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;

    private AuthController authController;

    public AuthFrame() {

        authController = new AuthController();

        setTitle("Finance Tracker - Authentication");
        setSize(450, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // ================= MAIN CONTAINER =================
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // ================= PANELS =================
        LoginPanel loginPanel = new LoginPanel(this, authController);
        SignupPanel signupPanel = new SignupPanel(this, authController);

        container.add(loginPanel, "login");
        container.add(signupPanel, "signup");

        add(container, BorderLayout.CENTER);

        // default screen
        showLogin();
    }

    // ================= NAVIGATION METHODS =================
    public void showLogin() {
        cardLayout.show(container, "login");
    }

    public void showSignup() {
        cardLayout.show(container, "signup");
    }

    // ================= SUCCESS LOGIN HANDLER =================
    public void onLoginSuccess(User user) {

        // open main application window
        SwingUtilities.invokeLater(() -> {
            new MainFrame(user).setVisible(true);
        });

        // close auth window
        this.dispose();
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AuthFrame().setVisible(true);
        });
    }
}