package com.servicerecord.db;

import com.servicerecord.model.*;
import com.servicerecord.util.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Vehicle entities.
 * Handles all CRUD operations for vehicles in the database.
 *
 * @author Rangga
 * @version 1.0
 */
public class VehicleDB {

    private final Connection conn;

    public VehicleDB() {
        this.conn = DatabaseManager.getInstance().getConnection();
    }

    /**
     * Inserts a new vehicle into the database.
     *
     * @param vehicle Vehicle to insert (Car or Motorcycle)
     * @return true if successful
     */
    public boolean insertVehicle(Vehicle vehicle) {
        String sql = """
            INSERT INTO vehicles
              (vehicle_id, license_plate, owner_name, owner_phone,
               year, vehicle_type, brand, model, transmission, engine_cc)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicle.getVehicleId());
            ps.setString(2, vehicle.getLicensePlate());
            ps.setString(3, vehicle.getOwnerName());
            ps.setString(4, vehicle.getOwnerPhone());
            ps.setInt   (5, vehicle.getYear());
            ps.setString(6, vehicle.getVehicleType());

            // Polymorphism - check type at runtime
            if (vehicle instanceof Car car) {
                ps.setString(7, car.getBrand());
                ps.setString(8, car.getModel());
                ps.setString(9, car.getTransmission());
                ps.setString(10, car.getEngineCC());
            } else if (vehicle instanceof Motorcycle moto) {
                ps.setString(7, moto.getBrand());
                ps.setString(8, moto.getModel());
                ps.setString(9, moto.getTransmission());
                ps.setString(9, moto.getEngineCC());
            } else {
                ps.setString(7, "");
                ps.setString(8, "");
                ps.setString(9, "");
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("insertVehicle error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all vehicles from the database.
     *
     * @return List of Vehicle objects
     */
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY owner_name";

        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vehicle v = buildVehicleFromRS(rs);
                if (v != null) list.add(v);
            }
        } catch (SQLException e) {
            System.err.println("getAllVehicles error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Finds a vehicle by license plate.
     *
     * @param plate License plate to search
     * @return Vehicle if found, null otherwise
     */
    public Vehicle findByLicensePlate(String plate) {
        String sql = "SELECT * FROM vehicles WHERE license_plate = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plate.toUpperCase());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return buildVehicleFromRS(rs);
        } catch (SQLException e) {
            System.err.println("findByLicensePlate error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates existing vehicle data.
     *
     * @param vehicle Vehicle with updated data
     * @return true if successful
     */
    public boolean updateVehicle(Vehicle vehicle) {
        String sql = """
            UPDATE vehicles SET
              license_plate = ?, owner_name = ?, owner_phone = ?,
              year = ?, brand = ?, model = ?, transmission = ?, engine_cc = ?
            WHERE vehicle_id = ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicle.getLicensePlate());
            ps.setString(2, vehicle.getOwnerName());
            ps.setString(3, vehicle.getOwnerPhone());
            ps.setInt   (4, vehicle.getYear());

            if (vehicle instanceof Car car) {
                ps.setString(5, car.getBrand());
                ps.setString(6, car.getModel());
                ps.setString(7, car.getTransmission());
                ps.setString(8, car.getEngineCC());
            } else if (vehicle instanceof Motorcycle moto) {
                ps.setString(5, moto.getBrand());
                ps.setString(6, moto.getModel());
                ps.setString(7, moto.getTransmission());
                ps.setString(8, moto.getEngineCC());
            } else {
                ps.setString(5, ""); ps.setString(6, ""); 
                ps.setString(7, ""); ps.setString(8, "");
            }

            ps.setString(9, vehicle.getVehicleId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("updateVehicle error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a vehicle by ID.
     *
     * @param vehicleId ID of vehicle to delete
     * @return true if successful
     */
    public boolean deleteVehicle(String vehicleId) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehicleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("deleteVehicle error: " + e.getMessage());
            return false;
        }
    }

    // ===================== PRIVATE HELPERS =====================

    private Vehicle buildVehicleFromRS(ResultSet rs) throws SQLException {
        String type = rs.getString("vehicle_type");

        if ("Mobil".equals(type)) {
            Car car = new Car();
            mapBaseFields(car, rs);
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            car.setTransmission(rs.getString("transmission"));
            car.setEngineCC(rs.getString("engine_cc"));
            return car;
        } else if ("Motor".equals(type)) {
            Motorcycle moto = new Motorcycle();
            mapBaseFields(moto, rs);
            moto.setBrand(rs.getString("brand"));
            moto.setModel(rs.getString("model"));
            moto.setTransmission(rs.getString("transmission"));
            moto.setEngineCC(rs.getString("engine_cc"));
            return moto;
        }
        return null;
    }

    private void mapBaseFields(Vehicle v, ResultSet rs) throws SQLException {
        v.setVehicleId   (rs.getString("vehicle_id"));
        v.setLicensePlate(rs.getString("license_plate"));
        v.setOwnerName   (rs.getString("owner_name"));
        v.setOwnerPhone  (rs.getString("owner_phone"));
        v.setYear        (rs.getInt("year"));
    }
}