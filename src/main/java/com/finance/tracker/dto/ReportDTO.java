package com.finance.tracker.dto;

public class ReportDTO {

    private double totalBudget;
    private double totalSpent;
    private double remaining;
    private double usagePercent;
    private String status;

    public ReportDTO(double totalBudget, double totalSpent,
                     double remaining, double usagePercent,
                     String status) {
        this.totalBudget = totalBudget;
        this.totalSpent = totalSpent;
        this.remaining = remaining;
        this.usagePercent = usagePercent;
        this.status = status;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public double getRemaining() {
        return remaining;
    }

    public double getUsagePercent() {
        return usagePercent;
    }

    public String getStatus() {
        return status;
    }
}
