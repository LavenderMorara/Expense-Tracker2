package com.finance.tracker.service;

import com.finance.tracker.model.Budget;
import com.finance.tracker.model.User;
import com.finance.tracker.model.enums.BudgetPeriod;
import com.finance.tracker.model.enums.Category;
import com.finance.tracker.repository.BudgetRepository;
import com.finance.tracker.repository.ExpenseRepository;

import java.util.List;

public class BudgetService {

    private BudgetRepository budgetRepository = new BudgetRepository();
    private ExpenseRepository expenseRepository = new ExpenseRepository();

    private User loggedInUser;

    public BudgetService(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    // ================= CREATE =================
    public boolean createBudget(Category category,
                                BudgetPeriod period,
                                double amount,
                                int month,
                                int year) {

        if (loggedInUser == null) return false;
        if (amount <= 0) return false;

        Budget budget = new Budget();
        budget.setUserId(loggedInUser.getId());
        budget.setCategory(category);
        budget.setPeriod(period);
        budget.setAmount(amount);
        budget.setMonth(month);
        budget.setYear(year);

        budgetRepository.addBudget(budget);

        return true;
    }

    // ================= READ =================
    public List<Budget> getMyBudgets() {

        if (loggedInUser == null) return null;

        List<Budget> budgets =
                budgetRepository.getBudgetsByUser(loggedInUser.getId());

        // inject spent values dynamically
        for (Budget b : budgets) {
            b.setSpent(calculateSpent(b));
        }

        return budgets;
    }

    // ================= DELETE =================
    public boolean deleteBudget(int budgetId) {

        if (loggedInUser == null) return false;

        budgetRepository.deleteBudget(budgetId, loggedInUser.getId());

        return true;
    }

    // ================= CORE FINANCE LOGIC =================

    // 🔥 Calculate spent from expenses (KEY FUNCTION)
    private double calculateSpent(Budget budget) {

    if (loggedInUser == null) return 0;

    return expenseRepository.getExpensesByUser(loggedInUser.getId())
            .stream()
            .filter(e -> e.getCategory() == budget.getCategory())
            .filter(e -> {
                int m = e.getCreatedAt().getMonthValue();
                int y = e.getCreatedAt().getYear();
                return m == budget.getMonth() && y == budget.getYear();
            })
            .mapToDouble(e -> e.getAmount())
            .sum();
}

    // ================= DASHBOARD SUPPORT =================

    public Budget getBudgetWithSpent(int budgetId) {

        Budget budget =
                budgetRepository.getBudgetById(budgetId, loggedInUser.getId());

        if (budget == null) return null;

        budget.setSpent(calculateSpent(budget));

        return budget;
    }

    public List<Budget> refreshBudgetSpending() {

        return getMyBudgets();
    }

    public boolean isBudgetExceeded(int budgetId) {

        Budget b = getBudgetWithSpent(budgetId);
        if (b == null) return false;

        return b.getSpent() > b.getAmount();
    }

    public boolean isBudgetNearLimit(int budgetId) {

        Budget b = getBudgetWithSpent(budgetId);
        if (b == null) return false;

        double usage = (b.getSpent() / b.getAmount()) * 100;

        return usage >= 80 && usage <= 100;
    }

    public double getTotalSpent(int userId) {

       return expenseRepository.getExpensesByUser(userId)
        .stream()
        .mapToDouble(e -> e.getAmount())
        .sum();
    }
}
