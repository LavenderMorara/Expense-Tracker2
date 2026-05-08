package com.finance.tracker;

import com.finance.tracker.util.DBConnection;
import java.sql.Connection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestConnection {

    @Test
    public void testDatabaseConnection() {
        Connection conn = DBConnection.getConnection();

        assertNotNull(conn, "Connection should not be null");

        System.out.println("Connected to Neon PostgreSQL!");
    }
}