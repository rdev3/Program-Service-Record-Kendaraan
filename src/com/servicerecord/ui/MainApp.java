package com.servicerecord.ui;

import com.servicerecord.util.DatabaseManager;
import javax.swing.*;

/**
 * Main application entry point.
 *
 * @author Rangga
 * @version 1.0
 */
public class MainApp {

    public static void main(String[] args) {
        // Inisialisasi database
        DatabaseManager.getInstance();

        // Buka LoginFrame dulu, bukan langsung MainFrame
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Could not set look and feel: " + e.getMessage());
            }

            LoginFrame login = new LoginFrame(); // ← ganti dari MainFrame
            login.setVisible(true);
        });
    }
}