package com.finance.tracker.ui.main;

import com.finance.tracker.controller.ExpenseController;
import com.finance.tracker.model.Expense;
import com.finance.tracker.model.enums.Category;
import com.finance.tracker.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ExpensePanel extends JPanel {

    private final User user;
    private final ExpenseController expenseController;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JComboBox<Category> categoryBox;
    private JTextField amountField;

    private int selectedExpenseId = -1;

    public ExpensePanel(User user) {

        this.user = user;

        ExpenseController tempController;
        try {
            tempController = new ExpenseController(user);
        } catch (Exception e) {
            tempController = null;
        }
        this.expenseController = tempController;

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildFormPanel(), BorderLayout.SOUTH);

        renderEmptyState();

        SwingUtilities.invokeLater(this::loadExpenses);
    }

    // ================= HEADER =================
    private JPanel buildHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Expense Manager");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        panel.add(title, BorderLayout.WEST);

        return panel;
    }

    // ================= TABLE =================
    private JPanel buildTablePanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Expenses"));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Category", "Amount"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        table.getSelectionModel().addListSelectionListener(e -> populateForm());

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    // ================= FORM (FIXED LAYOUT) =================
    private JPanel buildFormPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Manage Expense"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;

        nameField = new JTextField();
        categoryBox = new JComboBox<>(Category.values());
        amountField = new JTextField();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        int y = 0;

        // Name
        gbc.gridy = y++;
        panel.add(new JLabel("Name"), gbc);

        gbc.gridy = y++;
        panel.add(nameField, gbc);

        // Category
        gbc.gridy = y++;
        panel.add(new JLabel("Category"), gbc);

        gbc.gridy = y++;
        panel.add(categoryBox, gbc);

        // Amount
        gbc.gridy = y++;
        panel.add(new JLabel("Amount"), gbc);

        gbc.gridy = y++;
        panel.add(amountField, gbc);

        // Buttons row 1
        JPanel row1 = new JPanel(new GridLayout(1, 2, 10, 10));
        row1.add(addBtn);
        row1.add(updateBtn);

        gbc.gridy = y++;
        panel.add(row1, gbc);

        // Buttons row 2
        JPanel row2 = new JPanel(new GridLayout(1, 2, 10, 10));
        row2.add(deleteBtn);
        row2.add(clearBtn);

        gbc.gridy = y;
        panel.add(row2, gbc);

        addBtn.addActionListener(e -> addExpense());
        updateBtn.addActionListener(e -> updateExpense());
        deleteBtn.addActionListener(e -> deleteExpense());
        clearBtn.addActionListener(e -> clearForm());

        return panel;
    }

    // ================= EMPTY STATE =================
    private void renderEmptyState() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"", "No expenses yet", "", ""});
    }

    // ================= LOAD =================
    private void loadExpenses() {

        if (expenseController == null) {
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"", "Controller not ready", "", ""});
            return;
        }

        tableModel.setRowCount(0);

        try {
            List<Expense> expenses = expenseController.getMyExpenses();

            if (expenses == null || expenses.isEmpty()) {
                tableModel.addRow(new Object[]{"", "No expenses found", "", ""});
                return;
            }

            for (Expense e : expenses) {

                if (e == null) continue;

                tableModel.addRow(new Object[]{
                        e.getId(),
                        e.getName(),
                        e.getCategory(),
                        String.format("%,.2f", e.getAmount())
                });
            }

        } catch (Exception e) {
            tableModel.addRow(new Object[]{"", "Load error", "", ""});
            e.printStackTrace();
        }
    }

    // ================= POPULATE =================
    private void populateForm() {

        int row = table.getSelectedRow();
        if (row < 0) return;

        Object id = tableModel.getValueAt(row, 0);
        if (id instanceof Integer) {
            selectedExpenseId = (Integer) id;
        }

        nameField.setText(String.valueOf(tableModel.getValueAt(row, 1)));

        amountField.setText(String.valueOf(tableModel.getValueAt(row, 3)).replace(",", ""));
    }

    // ================= ADD =================
    private void addExpense() {

        try {
            if (expenseController == null) return;

            boolean ok = expenseController.addExpense(
                    nameField.getText(),
                    (Category) categoryBox.getSelectedItem(),
                    Double.parseDouble(amountField.getText())
            );

            if (ok) loadExpenses();
            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    // ================= UPDATE =================
    private void updateExpense() {

        if (selectedExpenseId == -1) return;

        try {
            boolean ok = expenseController.updateExpense(
                    selectedExpenseId,
                    nameField.getText(),
                    (Category) categoryBox.getSelectedItem(),
                    Double.parseDouble(amountField.getText())
            );

            if (ok) loadExpenses();
            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input");
        }
    }

    // ================= DELETE =================
    private void deleteExpense() {

        if (selectedExpenseId == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete expense?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                if (expenseController.deleteExpense(selectedExpenseId)) {
                    loadExpenses();
                    clearForm();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Delete failed");
            }
        }
    }

    // ================= CLEAR =================
    private void clearForm() {

        nameField.setText("");
        amountField.setText("");
        selectedExpenseId = -1;
        table.clearSelection();
    }
}
