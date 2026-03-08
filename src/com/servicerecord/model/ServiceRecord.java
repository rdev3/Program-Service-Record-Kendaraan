package com.servicerecord.model;

import java.time.LocalDate;

/**
 * Service record entity - stores one service/maintenance entry.
 *
 * @author Rangga
 * @version 1.0
 */
public class ServiceRecord {

    private int    serviceId;
    private String vehicleId;
    private String vehicleLicensePlate;
    private String serviceType;     // "Servis Rutin", "Ganti Oli", "Tune-Up", etc.
    private String description;
    private double cost;
    private int    mileage;         // odometer saat servis
    private String technicianName;
    private LocalDate serviceDate;
    private String status;          // "Selesai", "Dalam Proses", "Menunggu"

    /** Array of available service types */
    public static final String[] SERVICE_TYPES = {
        "Servis Rutin",
        "Ganti Oli",
        "Tune-Up",
        "Ganti Ban",
        "Servis Rem",
        "Overhaul Mesin",
        "Ganti Aki",
        "Cek Kelistrikan",
        "Spooring & Balancing",
        "Lainnya"
    };

    public static final String[] STATUS_OPTIONS = {
        "Menunggu",
        "Dalam Proses",
        "Selesai"
    };

    // Constructors
    public ServiceRecord() {
        this.serviceDate = LocalDate.now();
        this.status = "Menunggu";
    }

    public ServiceRecord(int serviceId, String vehicleId, String vehicleLicensePlate,
                         String serviceType, String description, double cost,
                         int mileage, String technicianName,
                         LocalDate serviceDate, String status) {
        this.serviceId          = serviceId;
        this.vehicleId          = vehicleId;
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.serviceType        = serviceType;
        this.description        = description;
        this.cost               = cost;
        this.mileage            = mileage;
        this.technicianName     = technicianName;
        this.serviceDate        = serviceDate;
        this.status             = status;
    }

    // ===================== GETTERS & SETTERS =====================
    public int    getServiceId()                    { return serviceId; }
    public void   setServiceId(int serviceId)       { this.serviceId = serviceId; }

    public String getVehicleId()                    { return vehicleId; }
    public void   setVehicleId(String vehicleId)    { this.vehicleId = vehicleId; }

    public String getVehicleLicensePlate()                              { return vehicleLicensePlate; }
    public void   setVehicleLicensePlate(String vehicleLicensePlate)   { this.vehicleLicensePlate = vehicleLicensePlate; }

    public String getServiceType()                      { return serviceType; }
    public void   setServiceType(String serviceType)    { this.serviceType = serviceType; }

    public String getDescription()                      { return description; }
    public void   setDescription(String description)    { this.description = description; }

    public double getCost()             { return cost; }
    public void   setCost(double cost)  { this.cost = cost; }

    public int  getMileage()                { return mileage; }
    public void setMileage(int mileage)    { this.mileage = mileage; }

    public String getTechnicianName()                       { return technicianName; }
    public void   setTechnicianName(String technicianName)  { this.technicianName = technicianName; }

    public LocalDate getServiceDate()                       { return serviceDate; }
    public void      setServiceDate(LocalDate serviceDate)  { this.serviceDate = serviceDate; }

    public String getStatus()               { return status; }
    public void   setStatus(String status)  { this.status = status; }

    @Override
    public String toString() {
        return String.format("Service #%d | %s | %s | %s | Rp%.0f | %s",
            serviceId, vehicleLicensePlate, serviceType,
            serviceDate, cost, status);
    }
}