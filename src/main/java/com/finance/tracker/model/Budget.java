package com.finance.tracker.model;

import com.finance.tracker.model.enums.Category;
import com.finance.tracker.model.enums.BudgetPeriod;

public class Budget {

    private int id;
    private int userId;

    private Category category;
    private BudgetPeriod period;

    // planned budget
    private double amount;

    // actual spent (can be computed OR set by service layer)
    private double spent;

    private int month;
    private int year;

    public Budget() {}

    // CREATE (no id yet)
    public Budget(int userId,
                  Category category,
                  BudgetPeriod period,
                  double amount,
                  double spent,
                  int month,
                  int year) {

        this.userId = userId;
        this.category = category;
        this.period = period;
        this.amount = amount;
        this.spent = spent;
        this.month = month;
        this.year = year;
    }

    // FULL (from DB)
    public Budget(int id,
                  int userId,
                  Category category,
                  BudgetPeriod period,
                  double amount,
                  double spent,
                  int month,
                  int year) {

        this.id = id;
        this.userId = userId;
        this.category = category;
        this.period = period;
        this.amount = amount;
        this.spent = spent;
        this.month = month;
        this.year = year;
    }

    // ================= GETTERS & SETTERS =================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BudgetPeriod getPeriod() {
        return period;
    }

    public void setPeriod(BudgetPeriod period) {
        this.period = period;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    // ================= BUSINESS HELPERS =================

    public double getRemaining() {
        return amount - spent;
    }

    public double getUsagePercentage() {
        if (amount == 0) return 0;
        return (spent / amount) * 100;
    }

    public boolean isExceeded() {
        return spent > amount;
    }

    public boolean isNearLimit() {
        return getUsagePercentage() >= 80 && !isExceeded();
    }

    // ================= DEBUG =================

    @Override
    public String toString() {
        return "Budget{" +
                "id=" + id +
                ", userId=" + userId +
                ", category=" + category +
                ", period=" + period +
                ", amount=" + amount +
                ", spent=" + spent +
                ", remaining=" + getRemaining() +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
