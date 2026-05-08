package com.finance.tracker.ui.main;

import com.finance.tracker.model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final User loggedInUser;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame(User loggedInUser) {

        if (loggedInUser == null) {
            throw new IllegalArgumentException("User cannot be null in MainFrame");
        }

        this.loggedInUser = loggedInUser;

        // ================= FRAME =================
        setTitle("Finance Tracker - " + loggedInUser.getUsername());
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel(new GridLayout(5, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(11, 31, 59));

        JButton dashboardBtn = new JButton("Dashboard");
        JButton expenseBtn = new JButton("Expenses");
        JButton budgetBtn = new JButton("Budget");
        JButton reportBtn = new JButton("Reports");
        JButton profileBtn = new JButton("Profile");

        style(dashboardBtn);
        style(expenseBtn);
        style(budgetBtn);
        style(reportBtn);
        style(profileBtn);

        sidebar.add(dashboardBtn);
        sidebar.add(expenseBtn);
        sidebar.add(budgetBtn);
        sidebar.add(reportBtn);
        sidebar.add(profileBtn);

        // ================= CONTENT =================
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new DashboardPanel(loggedInUser), "DASHBOARD");
        contentPanel.add(new ExpensePanel(loggedInUser), "EXPENSE");
        contentPanel.add(new BudgetPanel(loggedInUser), "BUDGET");
        contentPanel.add(new ReportPanel(loggedInUser), "REPORT");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // DEFAULT VIEW
        cardLayout.show(contentPanel, "DASHBOARD");

        // ================= NAVIGATION =================
        dashboardBtn.addActionListener(e -> cardLayout.show(contentPanel, "DASHBOARD"));
        expenseBtn.addActionListener(e -> cardLayout.show(contentPanel, "EXPENSE"));
        budgetBtn.addActionListener(e -> cardLayout.show(contentPanel, "BUDGET"));
        reportBtn.addActionListener(e -> cardLayout.show(contentPanel, "REPORT"));

        profileBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Profile not yet wired")
        );

        setVisible(true);
    }

    private void style(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(new Color(11, 31, 59));
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
