package com.finance.tracker.service;

import com.finance.tracker.model.Expense;
import com.finance.tracker.model.User;
import com.finance.tracker.model.enums.Category;
import com.finance.tracker.repository.ExpenseRepository;

import java.util.List;

public class ExpenseService {

    private ExpenseRepository expenseRepository = new ExpenseRepository();

    private User loggedInUser;

    public ExpenseService(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public boolean addExpense(String name, Category category, double amount) {

        if (loggedInUser == null) return false;
        if (name == null || name.trim().isEmpty()) return false;
        if (amount <= 0) return false;

        Expense expense = new Expense();
        expense.setUserId(loggedInUser.getId());
        expense.setName(name);
        expense.setCategory(category);
        expense.setAmount(amount);

        expenseRepository.addExpense(expense);

        return true;
    }

    public List<Expense> getMyExpenses() {

        if (loggedInUser == null) return null;

        return expenseRepository.getExpensesByUser(loggedInUser.getId());
    }

    public boolean updateExpense(int expenseId, String name, Category category, Double amount) {

        if (loggedInUser == null) return false;

        Expense expense = new Expense();
        expense.setId(expenseId);
        expense.setUserId(loggedInUser.getId());

        if (name != null && !name.trim().isEmpty()) {
            expense.setName(name);
        }

        if (category != null) {
            expense.setCategory(category);
        }

        if (amount != null && amount > 0) {
            expense.setAmount(amount);
        }

        expenseRepository.updateExpense(expense);

        return true;
    }

    public boolean deleteExpense(int expenseId) {

        if (loggedInUser == null) return false;

        expenseRepository.deleteExpense(expenseId, loggedInUser.getId());

        return true;
    }
}
