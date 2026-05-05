package com.finance.tracker.repository;

import com.finance.tracker.model.Expense;
import com.finance.tracker.model.enums.Category;
import com.finance.tracker.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.*;


public class ExpenseRepository {

    public void addExpense(Expense expense) {

        String sql = "INSERT INTO expenses (user_id, name, category, amount) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, expense.getUserId());
            stmt.setString(2, expense.getName());
            stmt.setString(3, expense.getCategory().name());
            stmt.setDouble(4, expense.getAmount());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Expense> getExpensesByUser(int userId) {

        List<Expense> expenses = new ArrayList<>();

        String sql = "SELECT * FROM expenses WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Expense expense = new Expense();

                expense.setId(rs.getInt("id"));
                expense.setUserId(rs.getInt("user_id"));
                expense.setName(rs.getString("name"));
                expense.setCategory(Category.valueOf(rs.getString("category")));
                expense.setAmount(rs.getDouble("amount"));
                expense.setCreatedAt(
                rs.getTimestamp("created_at").toLocalDateTime()
                );           

                expenses.add(expense);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    public void updateExpense(Expense expense) {

    StringBuilder sql = new StringBuilder("UPDATE expenses SET ");
    List<Object> params = new ArrayList<>();

    if (expense.getName() != null) {
        sql.append("name = ?, ");
        params.add(expense.getName());
    }

    if (expense.getCategory() != null) {
        sql.append("category = ?, ");
        params.add(expense.getCategory().name());
    }

    if (expense.getAmount() != null) {
        sql.append("amount = ?, ");
        params.add(expense.getAmount());
    }

    if (params.isEmpty()) return;

    sql.setLength(sql.length() - 2);

    sql.append(" WHERE id = ? AND user_id = ?");
    params.add(expense.getId());
    params.add(expense.getUserId());

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

        for (int i = 0; i < params.size(); i++) {
            stmt.setObject(i + 1, params.get(i));
        }

        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void deleteExpense(int expenseId, int userId) {

        String sql = "DELETE FROM expenses WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, expenseId);
            stmt.setInt(2, userId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
