package com.servicerecord.util;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Utility class for generating unique IDs.
 *
 * @author Rangga
 * @version 1.0
 */
public class IDGenerator {

    /**
     * Generates a unique vehicle ID based on type prefix + date + random.
     *
     * @param prefix Vehicle type prefix (e.g., "MOB", "MTR")
     * @return Generated ID string
     */
    public static String generateVehicleId(String prefix) {
        String datePart = LocalDate.now().toString().replace("-", "");
        String randPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return prefix + "-" + datePart + "-" + randPart;
    }

    /**
     * Validates a license plate format (Indonesian format).
     *
     * @param plate License plate string
     * @return true if valid format
     */
    public static boolean isValidLicensePlate(String plate) {
        if (plate == null || plate.isBlank()) return false;
        // Basic check: not empty, at least 4 chars, no special chars besides space
        return plate.matches("[A-Z]{1,2}\\s?\\d{1,4}\\s?[A-Z]{1,3}");
    }
}