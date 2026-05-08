package com.finance.tracker.ui.main;

import com.finance.tracker.controller.ReportController;
import com.finance.tracker.model.User;
import com.finance.tracker.model.enums.Category;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class ReportPanel extends JPanel {

    private final User user;
    private final ReportController reportController;

    private JLabel totalExpensesLabel;
    private JLabel insightLabel;

    private JTextArea budgetArea;
    private JTextArea projectionArea;

    private JComboBox<Integer> monthBox;
    private JComboBox<Integer> yearBox;

    private JComboBox<Category> budgetCategoryBox;
    private JComboBox<Category> pieCategoryBox;

    private JPanel pieChartPanel;

    private Map<String, Double> pieData = new LinkedHashMap<>();
    private Map<String, Color> colorMap = new LinkedHashMap<>();

    private volatile boolean isLoading = false;

    public ReportPanel(User user) {

        this.user = user;
        this.reportController = new ReportController();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);

        initializeDefaults();
        setCurrentDateDefaults();
        loadReports();
    }

    // ================= INIT =================
    private void initializeDefaults() {
        Category defaultCat = reportController.getDefaultCategory(user.getId());
        budgetCategoryBox.setSelectedItem(defaultCat);
        pieCategoryBox.setSelectedItem(defaultCat);
    }

    private void setCurrentDateDefaults() {
        java.time.LocalDate now = java.time.LocalDate.now();
        monthBox.setSelectedItem(now.getMonthValue());
        yearBox.setSelectedItem(now.getYear());
    }

    // ================= TOP =================
    private JPanel buildTopPanel() {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Filters"));

        monthBox = new JComboBox<>(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12});
        yearBox = new JComboBox<>(new Integer[]{2024, 2025, 2026});
        budgetCategoryBox = new JComboBox<>(Category.values());

        JButton refreshBtn = new JButton("Refresh");

        ActionListener reload = e -> loadReports();

        monthBox.addActionListener(reload);
        yearBox.addActionListener(reload);
        budgetCategoryBox.addActionListener(reload);
        refreshBtn.addActionListener(reload);

        panel.add(new JLabel("Month"));
        panel.add(monthBox);
        panel.add(new JLabel("Year"));
        panel.add(yearBox);
        panel.add(new JLabel("Budget Category"));
        panel.add(budgetCategoryBox);
        panel.add(refreshBtn);

        return panel;
    }

    // ================= CENTER =================
    private JPanel buildCenterPanel() {

        JPanel main = new JPanel(new BorderLayout(10, 10));

        JPanel content = new JPanel(new GridLayout(1, 2, 10, 10));

        // ===== LEFT =====
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        totalExpensesLabel = new JLabel("Total: ...");
        totalExpensesLabel.setFont(new Font("Arial", Font.BOLD, 14));

        budgetArea = new JTextArea(5, 20);
        projectionArea = new JTextArea(5, 20);

        budgetArea.setEditable(false);
        projectionArea.setEditable(false);

        left.add(totalExpensesLabel);

        left.add(Box.createVerticalStrut(10));
        left.add(new JLabel("Budget"));
        left.add(new JScrollPane(budgetArea));

        left.add(Box.createVerticalStrut(10));
        left.add(new JLabel("Projections"));
        left.add(new JScrollPane(projectionArea));

        // ===== RIGHT (PIE) =====
        JPanel pieCard = new JPanel(new BorderLayout());
        pieCard.setBorder(BorderFactory.createTitledBorder("Expense Breakdown"));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        pieCategoryBox = new JComboBox<>(Category.values());
        pieCategoryBox.addActionListener(e -> loadReports());

        topBar.add(new JLabel("Category:"));
        topBar.add(pieCategoryBox);

        pieChartPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (isLoading) {
                    g.drawString("Loading...", 40, 40);
                    return;
                }

                if (pieData.isEmpty()) {
                    g.drawString("No data", 40, 40);
                    return;
                }

                drawPieChart((Graphics2D) g);
            }
        };

        pieCard.add(topBar, BorderLayout.NORTH);
        pieCard.add(pieChartPanel, BorderLayout.CENTER);

        content.add(left);
        content.add(pieCard);

        // ===== INSIGHT BAR =====
        insightLabel = new JLabel("Weekly Budget Progress: ...");
        insightLabel.setOpaque(true);
        insightLabel.setFont(new Font("Arial", Font.BOLD, 14));
        insightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        insightLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        main.add(content, BorderLayout.CENTER);
        main.add(insightLabel, BorderLayout.SOUTH);

        return main;
    }

    // ================= LOAD =================
    private void loadReports() {

        isLoading = true;
        repaint();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            double total;
            Map<String, Object> budgetData;
            Map<String, Double> projections;

            protected Void doInBackground() {

                int userId = user.getId();
                int month = (int) monthBox.getSelectedItem();
                int year = (int) yearBox.getSelectedItem();

                Category budgetCategory = (Category) budgetCategoryBox.getSelectedItem();
                Category pieCategory = (Category) pieCategoryBox.getSelectedItem();

                total = reportController.getTotalExpenses(userId);

                pieData = reportController.getCategoryDetailBreakdown(userId, pieCategory);
                generateColors();

                budgetData = reportController.getCategoryBudgetReport(userId, month, year, budgetCategory);
                projections = reportController.getCategoryProjections(userId, month, year, budgetCategory);

                return null;
            }

            protected void done() {

                totalExpensesLabel.setText("Total: KES " + String.format("%,.2f", total));

                double budget = (double) budgetData.getOrDefault("budget", 0.0);
                double spent = (double) budgetData.getOrDefault("spent", 0.0);

                if (budget <= 0) {
                    budgetArea.setText("No budget set.");
                    projectionArea.setText("No projections.");
                    setInsight("No budget data available.", Color.GRAY);
                } else {

                    budgetArea.setText(
                            "Budget: " + budget + "\n" +
                            "Spent: " + spent + "\n" +
                            "Remaining: " + budgetData.get("remaining") + "\n" +
                            "Usage: " + budgetData.get("usagePercent") + "%\n"
                    );

                    double weekly = projections.getOrDefault("Weekly", 0.0);

                    projectionArea.setText(
                            "Weekly: " + weekly + "\n" +
                            "Monthly: " + projections.get("Monthly") + "\n" +
                            "Quarterly: " + projections.get("Quarterly") + "\n" +
                            "Biannual: " + projections.get("Biannual") + "\n" +
                            "Annual: " + projections.get("Annual")
                    );

                    // 🔥 SMART WEEKLY INSIGHT
                    if (weekly <= 0) {
                        setInsight("No weekly projection available.", Color.GRAY);
                    } else {

                        double percent = (spent / weekly) * 100;

                        if (percent >= 100) {
                            setInsight(String.format("🚨 %.1f%% used — OVER budget. Cut spending immediately.", percent), Color.RED);

                        } else if (percent >= 85) {
                            setInsight(String.format("⚠️ %.1f%% used — nearing limit. Reduce non-essential expenses.", percent), Color.ORANGE);

                        } else if (percent >= 60) {
                            setInsight(String.format("⚠️ %.1f%% used — spending is fast. Monitor closely.", percent), Color.ORANGE);

                        } else {
                            setInsight(String.format("✅ %.1f%% used — on track. Maintain discipline.", percent), new Color(0,153,0));
                        }
                    }
                }

                isLoading = false;
                repaint();
            }
        };

        worker.execute();
    }

    // ================= INSIGHT =================
    private void setInsight(String text, Color bg) {
        insightLabel.setText("Weekly Budget Progress: " + text);
        insightLabel.setBackground(bg);
        insightLabel.setForeground(Color.WHITE);
    }

    // ================= PIE =================
    private void drawPieChart(Graphics2D g2) {

        int size = 200;
        int x = 40;
        int y = 40;

        double total = pieData.values().stream().mapToDouble(Double::doubleValue).sum();
        if (total <= 0) return;

        int start = 0;

        for (Map.Entry<String, Double> e : pieData.entrySet()) {

            int angle = (int) ((e.getValue() / total) * 360);

            g2.setColor(colorMap.get(e.getKey()));
            g2.fillArc(x, y, size, size, start, angle);

            start += angle;
        }

        // LEGEND
        int lx = x + size + 20;
        int ly = y;
        int offset = 0;

        for (Map.Entry<String, Double> e : pieData.entrySet()) {

            g2.setColor(colorMap.get(e.getKey()));
            g2.fillRect(lx, ly + offset, 12, 12);

            g2.setColor(Color.BLACK);
            g2.drawString(e.getKey(), lx + 20, ly + offset + 10);

            offset += 20;
        }
    }

    private void generateColors() {

        colorMap.clear();
        Random rand = new Random();

        for (String key : pieData.keySet()) {
            colorMap.put(key, new Color(rand.nextInt(200), rand.nextInt(200), rand.nextInt(200)));
        }
    }
}
