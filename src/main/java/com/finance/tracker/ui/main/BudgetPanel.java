package com.finance.tracker.ui.main;

import com.finance.tracker.controller.BudgetController;
import com.finance.tracker.model.Budget;
import com.finance.tracker.model.enums.BudgetPeriod;
import com.finance.tracker.model.enums.Category;
import com.finance.tracker.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BudgetPanel extends JPanel {

    private final User user;
    private BudgetController budgetController;

    private DefaultListModel<String> listModel;
    private JList<String> budgetList;

    private JComboBox<Category> categoryBox;
    private JComboBox<BudgetPeriod> periodBox;

    private JTextField amountField;
    private JTextField monthField;
    private JTextField yearField;

    private JLabel statusLabel;

    public BudgetPanel(User user) {

        this.user = user;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildListPanel(), BorderLayout.WEST);
        add(buildFormPanel(), BorderLayout.CENTER);

        renderEmptyState();

        try {
            this.budgetController = new BudgetController(user);
            loadBudgets();
        } catch (Exception e) {
            showError("Controller init failed");
        }
    }

    // ================= PLACEHOLDER HELPER =================
    private void addPlaceholder(JTextField field, String text) {

        field.setText(text);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(text)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(text);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    // ================= HEADER =================
    private JPanel buildHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Budget Manager");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        statusLabel = new JLabel("Initializing...");
        statusLabel.setForeground(Color.GRAY);

        panel.add(title, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.EAST);

        return panel;
    }

    // ================= LIST =================
    private JPanel buildListPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(420, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Budgets"));

        listModel = new DefaultListModel<>();
        budgetList = new JList<>(listModel);

        panel.add(new JScrollPane(budgetList), BorderLayout.CENTER);

        return panel;
    }

    // ================= FORM =================
    private JPanel buildFormPanel() {

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Create Budget"));

        categoryBox = new JComboBox<>(Category.values());
        periodBox = new JComboBox<>(BudgetPeriod.values());

        amountField = new JTextField();
        monthField = new JTextField();
        yearField = new JTextField();

        // ✅ PLACEHOLDERS ADDED
        addPlaceholder(amountField, "Enter amount (e.g. 5000)");
        addPlaceholder(monthField, "Enter month (1-12)");
        addPlaceholder(yearField, "Enter year (e.g. 2026)");

        JButton createBtn = new JButton("Create");
        JButton refreshBtn = new JButton("Refresh");

        panel.add(new JLabel("Category"));
        panel.add(categoryBox);

        panel.add(new JLabel("Period"));
        panel.add(periodBox);

        panel.add(new JLabel("Amount"));
        panel.add(amountField);

        panel.add(new JLabel("Month"));
        panel.add(monthField);

        panel.add(new JLabel("Year"));
        panel.add(yearField);

        panel.add(createBtn);
        panel.add(refreshBtn);

        createBtn.addActionListener(e -> createBudget());
        refreshBtn.addActionListener(e -> loadBudgets());

        return panel;
    }

    // ================= EMPTY =================
    private void renderEmptyState() {

        listModel.clear();
        listModel.addElement("No budgets yet");
        listModel.addElement("Create one using the form");

        if (statusLabel != null) {
            statusLabel.setText("Ready (no data)");
            statusLabel.setForeground(Color.GRAY);
        }
    }

    // ================= LOAD =================
    private void loadBudgets() {

        if (budgetController == null) {
            showError("Controller not ready");
            return;
        }

        statusLabel.setText("Loading...");
        statusLabel.setForeground(Color.BLUE);

        SwingWorker<List<Budget>, Void> worker = new SwingWorker<>() {

            @Override
            protected List<Budget> doInBackground() {
                return budgetController.refreshBudgetSpending();
            }

            @Override
            protected void done() {

                listModel.clear();

                try {
                    List<Budget> budgets = get();

                    if (budgets == null || budgets.isEmpty()) {
                        listModel.addElement("No budgets found");
                        statusLabel.setText("Empty state");
                        statusLabel.setForeground(Color.ORANGE);
                        return;
                    }

                    for (Budget b : budgets) {
                        listModel.addElement(
                                b.getCategory() + " | " +
                                String.format("KES %,.2f", b.getAmount()) +
                                " | " + String.format("%.1f%%", b.getUsagePercentage())
                        );
                    }

                    statusLabel.setText("Loaded ✔");
                    statusLabel.setForeground(new Color(0, 120, 0));

                } catch (Exception e) {
                    showError("Load failed");
                }
            }
        };

        worker.execute();
    }

    // ================= CREATE =================
    private void createBudget() {

        try {
            Category category = (Category) categoryBox.getSelectedItem();
            BudgetPeriod period = (BudgetPeriod) periodBox.getSelectedItem();

            String amountText = amountField.getText();
            String monthText = monthField.getText();
            String yearText = yearField.getText();

            // ✅ prevent placeholder + empty input
            if (amountText.contains("Enter") ||
                monthText.contains("Enter") ||
                yearText.contains("Enter")) {
                showError("Please fill all fields properly");
                return;
            }

            double amount = Double.parseDouble(amountText);
            int month = Integer.parseInt(monthText);
            int year = Integer.parseInt(yearText);

            boolean ok = budgetController.createBudget(category, period, amount, month, year);

            if (ok) {
                statusLabel.setText("Created ✔");
                loadBudgets();
                clearForm();
            } else {
                showError("Create failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Invalid input");
        }
    }

    private void clearForm() {
        amountField.setText("");
        monthField.setText("");
        yearField.setText("");
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(Color.RED);
    }
}
