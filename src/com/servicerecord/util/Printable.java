package com.servicerecord.util;

/**
 * Interface for printable/exportable entities.
 * Demonstrates interface implementation requirement.
 *
 * @author Rangga
 * @version 1.0
 */
public interface Printable {

    /**
     * Generate a formatted string for printing/export
     *
     * @return Formatted print content
     */
    String generatePrintContent();

    /**
     * Export data to file
     *
     * @param filePath Destination file path
     * @return true if export successful
     */
    boolean exportToFile(String filePath);
}