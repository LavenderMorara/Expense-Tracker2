package com.finance.tracker.model;

import com.finance.tracker.model.enums.Category;
import java.time.LocalDateTime;

public class Expense{
    private int id;
    private int userId;
    private Category category;
    private String name;
    private Double amount;
    private LocalDateTime createdAt;

    public Expense() {}

    public Expense(int userId, String name, Category category, Double amount, String createdAt) {
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.amount = amount;
    }

    public Expense(int id, int userId, String name, Category category, Double amount) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.amount = amount;
    }

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

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public Category getCategory() {
    return category;
}

public void setCategory(Category category) {
    this.category = category;
}

public Double getAmount() {
    return amount;
}

public void setAmount(Double amount) {
    this.amount = amount;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}
   
@Override
public String toString() {
    return "Expense{" +
            "id=" + id +
            ", userId=" + userId +
            ", name='" + name + '\'' +
            ", category=" + category +
            ", amount=" + amount +
            '}';
}
}
