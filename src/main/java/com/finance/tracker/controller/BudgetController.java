package com.finance.tracker.controller;

import com.finance.tracker.model.Budget;
import com.finance.tracker.model.enums.BudgetPeriod;
import com.finance.tracker.model.enums.Category;
import com.finance.tracker.model.User;
import com.finance.tracker.service.BudgetService;

import java.util.List;

public class BudgetController {

    private BudgetService budgetService;

    public BudgetController(User loggedInUser) {
        this.budgetService = new BudgetService(loggedInUser);
    }

    // ================= CREATE =================
    public boolean createBudget(Category category,
                                BudgetPeriod period,
                                double amount,
                                int month,
                                int year) {

        return budgetService.createBudget(category, period, amount, month, year);
    }

    // ================= READ =================
    public List<Budget> getMyBudgets() {
        return budgetService.getMyBudgets();
    }

    // ================= DELETE =================
    public boolean deleteBudget(int budgetId) {
        return budgetService.deleteBudget(budgetId);
    }

    // ================= FINANCE LOGIC (NEW) =================

    // Get updated budget with computed/updated spent value
    public Budget getBudgetWithSpent(int budgetId) {
        return budgetService.getBudgetWithSpent(budgetId);
    }

    // Refresh all budgets with correct spent values (for dashboard refresh)
    public List<Budget> refreshBudgetSpending() {
        return budgetService.refreshBudgetSpending();
    }

    // Check if budget is exceeded (for alerts)
    public boolean isBudgetExceeded(int budgetId) {
        return budgetService.isBudgetExceeded(budgetId);
    }

    // Check if budget is near limit (80% rule)
    public boolean isBudgetNearLimit(int budgetId) {
        return budgetService.isBudgetNearLimit(budgetId);
    }

    // Get total spent for dashboard bar/pie chart
    public double getTotalSpent(int userId) {
        return budgetService.getTotalSpent(userId);
    }
}
