package com.finance.tracker.service;

import com.finance.tracker.model.Budget;
import com.finance.tracker.model.Expense;
import com.finance.tracker.model.enums.Category;
import com.finance.tracker.model.enums.BudgetPeriod;
import com.finance.tracker.repository.ExpenseRepository;
import com.finance.tracker.repository.BudgetRepository;

import java.util.*;
import java.time.LocalDateTime;

public class ReportService {

    private ExpenseRepository expenseRepository = new ExpenseRepository();
    private BudgetRepository budgetRepository = new BudgetRepository();

    public ReportService() {}

    // ================= TOTAL =================
    public double getTotalExpenses(int userId) {
        List<Expense> expenses = expenseRepository.getExpensesByUser(userId);
        if (expenses == null) return 0;

        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        return total;
    }

    // ================= MONTHLY =================
    public double getMonthlyExpenses(int userId, int month, int year) {

        List<Expense> expenses = expenseRepository.getExpensesByUser(userId);
        if (expenses == null) return 0;

        double total = 0;

        for (Expense e : expenses) {

            if (e.getCreatedAt() == null) continue;

            LocalDateTime d = e.getCreatedAt();

            if (d.getMonthValue() == month && d.getYear() == year) {
                total += e.getAmount();
            }
        }

        return total;
    }

    // ================= CATEGORY DETAIL =================
    public Map<String, Double> getExpensesByCategoryDetailed(int userId, Category category) {

        List<Expense> expenses = expenseRepository.getExpensesByUser(userId);
        Map<String, Double> result = new LinkedHashMap<>();

        if (expenses == null || category == null) return result;

        for (Expense e : expenses) {

            if (e.getCategory() != category) continue;

            String name = e.getName() == null ? "Unnamed" : e.getName();

            result.put(name,
                    result.getOrDefault(name, 0.0) + e.getAmount());
        }

        return result;
    }

    // ================= DEFAULT CATEGORY =================
    public Category getDefaultCategory(int userId) {

        List<Expense> expenses = expenseRepository.getExpensesByUser(userId);

        if (expenses == null || expenses.isEmpty()) {
            return Category.FOOD;
        }

        for (Expense e : expenses) {
            if (e.getCategory() != null) return e.getCategory();
        }

        return Category.FOOD;
    }

    // ================= NORMALIZED BUDGET CALC =================
    private double normalizeBudgetToMonthly(Budget b) {

        if (b.getPeriod() == null) return b.getAmount();

        switch (b.getPeriod()) {

            case WEEKLY:
                return b.getAmount() * 4.0; // 4 weeks avg

            case MONTHLY:
                return b.getAmount();

            default:
                return b.getAmount(); // fallback
        }
    }

    // ================= CATEGORY BUDGET REPORT =================
    public Map<String, Object> getCategoryBudgetReport(int userId, int month, int year, Category category) {

        List<Budget> budgets = budgetRepository.getBudgetsByUser(userId);
        List<Expense> expenses = expenseRepository.getExpensesByUser(userId);

        double budgetAmount = 0;
        double spent = 0;

        // EXPENSES
        if (expenses != null) {
            for (Expense e : expenses) {

                if (e.getCategory() != category) continue;
                if (e.getCreatedAt() == null) continue;

                LocalDateTime d = e.getCreatedAt();

                if (d.getMonthValue() == month && d.getYear() == year) {
                    spent += e.getAmount();
                }
            }
        }

        // BUDGETS (🔥 FIXED: PERIOD NORMALIZATION)
        if (budgets != null) {
            for (Budget b : budgets) {

                if (b.getCategory() != category) continue;
                if (b.getMonth() != month || b.getYear() != year) continue;

                budgetAmount += normalizeBudgetToMonthly(b);
            }
        }

        double remaining = budgetAmount - spent;
        double usage = (budgetAmount == 0) ? 0 : (spent / budgetAmount) * 100;

        String status = usage < 80 ? "SAFE" : usage < 100 ? "WARNING" : "EXCEEDED";

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("budget", budgetAmount);
        result.put("spent", spent);
        result.put("remaining", remaining);
        result.put("usagePercent", usage);
        result.put("status", status);

        return result;
    }

    // ================= CATEGORY PROJECTIONS =================
    public Map<String, Double> getCategoryProjections(int userId, int month, int year, Category category) {

        Map<String, Object> report = getCategoryBudgetReport(userId, month, year, category);

        double budget = (double) report.getOrDefault("budget", 0.0);

        Map<String, Double> projections = new LinkedHashMap<>();

        if (budget <= 0) {
            projections.put("Weekly", 0.0);
            projections.put("Monthly", 0.0);
            projections.put("Quarterly", 0.0);
            projections.put("Biannual", 0.0);
            projections.put("Annual", 0.0);
            return projections;
        }

        double weekly = budget / 4.0;
        double daily = weekly / 7.0;

        projections.put("Weekly", weekly);
        projections.put("Monthly", daily * 30);
        projections.put("Quarterly", daily * 90);
        projections.put("Biannual", daily * 180);
        projections.put("Annual", daily * 365);

        return projections;
    }
}
