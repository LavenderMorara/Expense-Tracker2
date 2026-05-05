package com.finance.tracker.controller;

import com.finance.tracker.model.Expense;
import com.finance.tracker.model.enums.Category;
import com.finance.tracker.model.User;
import com.finance.tracker.service.ExpenseService;

import java.util.List;

public class ExpenseController {

    private ExpenseService expenseService;

    public ExpenseController(User loggedInUser) {
        try {
            this.expenseService = new ExpenseService(loggedInUser);
        } catch (Exception e) {
            System.out.println("ERROR initializing ExpenseService:");
            e.printStackTrace();
        }
    }

    public boolean addExpense(String name, Category category, double amount) {
        try {
            return expenseService != null &&
                   expenseService.addExpense(name, category, amount);
        } catch (Exception e) {
            System.out.println("ERROR adding expense:");
            e.printStackTrace();
            return false;
        }
    }

    public List<Expense> getMyExpenses() {
        try {
            return expenseService != null
                    ? expenseService.getMyExpenses()
                    : List.of();
        } catch (Exception e) {
            System.out.println("ERROR fetching expenses:");
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean updateExpense(int expenseId, String name, Category category, Double amount) {
        try {
            return expenseService != null &&
                   expenseService.updateExpense(expenseId, name, category, amount);
        } catch (Exception e) {
            System.out.println("ERROR updating expense:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteExpense(int expenseId) {
        try {
            return expenseService != null &&
                   expenseService.deleteExpense(expenseId);
        } catch (Exception e) {
            System.out.println("ERROR deleting expense:");
            e.printStackTrace();
            return false;
        }
    }
}
