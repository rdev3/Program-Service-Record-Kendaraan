package com.servicerecord.service;

import com.servicerecord.db.*;
import com.servicerecord.model.*;
import com.servicerecord.util.Printable;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Business logic layer for managing vehicles and service records.
 * Implements Printable interface for export functionality.
 *
 * @author Rangga
 * @version 1.0
 */
public class ServiceRecordManager implements Printable {

    private final VehicleDB       vehicleDB;
    private final ServiceRecordDB serviceRecordDB;

    public ServiceRecordManager() {
        this.vehicleDB       = new VehicleDB();
        this.serviceRecordDB = new ServiceRecordDB();
    }

    // ===================== VEHICLE METHODS =====================

    /**
     * Registers a new vehicle with validation.
     *
     * @param vehicle Vehicle to register
     * @return Result message
     */
    public String registerVehicle(Vehicle vehicle) {
        // Validation (if-else structure)
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().isBlank()) {
            return "ERROR: Plat nomor tidak boleh kosong!";
        }
        if (vehicle.getOwnerName() == null || vehicle.getOwnerName().isBlank()) {
            return "ERROR: Nama pemilik tidak boleh kosong!";
        }
        if (vehicle.getYear() < 1900 || vehicle.getYear() > LocalDate.now().getYear()) {
            return "ERROR: Tahun kendaraan tidak valid!";
        }

        // Check duplicate
        if (vehicleDB.findByLicensePlate(vehicle.getLicensePlate()) != null) {
            return "ERROR: Plat nomor sudah terdaftar!";
        }

        boolean success = vehicleDB.insertVehicle(vehicle);
        return success ? "Kendaraan berhasil didaftarkan!" : "ERROR: Gagal mendaftarkan kendaraan.";
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleDB.getAllVehicles();
    }

    public Vehicle findVehicle(String plate) {
        return vehicleDB.findByLicensePlate(plate);
    }

    public boolean updateVehicle(Vehicle vehicle) {
        return vehicleDB.updateVehicle(vehicle);
    }

    public boolean deleteVehicle(String vehicleId) {
        return vehicleDB.deleteVehicle(vehicleId);
    }

    // ===================== SERVICE RECORD METHODS =====================

    /**
     * Creates a new service record with validation.
     *
     * @param record ServiceRecord to add
     * @return Result message
     */
    public String addServiceRecord(ServiceRecord record) {
        if (record.getVehicleId() == null || record.getVehicleId().isBlank()) {
            return "ERROR: Kendaraan harus dipilih!";
        }
        if (record.getServiceType() == null || record.getServiceType().isBlank()) {
            return "ERROR: Jenis servis harus dipilih!";
        }
        if (record.getCost() < 0) {
            return "ERROR: Biaya tidak boleh negatif!";
        }

        int id = serviceRecordDB.insertServiceRecord(record);
        if (id > 0) {
            record.setServiceId(id);
            return "Record servis berhasil ditambahkan! (ID: " + id + ")";
        }
        return "ERROR: Gagal menambahkan record servis.";
    }

    public List<ServiceRecord> getAllRecords() {
        return serviceRecordDB.getAllRecords();
    }

    public List<ServiceRecord> getVehicleHistory(String vehicleId) {
        return serviceRecordDB.getRecordsByVehicle(vehicleId);
    }

    public boolean updateStatus(int serviceId, String status) {
        return serviceRecordDB.updateStatus(serviceId, status);
    }

    public boolean deleteRecord(int serviceId) {
        return serviceRecordDB.deleteRecord(serviceId);
    }

    /**
     * Calculates total service cost for a vehicle (uses loop).
     *
     * @param vehicleId Vehicle ID
     * @return Total cost
     */
    public double calculateTotalCost(String vehicleId) {
        List<ServiceRecord> records = serviceRecordDB.getRecordsByVehicle(vehicleId);
        double total = 0;

        // for loop iteration (requirement: pengulangan)
        for (ServiceRecord rec : records) {
            total += rec.getCost();
        }
        return total;
    }

    // ===================== PRINTABLE INTERFACE =====================

    /**
     * Generates formatted report content.
     *
     * @return Formatted string report
     */
    @Override
public String generatePrintContent() {
    StringBuilder sb = new StringBuilder();
    sb.append("============================================\n");
    sb.append("   LAPORAN SERVICE RECORD KENDARAAN\n");
    sb.append("============================================\n");
    sb.append("Tanggal Cetak: ").append(LocalDate.now()).append("\n");
    sb.append("--------------------------------------------\n\n");

    List<ServiceRecord> records = getAllRecords();

    if (!records.isEmpty()) {
        int i = 0;
        do {
            ServiceRecord r = records.get(i);

            // Ambil info kendaraan
            Vehicle v = vehicleDB.findByLicensePlate(r.getVehicleLicensePlate());
            String jenisKendaraan = "-";
            String merek          = "-";
            String model          = "-";

            if (v != null) {
                jenisKendaraan = v.getVehicleType();
                if (v instanceof Car c) {
                    merek = c.getBrand();
                    model = c.getModel();
                } else if (v instanceof Motorcycle m) {
                    merek = m.getBrand();
                    model = m.getModel();
                }
            }

            sb.append(String.format("Service ID    : %d\n",   r.getServiceId()));
            sb.append(String.format("Plat Nomor    : %s\n",   r.getVehicleLicensePlate()));
            sb.append(String.format("Jenis         : %s\n",   jenisKendaraan));
            sb.append(String.format("Merek         : %s\n",   merek));
            sb.append(String.format("Model         : %s\n",   model));
            sb.append(String.format("Jenis Servis  : %s\n",   r.getServiceType()));
            sb.append(String.format("Tanggal       : %s\n",   r.getServiceDate()));
            sb.append(String.format("Odometer      : %d km\n", r.getMileage()));
            sb.append(String.format("Biaya         : Rp%.0f\n", r.getCost()));
            sb.append(String.format("Teknisi       : %s\n",   r.getTechnicianName()));
            sb.append(String.format("Status        : %s\n",   r.getStatus()));
            if (r.getDescription() != null && !r.getDescription().isBlank()) {
                sb.append(String.format("Deskripsi     : %s\n", r.getDescription()));
            }
            sb.append("--------------------------------------------\n");

            i++;
        } while (i < records.size());

        // Total biaya semua record
        double total = 0;
        for (ServiceRecord r : records) total += r.getCost();
        sb.append(String.format("\nTOTAL BIAYA SEMUA SERVIS: Rp%.0f\n", total));

    } else {
        sb.append("Tidak ada data service record.\n");
    }

    sb.append("============================================\n");
    return sb.toString();
}

    /**
     * Exports service records to a plain text file.
     *
     * @param filePath Destination path
     * @return true if export succeeded
     */
    @Override
    public boolean exportToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(generatePrintContent());
            return true;
        } catch (IOException e) {
            System.err.println("Export error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reads data back from exported file.
     *
     * @param filePath File to read
     * @return File contents as string
     */
    public String readFromFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            return "ERROR membaca file: " + e.getMessage();
        }
        return sb.toString();
    }

    // ===================== OVERLOADED METHODS =====================

    /**
     * Generates a summary for all vehicles (overloading).
     *
     * @return Summary string
     */
    public String generateSummary() {
        return generateSummary(getAllVehicles().size(), getAllRecords().size());
    }

    /**
     * Generates a summary with custom counts (overloading).
     *
     * @param vehicleCount  Number of vehicles
     * @param recordCount   Number of records
     * @return Summary string
     */
    public String generateSummary(int vehicleCount, int recordCount) {
        return String.format(
            "Total Kendaraan: %d | Total Record Servis: %d",
            vehicleCount, recordCount);
    }
}