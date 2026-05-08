package com.finance.tracker.ui.main;

import com.finance.tracker.controller.BudgetController;
import com.finance.tracker.controller.ExpenseController;
import com.finance.tracker.model.Budget;
import com.finance.tracker.model.Expense;
import com.finance.tracker.model.User;
import com.finance.tracker.model.enums.Category;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DashboardPanel extends JPanel {

    private final User user;

    private final ExpenseController expenseController;
    private final BudgetController budgetController;

    private JLabel greetingLabel;
    private JLabel totalExpenseLabel;
    private JProgressBar budgetBar;
    private JLabel insightLabel;
    private JTextArea logsArea;

    private final Map<Category, Double> categoryData = new HashMap<>();
    private JPanel pieChartPanel;

    private double totalSpent = 0;
    private double percentUsed = 0;
    private String logsText = "";

    public DashboardPanel(User user) {

        this.user = user;

        this.expenseController = new ExpenseController(user);
        this.budgetController = new BudgetController(user);

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        add(buildTop(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        renderEmptyState();
        loadData();
    }

    // ================= TOP =================
    private JPanel buildTop() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        greetingLabel = new JLabel("Welcome back, " + user.getName());
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadData());

        panel.add(greetingLabel, BorderLayout.WEST);
        panel.add(refreshBtn, BorderLayout.EAST);

        return panel;
    }

    // ================= CENTER =================
    private JPanel buildCenter() {

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setBackground(Color.WHITE);

       // ===== LEFT SIDE (EQUAL SIZE CARDS) =====
JPanel left = new JPanel();
left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

// common width behavior
int cardHeight = 90;

// ================= TOTAL EXPENSE CARD (SMALLER TEXT BUT SAME SIZE BOX) =================
totalExpenseLabel = new JLabel("KES 0.00");
totalExpenseLabel.setFont(new Font("Arial", Font.BOLD, 18));
totalExpenseLabel.setHorizontalAlignment(SwingConstants.CENTER);

JPanel expenseCard = new JPanel(new BorderLayout());
expenseCard.setBorder(BorderFactory.createTitledBorder("Expenses Incurred"));
expenseCard.add(totalExpenseLabel, BorderLayout.CENTER);

// FORCE SAME SIZE AS INSIGHT
expenseCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));
expenseCard.setPreferredSize(new Dimension(0, cardHeight));

// ================= INSIGHT CARD (EXPANDED VISUALLY BUT SAME BOX SIZE) =================
insightLabel = new JLabel("Waiting...");
insightLabel.setHorizontalAlignment(SwingConstants.CENTER);

JPanel insightPanel = new JPanel(new BorderLayout());
insightPanel.setBorder(BorderFactory.createTitledBorder("Insight"));
insightPanel.add(insightLabel, BorderLayout.CENTER);

// SAME SIZE AS EXPENSE CARD
insightPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, cardHeight));
insightPanel.setPreferredSize(new Dimension(0, cardHeight));

// ================= ADD TO LEFT =================
left.add(expenseCard);
left.add(Box.createVerticalStrut(5));
left.add(insightPanel);
        

        // ===== RIGHT SIDE (SMALLER PIE PANEL) =====
        pieChartPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                if (categoryData.isEmpty()) {
                    g2.setColor(Color.GRAY);
                    g2.drawString("No data yet", 40, 40);
                    return;
                }

                drawPieChart(g2);
            }
        };

        pieChartPanel.setPreferredSize(new Dimension(300, 250)); // 🔥 reduced size
        pieChartPanel.setBorder(BorderFactory.createTitledBorder("Spending Breakdown"));

        panel.add(left);
        panel.add(pieChartPanel);

        return panel;
    }

    // ================= BOTTOM =================
    private JPanel buildBottom() {

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Recent Activity"));

        logsArea = new JTextArea();
        logsArea.setEditable(false);

        // 🔥 moved up slightly (reduced height feel)
        logsArea.setRows(4);

        JPanel bottomWrapper = new JPanel(new BorderLayout());

        // 🔥 LABEL ABOVE PROGRESS BAR
        JLabel progressLabel = new JLabel("Total Budgets Progress Bar");
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.BOLD, 13));

        budgetBar = new JProgressBar(0, 100);
        budgetBar.setStringPainted(true);

        bottomWrapper.add(progressLabel, BorderLayout.NORTH);
        bottomWrapper.add(budgetBar, BorderLayout.CENTER);

        panel.add(new JScrollPane(logsArea), BorderLayout.CENTER);
        panel.add(bottomWrapper, BorderLayout.SOUTH);

        return panel;
    }

    // ================= EMPTY STATE =================
    private void renderEmptyState() {

        totalExpenseLabel.setText("KES 0.00");

        if (budgetBar != null) {
            budgetBar.setValue(0);
            budgetBar.setString("0% used");
        }

        if (logsArea != null) {
            logsArea.setText("No transactions yet.");
        }

        categoryData.clear();

        if (pieChartPanel != null) {
            pieChartPanel.repaint();
        }
    }

    // ================= LOAD DATA =================
    private void loadData() {

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            @Override
            protected Void doInBackground() {

                List<Expense> expenses = safeExpenses();
                List<Budget> budgets = safeBudgets();

                categoryData.clear();
                totalSpent = 0;

                StringBuilder logs = new StringBuilder();

                int month = LocalDateTime.now().getMonthValue();
                int year = LocalDateTime.now().getYear();

                int count = 0;

                for (Expense e : expenses) {

                    if (e == null || e.getCreatedAt() == null) continue;

                    LocalDateTime date = e.getCreatedAt();

                    if (date.getMonthValue() == month && date.getYear() == year) {

                        double amount = e.getAmount();
                        totalSpent += amount;

                        if (count < 3) {
                            logs.append("• ")
                                    .append(e.getName())
                                    .append(" - KES ")
                                    .append(String.format("%,.2f", amount))
                                    .append("\n");
                            count++;
                        }

                        if (e.getCategory() != null) {
                            categoryData.merge(e.getCategory(), amount, Double::sum);
                        }
                    }
                }

                logsText = logs.toString();

                double totalBudget = 0;
                for (Budget b : budgets) {
                    if (b != null) totalBudget += b.getAmount();
                }

                percentUsed = totalBudget == 0 ? 0 : (totalSpent / totalBudget) * 100;

                return null;
            }

            protected void done() {

                totalExpenseLabel.setText("KES " + String.format("%,.2f", totalSpent));

                budgetBar.setValue((int) Math.min(percentUsed, 100));
                budgetBar.setString(String.format("%.1f%% used", percentUsed));

                if (percentUsed >= 100) insightLabel.setText("Over budget");
                else if (percentUsed >= 80) insightLabel.setText("Near limit");
                else insightLabel.setText("Healthy");

                logsArea.setText(logsText.isEmpty()
                        ? "No recent expenses."
                        : logsText);

                pieChartPanel.repaint();
            }
        };

        worker.execute();
    }

    // ================= SAFE =================
    private List<Expense> safeExpenses() {
        try {
            List<Expense> list = expenseController.getMyExpenses();
            return list == null ? List.of() : list;
        } catch (Exception e) {
            return List.of();
        }
    }

    private List<Budget> safeBudgets() {
        try {
            List<Budget> list = budgetController.refreshBudgetSpending();
            return list == null ? List.of() : list;
        } catch (Exception e) {
            return List.of();
        }
    }

    private void drawPieChart(Graphics2D g2) {

        int width = pieChartPanel.getWidth();
        int height = pieChartPanel.getHeight();

        int legendWidth = 150;
        int size = Math.min(height - 60, width - legendWidth - 40);

        int centerX = (width - legendWidth) / 2;
        int centerY = height / 2;

        int x = centerX - size / 2;
        int y = centerY - size / 2;

        double total = categoryData.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        if (total == 0) return;

        int startAngle = 0;

        for (Map.Entry<Category, Double> entry : categoryData.entrySet()) {

            double value = entry.getValue();
            int angle = (int) ((value / total) * 360);

            g2.setColor(getColor(entry.getKey()));
            g2.fillArc(x, y, size, size, startAngle, angle);

            startAngle += angle;
        }

        int legendX = centerX + size / 2 + 20;
        int legendY = y + 20;

        int offsetY = 0;

        for (Map.Entry<Category, Double> entry : categoryData.entrySet()) {

            Category category = entry.getKey();
            double value = entry.getValue();
            double percent = (value / total) * 100;

            g2.setColor(getColor(category));
            g2.fillRect(legendX, legendY + offsetY, 12, 12);

            g2.setColor(Color.BLACK);
            g2.drawString(category.name() + " (" + String.format("%.1f%%", percent) + ")",
                    legendX + 18,
                    legendY + offsetY + 10);

            offsetY += 20;
        }
    }

    private Color getColor(Category c) {
        switch (c) {
            case FOOD: return Color.RED;
            case TRANSPORT: return Color.BLUE;
            case RENT: return Color.GREEN;
            case ENTERTAINMENT: return Color.ORANGE;
            default: return Color.GRAY;
        }
    }
}
