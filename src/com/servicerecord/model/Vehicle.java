package com.servicerecord.model;

/**
 * Abstract base class representing a vehicle.
 * Demonstrates inheritance, abstraction, and encapsulation.
 *
 * @author Rangga
 * @version 1.0
 * @since 2026
 */
public abstract class Vehicle {

    // Private fields - encapsulation
    private String vehicleId;
    private String licensePlate;
    private String ownerName;
    private String ownerPhone;
    private int year;

    /**
     * Default constructor
     */
    public Vehicle() {}

    /**
     * Parameterized constructor
     *
     * @param vehicleId    Unique vehicle ID
     * @param licensePlate License plate number
     * @param ownerName    Owner's full name
     * @param ownerPhone   Owner's phone number
     * @param year         Manufacturing year
     */
    public Vehicle(String vehicleId, String licensePlate,
                   String ownerName, String ownerPhone, int year) {
        this.vehicleId    = vehicleId;
        this.licensePlate = licensePlate;
        this.ownerName    = ownerName;
        this.ownerPhone   = ownerPhone;
        this.year         = year;
    }

    // ===================== GETTERS & SETTERS =====================

    public String getVehicleId()              { return vehicleId; }
    public void   setVehicleId(String id)     { this.vehicleId = id; }

    public String getLicensePlate()                   { return licensePlate; }
    public void   setLicensePlate(String plate)       { this.licensePlate = plate; }

    public String getOwnerName()                  { return ownerName; }
    public void   setOwnerName(String name)       { this.ownerName = name; }

    public String getOwnerPhone()                   { return ownerPhone; }
    public void   setOwnerPhone(String phone)       { this.ownerPhone = phone; }

    public int  getYear()           { return year; }
    public void setYear(int year)   { this.year = year; }

    /**
     * Abstract method - must be implemented by subclasses (polymorphism)
     *
     * @return Vehicle type description
     */
    public abstract String getVehicleType();

    /**
     * Returns string representation of vehicle
     *
     * @return Formatted vehicle info string
     */
    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%d)",
            getVehicleType(), licensePlate, ownerName, year);
    }
}