package com.finance.tracker.dto;

import com.finance.tracker.model.enums.Category;
import com.finance.tracker.model.enums.BudgetPeriod;

public class BudgetDTO {

    private int id;

    private Category category;
    private BudgetPeriod period;

    // planned budget
    private double amount;

    // actual spent (for dashboard display)
    private double spent;

    private int month;
    private int year;

    public BudgetDTO(int id,
                     Category category,
                     BudgetPeriod period,
                     double amount,
                     double spent,
                     int month,
                     int year) {

        this.id = id;
        this.category = category;
        this.period = period;
        this.amount = amount;
        this.spent = spent;
        this.month = month;
        this.year = year;
    }

    // ================= GETTERS =================

    public int getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public BudgetPeriod getPeriod() {
        return period;
    }

    public double getAmount() {
        return amount;
    }

    public double getSpent() {
        return spent;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    // ================= UI HELPER METHODS =================

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
        double usage = getUsagePercentage();
        return usage >= 80 && usage <= 100;
    }

    public String getStatus() {
        if (isExceeded()) return "EXCEEDED";
        if (isNearLimit()) return "WARNING";
        return "SAFE";
    }
}
