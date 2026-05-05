package com.finance.tracker.controller;

import com.finance.tracker.service.ReportService;
import com.finance.tracker.model.enums.Category;

import java.util.Map;

public class ReportController {

    private final ReportService reportService = new ReportService();

    public ReportController() {}

    // ================= TOTAL =================
    public double getTotalExpenses(int userId) {
        return reportService.getTotalExpenses(userId);
    }

    // ================= MONTHLY =================
    public double getMonthlyExpenses(int userId, int month, int year) {
        return reportService.getMonthlyExpenses(userId, month, year);
    }

    // ================= CATEGORY DETAIL PIE =================
    public Map<String, Double> getCategoryDetailBreakdown(int userId, Category category) {
        return reportService.getExpensesByCategoryDetailed(userId, category);
    }

    // ================= DEFAULT CATEGORY =================
    public Category getDefaultCategory(int userId) {
        return reportService.getDefaultCategory(userId);
    }

    // ================= CATEGORY BUDGET REPORT =================
    public Map<String, Object> getCategoryBudgetReport(
            int userId,
            int month,
            int year,
            Category category
    ) {
        return reportService.getCategoryBudgetReport(userId, month, year, category);
    }

    // ================= CATEGORY PROJECTIONS =================
    public Map<String, Double> getCategoryProjections(
            int userId,
            int month,
            int year,
            Category category
    ) {
        return reportService.getCategoryProjections(userId, month, year, category);
    }
}
