package com.servicerecord.ui;

import com.servicerecord.model.*;
import com.servicerecord.service.ServiceRecordManager;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel for entering new service records.
 *
 * @author Rangga
 * @version 1.0
 */
public class ServiceInputPanel extends JPanel {

    private final ServiceRecordManager manager;
    private final JLabel statusLabel;

    private JComboBox<String> cbVehicle, cbServiceType, cbStatus;
    private JTextField tfMileage, tfCost, tfTechnician, tfDate;
    private JTextArea  taDescription;

    public ServiceInputPanel(ServiceRecordManager manager, JLabel statusLabel) {
        this.manager     = manager;
        this.statusLabel = statusLabel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Input Data Servis Kendaraan"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(5, 8, 5, 8);
        gbc.anchor  = GridBagConstraints.WEST;

        // Vehicle selector
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Kendaraan:"), gbc);
        cbVehicle = new JComboBox<>();
        loadVehicles();
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        gbc.gridwidth = 3;
        formPanel.add(cbVehicle, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        // Jenis Servis
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Jenis Servis:"), gbc);
        cbServiceType = new JComboBox<>(ServiceRecord.SERVICE_TYPES);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(cbServiceType, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Tanggal
        gbc.gridx = 2; gbc.gridy = 1;
        formPanel.add(new JLabel("Tanggal:"), gbc);
        tfDate = new JTextField(LocalDate.now().toString(), 12);
        gbc.gridx = 3;
        formPanel.add(tfDate, gbc);

        // Km/Odometer
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Odometer (km):"), gbc);
        tfMileage = new JTextField(10);
        gbc.gridx = 1;
        formPanel.add(tfMileage, gbc);

        // Biaya
        gbc.gridx = 2; gbc.gridy = 2;
        formPanel.add(new JLabel("Biaya (Rp):"), gbc);
        tfCost = new JTextField(12);
        gbc.gridx = 3;
        formPanel.add(tfCost, gbc);

        // Teknisi
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Nama Teknisi:"), gbc);
        tfTechnician = new JTextField(20);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(tfTechnician, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Status
        gbc.gridx = 2; gbc.gridy = 3;
        formPanel.add(new JLabel("Status:"), gbc);
        cbStatus = new JComboBox<>(ServiceRecord.STATUS_OPTIONS);
        gbc.gridx = 3;
        formPanel.add(cbStatus, gbc);

        // Deskripsi
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Deskripsi:"), gbc);
        taDescription = new JTextArea(4, 30);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(taDescription);
        gbc.gridx = 1; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1; gbc.weighty = 1;
        formPanel.add(descScroll, gbc);
        gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0; gbc.weighty = 0;

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        JButton btnSave    = new JButton("Simpan Servis");
        JButton btnClear   = new JButton("Bersihkan");
        JButton btnRefresh = new JButton("Refresh Kendaraan");

        btnSave.setBackground(new Color(0, 120, 215));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(e -> saveRecord());

        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> loadVehicles());

        btnPanel.add(btnSave);
        btnPanel.add(btnClear);
        btnPanel.add(btnRefresh);

        add(formPanel, BorderLayout.CENTER);
        add(btnPanel,  BorderLayout.SOUTH);
    }

    /** Loads vehicles into combo box */
    private void loadVehicles() {
        cbVehicle.removeAllItems();
        List<Vehicle> vehicles = manager.getAllVehicles();
        if (vehicles.isEmpty()) {
            cbVehicle.addItem("-- Belum ada kendaraan terdaftar --");
        } else {
            for (Vehicle v : vehicles) {
                cbVehicle.addItem(v.getVehicleId() + " | " + v.getLicensePlate() + " - " + v.getOwnerName());
            }
        }
    }

    /** Saves the service record */
    private void saveRecord() {
        try {
            if (cbVehicle.getItemCount() == 0 ||
                cbVehicle.getSelectedItem().toString().startsWith("--")) {
                JOptionPane.showMessageDialog(this, "Daftarkan kendaraan terlebih dahulu!");
                return;
            }

            // Parse vehicle info from combo
            String selected = (String) cbVehicle.getSelectedItem();
            String vehicleId    = selected.split("\\|")[0].trim();
            String licensePlate = selected.split("\\|")[1].split("-")[0].trim();

            String dateStr = tfDate.getText().trim();
            LocalDate date;
            try {
                date = LocalDate.parse(dateStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Format tanggal harus YYYY-MM-DD!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double cost = 0;
            if (!tfCost.getText().isBlank()) cost = Double.parseDouble(tfCost.getText().trim());

            int mileage = 0;
            if (!tfMileage.getText().isBlank()) mileage = Integer.parseInt(tfMileage.getText().trim());

            ServiceRecord record = new ServiceRecord(
                0, vehicleId, licensePlate,
                (String) cbServiceType.getSelectedItem(),
                taDescription.getText().trim(),
                cost, mileage,
                tfTechnician.getText().trim(),
                date,
                (String) cbStatus.getSelectedItem()
            );

            String result = manager.addServiceRecord(record);
            JOptionPane.showMessageDialog(this, result,
                result.startsWith("ERROR") ? "Gagal" : "Berhasil",
                result.startsWith("ERROR") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);

            if (!result.startsWith("ERROR")) {
                clearForm();
                statusLabel.setText(" " + manager.generateSummary());
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format angka tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        tfDate       .setText(LocalDate.now().toString());
        tfMileage    .setText("");
        tfCost       .setText("");
        tfTechnician .setText("");
        taDescription.setText("");
        cbServiceType.setSelectedIndex(0);
        cbStatus     .setSelectedIndex(0);
    }
}