package com.servicerecord.model;

/**
 * Motorcycle entity - extends Vehicle (inheritance).
 *
 * @author Rangga
 * @version 1.0
 */
public class Motorcycle extends Vehicle {

    private String brand;
    private String model;
    private String transmission; // Manual / Kopling / Matic
    private String engineCC;     // CC Mesin: 150cc, 250cc, dll

    public Motorcycle() { super(); }

    public Motorcycle(String vehicleId, String licensePlate,
                      String ownerName, String ownerPhone,
                      int year, String brand, String model,
                      String transmission, String engineCC) {
        super(vehicleId, licensePlate, ownerName, ownerPhone, year);
        this.brand        = brand;
        this.model        = model;
        this.transmission = transmission;
        this.engineCC     = engineCC;
    }

    public String getBrand()                      { return brand; }
    public void   setBrand(String brand)          { this.brand = brand; }

    public String getModel()                      { return model; }
    public void   setModel(String model)          { this.model = model; }

    public String getTransmission()                         { return transmission; }
    public void   setTransmission(String transmission)      { this.transmission = transmission; }

    public String getEngineCC()                   { return engineCC; }
    public void   setEngineCC(String engineCC)    { this.engineCC = engineCC; }

    
      /**
     * Polymorphic implementation of getVehicleType()
     */
    @Override
    public String getVehicleType() { return "Motor"; }

    @Override
    public String toString() {
        return super.toString() + " | " + brand + " " + model
            + " " + transmission + " " + engineCC;
    }
}

 