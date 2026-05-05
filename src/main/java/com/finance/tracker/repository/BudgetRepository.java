package com.finance.tracker.repository;

import com.finance.tracker.model.Budget;
import com.finance.tracker.model.enums.Category;
import com.finance.tracker.model.enums.BudgetPeriod;
import com.finance.tracker.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetRepository {

    // ================= CREATE =================
    public void addBudget(Budget budget) {

        String sql = "INSERT INTO budgets (user_id, category, period, amount, month, year) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, budget.getUserId());

            if (budget.getCategory() != null) {
                stmt.setString(2, budget.getCategory().name());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }

            stmt.setString(3, budget.getPeriod().name());
            stmt.setDouble(4, budget.getAmount());
            stmt.setInt(5, budget.getMonth());
            stmt.setInt(6, budget.getYear());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= READ =================
    public List<Budget> getBudgetsByUser(int userId) {

        List<Budget> budgets = new ArrayList<>();

        String sql = "SELECT * FROM budgets WHERE user_id = ? ORDER BY year DESC, month DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Budget budget = new Budget();

                budget.setId(rs.getInt("id"));
                budget.setUserId(rs.getInt("user_id"));

                String category = rs.getString("category");
                if (category != null) {
                    budget.setCategory(Category.valueOf(category));
                }

                budget.setPeriod(BudgetPeriod.valueOf(rs.getString("period")));
                budget.setAmount(rs.getDouble("amount"));

                budget.setMonth(rs.getInt("month"));
                budget.setYear(rs.getInt("year"));

                // NOTE: spent is NOT stored here anymore
                budget.setSpent(0); // will be injected by service layer

                budgets.add(budget);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return budgets;
    }

    // ================= GET SINGLE =================
    public Budget getBudgetById(int budgetId, int userId) {

        String sql = "SELECT * FROM budgets WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, budgetId);
            stmt.setInt(2, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Budget budget = new Budget();

                budget.setId(rs.getInt("id"));
                budget.setUserId(rs.getInt("user_id"));

                String category = rs.getString("category");
                if (category != null) {
                    budget.setCategory(Category.valueOf(category));
                }

                budget.setPeriod(BudgetPeriod.valueOf(rs.getString("period")));
                budget.setAmount(rs.getDouble("amount"));

                budget.setMonth(rs.getInt("month"));
                budget.setYear(rs.getInt("year"));

                return budget;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= DELETE =================
    public void deleteBudget(int budgetId, int userId) {

        String sql = "DELETE FROM budgets WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, budgetId);
            stmt.setInt(2, userId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= ANALYTICS SUPPORT =================
    // (optional but recommended for dashboard scaling)

    public double getTotalBudgetByUser(int userId) {

        String sql = "SELECT SUM(amount) FROM budgets WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
