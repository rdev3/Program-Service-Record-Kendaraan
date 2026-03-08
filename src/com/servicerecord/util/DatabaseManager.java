package com.servicerecord.util;

import java.sql.*;

/**
 * Manages SQLite database connection and table initialization.
 * Uses Singleton pattern for single connection instance.
 *
 * @author Rangga
 * @version 1.0
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:service_record.db";
    private static DatabaseManager instance;
    private Connection connection;

    /**
     * Private constructor - Singleton pattern
     */
    private DatabaseManager() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            initializeTables();
            System.out.println("Database connected: " + DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }

    /**
     * Returns the singleton instance
     *
     * @return DatabaseManager instance
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Returns the active database connection
     *
     * @return Connection object
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Initializes all required tables if not exist
     */
    private void initializeTables() throws SQLException {
        String createVehicles = """
            CREATE TABLE IF NOT EXISTS vehicles (
                vehicle_id       TEXT PRIMARY KEY,
                license_plate    TEXT NOT NULL UNIQUE,
                owner_name       TEXT NOT NULL,
                owner_phone      TEXT,
                year             INTEGER,
                vehicle_type     TEXT NOT NULL,
                brand            TEXT,
                model            TEXT,
                transmission  TEXT,
                engine_cc     TEXT
            )
            """;

        String createServiceRecords = """
            CREATE TABLE IF NOT EXISTS service_records (
                service_id       INTEGER PRIMARY KEY AUTOINCREMENT,
                vehicle_id       TEXT NOT NULL,
                license_plate    TEXT NOT NULL,
                service_type     TEXT NOT NULL,
                description      TEXT,
                cost             REAL DEFAULT 0,
                mileage          INTEGER DEFAULT 0,
                technician_name  TEXT,
                service_date     TEXT NOT NULL,
                status           TEXT DEFAULT 'Menunggu',
                FOREIGN KEY(vehicle_id) REFERENCES vehicles(vehicle_id)
            )
            """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createVehicles);
            stmt.execute(createServiceRecords);
        }
    }

    /**
     * Closes the database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}