package com.finance.tracker.dto;

import com.finance.tracker.model.enums.Category;

public class ExpenseDTO {

    private int id;
    private String name;
    private Category category;
    private double amount;

    public ExpenseDTO(int id, String name, Category category, double amount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}
