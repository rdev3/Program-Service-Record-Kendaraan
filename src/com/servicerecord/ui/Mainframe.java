package com.servicerecord.ui;

import com.servicerecord.service.ServiceRecordManager;
import javax.swing.*;
import java.awt.*;

/**
 * Main application window with tabbed interface.
 *
 * @author Rangga
 * @version 1.0
 */
public class Mainframe extends JFrame {

    private final ServiceRecordManager manager;
    private JTabbedPane tabbedPane;

    public Mainframe() {
        this.manager = new ServiceRecordManager();
        initComponents();
    }

    /**
     * Initializes all UI components.
     */
    private void initComponents() {
        setTitle("Sistem Manajemen Service Record Kendaraan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        // Add window close listener to close DB connection
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                com.servicerecord.util.DatabaseManager.getInstance().closeConnection();
            }
        });

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel(" Sistem Service Record Kendaraan", SwingConstants.LEFT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Status bar
        JLabel statusLabel = new JLabel(" " + manager.generateSummary());
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setBorder(BorderFactory.createEtchedBorder());

        // Tabbed pane
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tabbedPane.addTab("Data Kendaraan",  new VehiclePanel(manager, statusLabel));
        tabbedPane.addTab("Input Servis",    new ServiceInputPanel(manager, statusLabel));
        tabbedPane.addTab("Riwayat Servis",  new ServiceHistoryPanel(manager, statusLabel));
        tabbedPane.addTab("Laporan",         new ReportPanel(manager));

        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane,  BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }
}