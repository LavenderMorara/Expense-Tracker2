package com.finance.tracker.ui.main;

import com.finance.tracker.controller.AuthController;
import com.finance.tracker.controller.UserController;
import com.finance.tracker.model.User;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {

    private AuthController authController;
    private UserController userController;

    private User user;

    private JLabel usernameLabel;
    private JLabel emailLabel;

    private JTextField usernameField;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;

    public ProfilePanel(AuthController authController, UserController userController) {

        this.authController = authController;
        this.userController = userController;

        this.user = authController.getLoggedInUser();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setVisible(true);
        setPreferredSize(new Dimension(800, 600));

        add(buildViewPanel(), BorderLayout.NORTH);
        add(buildEditPanel(), BorderLayout.CENTER);
    }

    // ================= VIEW =================
    private JPanel buildViewPanel() {

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createTitledBorder("Profile Info"));
        panel.setBackground(Color.WHITE);

        usernameLabel = new JLabel("Username: " + user.getUsername());
        emailLabel = new JLabel("Email: " + user.getEmail());

        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(usernameLabel);
        panel.add(emailLabel);

        return panel;
    }

    // ================= EDIT =================
    private JPanel buildEditPanel() {

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Edit Profile"));
        panel.setBackground(Color.WHITE);

        usernameField = new JTextField();

        oldPasswordField = new JPasswordField();
        newPasswordField = new JPasswordField();

        JButton updateUsernameBtn = new JButton("Update Username");
        JButton updatePasswordBtn = new JButton("Update Password");

        panel.add(new JLabel("New Username"));
        panel.add(usernameField);
        panel.add(new JLabel(""));
        panel.add(updateUsernameBtn);

        panel.add(new JLabel("Old Password"));
        panel.add(oldPasswordField);

        panel.add(new JLabel("New Password"));
        panel.add(newPasswordField);

        panel.add(new JLabel(""));
        panel.add(updatePasswordBtn);

        updateUsernameBtn.addActionListener(e -> updateUsername());
        updatePasswordBtn.addActionListener(e -> updatePassword());

        return panel;
    }

    // ================= USERNAME =================
    private void updateUsername() {

        String newUsername = usernameField.getText().trim();

        if (newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty");
            return;
        }

        boolean success = userController.updateUsername(newUsername);

        if (success) {
            JOptionPane.showMessageDialog(this, "Username updated!");

            user.setUsername(newUsername);
            usernameLabel.setText("Username: " + newUsername);

            usernameField.setText("");

        } else {
            JOptionPane.showMessageDialog(this, "Update failed");
        }
    }

    // ================= PASSWORD =================
    private void updatePassword() {

        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());

        if (oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all password fields");
            return;
        }

        boolean success = userController.updatePassword(oldPass, newPass);

        if (success) {
            JOptionPane.showMessageDialog(this, "Password updated!");

            oldPasswordField.setText("");
            newPasswordField.setText("");

        } else {
            JOptionPane.showMessageDialog(this, "Incorrect old password");
        }
    }
}
