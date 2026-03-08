package com.servicerecord.db;

import com.servicerecord.model.ServiceRecord;
import com.servicerecord.util.DatabaseManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for ServiceRecord entities.
 *
 * @author YourName
 * @version 1.0
 */
public class ServiceRecordDB {

    private final Connection conn;

    public ServiceRecordDB() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Inserts a new service record.
     *
     * @param record ServiceRecord to insert
     * @return Generated service ID, or -1 if failed
     */
    public int insertServiceRecord(ServiceRecord record) {
        String sql = """
            INSERT INTO service_records
              (vehicle_id, license_plate, service_type, description,
               cost, mileage, technician_name, service_date, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, record.getVehicleId());
            ps.setString(2, record.getVehicleLicensePlate());
            ps.setString(3, record.getServiceType());
            ps.setString(4, record.getDescription());
            ps.setDouble(5, record.getCost());
            ps.setInt   (6, record.getMileage());
            ps.setString(7, record.getTechnicianName());
            ps.setString(8, record.getServiceDate().toString());
            ps.setString(9, record.getStatus());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.err.println("insertServiceRecord error: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Retrieves all service records.
     *
     * @return List of all ServiceRecord objects
     */
    public List<ServiceRecord> getAllRecords() {
        List<ServiceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM service_records ORDER BY service_date DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) {
            System.err.println("getAllRecords error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves service records for a specific vehicle.
     *
     * @param vehicleId Vehicle ID to filter by
     * @return List of matching ServiceRecord objects
     */
    public List<ServiceRecord> getRecordsByVehicle(String vehicleId) {
        List<ServiceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM service_records WHERE vehicle_id = ? ORDER BY service_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSet(rs));
        } catch (SQLException e) {
            System.err.println("getRecordsByVehicle error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Updates the status of a service record.
     *
     * @param serviceId Service record ID
     * @param newStatus New status value
     * @return true if successful
     */
    public boolean updateStatus(int serviceId, String newStatus) {
        String sql = "UPDATE service_records SET status = ? WHERE service_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt   (2, serviceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updateStatus error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a service record by ID.
     *
     * @param serviceId ID to delete
     * @return true if successful
     */
    public boolean deleteRecord(int serviceId) {
        String sql = "DELETE FROM service_records WHERE service_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("deleteRecord error: " + e.getMessage());
            return false;
        }
    }

    // ===================== PRIVATE HELPERS =====================

    private ServiceRecord mapResultSet(ResultSet rs) throws SQLException {
        return new ServiceRecord(
            rs.getInt   ("service_id"),
            rs.getString("vehicle_id"),
            rs.getString("license_plate"),
            rs.getString("service_type"),
            rs.getString("description"),
            rs.getDouble("cost"),
            rs.getInt   ("mileage"),
            rs.getString("technician_name"),
            LocalDate.parse(rs.getString("service_date")),
            rs.getString("status")
        );
    }
}