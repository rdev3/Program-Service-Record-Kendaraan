package com.servicerecord.ui;

import com.servicerecord.model.*;
import com.servicerecord.service.ServiceRecordManager;
import com.servicerecord.util.IDGenerator;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing vehicle registration (CRUD).
 *
 * @author Rangga
 * @version 1.0
 */
public class VehiclePanel extends JPanel {

    private final ServiceRecordManager manager;
    private final JLabel statusLabel;

    // Form fields
    private JTextField tfLicensePlate, tfOwnerName, tfOwnerPhone,
                   tfYear, tfBrand, tfModel, tfTransmission, tfEngineCC;
    private JComboBox<String> cbVehicleType;
    private JTable     vehicleTable;
    private DefaultTableModel tableModel;

    public VehiclePanel(ServiceRecordManager manager, JLabel statusLabel) {
        this.manager     = manager;
        this.statusLabel = statusLabel;
        initComponents();
        loadTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Pendaftaran Kendaraan"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Jenis Kendaraan
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Jenis Kendaraan:"), gbc);
        cbVehicleType = new JComboBox<>(new String[]{"Mobil", "Motor"});
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(cbVehicleType, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        // Row 1: Plat Nomor
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Plat Nomor:"), gbc);
        tfLicensePlate = new JTextField(15);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(tfLicensePlate, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Row 2: Nama Pemilik
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Nama Pemilik:"), gbc);
        tfOwnerName = new JTextField(20);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(tfOwnerName, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Row 3: No. HP
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("No. Telepon:"), gbc);
        tfOwnerPhone = new JTextField(15);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(tfOwnerPhone, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Row 4: Tahun
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Tahun:"), gbc);
        tfYear = new JTextField(6);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(tfYear, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Row 5: Merek
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Merek:"), gbc);
        tfBrand = new JTextField(15);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(tfBrand, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Row 6: Model
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Model/Tipe:"), gbc);
        tfModel = new JTextField(15);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(tfModel, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Row 7: Extra Info (dynamic label)
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("Transmisi:"), gbc);
        tfTransmission = new JTextField(15);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(tfTransmission, gbc);
        gbc.fill = GridBagConstraints.NONE;
        
        // Row 8: Kapasitas Mesin (hanya untuk Mobil)
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("CC Mesin:"), gbc);
        tfEngineCC = new JTextField(10);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(tfEngineCC, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton btnSave   = new JButton("Simpan");
        JButton btnEdit   = new JButton("Edit");
        JButton btnClear  = new JButton("Hapus Isian");
        JButton btnDelete = new JButton("Hapus Data");

        btnSave.setBackground(new Color(0, 153, 76));
        btnSave.setForeground(Color.WHITE);
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(e -> saveVehicle());

        btnClear.addActionListener(e -> clearForm());

        btnDelete.setBackground(new Color(204, 0, 0));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setOpaque(true);
        btnDelete.setBorderPainted(false);
        btnDelete.addActionListener(e -> deleteSelected());
        
        btnEdit.setBackground(new Color(255, 153, 0));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setOpaque(true);
        btnEdit.setBorderPainted(false);
        btnEdit.addActionListener(e -> editVehicle());

        btnPanel.add(btnSave);
        btnPanel.add(btnEdit);
        btnPanel.add(btnClear);
        btnPanel.add(btnDelete);

        JPanel leftPanel = new JPanel(new BorderLayout(0, 8));
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(btnPanel, BorderLayout.SOUTH);
        leftPanel.setPreferredSize(new Dimension(340, 0));

        // ===== TABLE PANEL =====
        String[] columns = {"ID", "Plat", "Pemilik", "Telepon", "Tahun", "Tipe", "Merek", "Model", "Transmisi", "Kapasitas Mesin"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        vehicleTable = new JTable(tableModel);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleTable.getColumnModel().getColumn(0).setPreferredWidth(160);

        // Fill form when row selected
        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelection();
        });

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Kendaraan Terdaftar"));

        add(leftPanel,  BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
    }

    /** Saves a new vehicle from form inputs */
    private void saveVehicle() {
        try {
            String type  = (String) cbVehicleType.getSelectedItem();
            String plate = tfLicensePlate.getText().trim().toUpperCase();
            String name  = tfOwnerName.getText().trim();
            String phone = tfOwnerPhone.getText().trim();
            String yearStr = tfYear.getText().trim();
            String brand = tfBrand.getText().trim();
            String model = tfModel.getText().trim();
            String transmission = tfTransmission.getText().trim();
            String engineCC     = tfEngineCC.getText().trim();


            if (yearStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Tahun harus diisi!"); return; }
            int year = Integer.parseInt(yearStr);

            Vehicle vehicle;
            if ("Motor".equals(type)) {
                String id = IDGenerator.generateVehicleId("MTR");
                vehicle = new Motorcycle(id, plate, name, phone, year,
                             brand, model, transmission, engineCC);
            } else {
                String id = IDGenerator.generateVehicleId("MOB");
                vehicle = new Car(id, plate, name, phone, year,
                            brand, model, transmission, engineCC);
            }

            String result = manager.registerVehicle(vehicle);
            JOptionPane.showMessageDialog(this, result,
                result.startsWith("ERROR") ? "Gagal" : "Berhasil",
                result.startsWith("ERROR") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);

            if (!result.startsWith("ERROR")) {
                clearForm();
                loadTable();
                statusLabel.setText(" " + manager.generateSummary());
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Format tahun tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

 /**
 * Updates existing vehicle data from form inputs. Update data kendaraan
 * Validates that a row is selected before updating.
 */
private void editVehicle() {
    // Cek apakah ada baris yang dipilih di tabel
    int row = vehicleTable.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(this,
            "Pilih kendaraan yang ingin diedit terlebih dahulu!",
            "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        String vehicleId = (String) tableModel.getValueAt(row, 0);
        String type      = (String) cbVehicleType.getSelectedItem();
        String plate     = tfLicensePlate.getText().trim().toUpperCase();
        String name      = tfOwnerName.getText().trim();
        String phone     = tfOwnerPhone.getText().trim();
        String yearStr   = tfYear.getText().trim();
        String brand     = tfBrand.getText().trim();
        String model     = tfModel.getText().trim();
        String transmission = tfTransmission.getText().trim();
        String engineCC     = tfEngineCC.getText().trim();

        // Validasi input tidak kosong (if-else)
        if (plate.isEmpty() || name.isEmpty() || yearStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Plat nomor, nama pemilik, dan tahun harus diisi!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int year = Integer.parseInt(yearStr);

        // Buat objek vehicle sesuai tipe (polymorphism)
        Vehicle vehicle;
        if ("Motor".equals(type)) {
            vehicle = new Motorcycle(vehicleId, plate, name, phone, year,
                             brand, model, transmission, engineCC);
      } else {
            vehicle = new Car(vehicleId, plate, name, phone, year,
                          brand, model, transmission, engineCC);
      }

        // Konfirmasi sebelum update
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin mengubah data kendaraan ini?",
            "Konfirmasi Edit", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = manager.updateVehicle(vehicle);
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Data kendaraan berhasil diubah!",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadTable();
                statusLabel.setText(" " + manager.generateSummary());
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal mengubah data kendaraan!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this,
            "Format tahun tidak valid!",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    /** Loads all vehicles into table */
private void loadTable() {
    tableModel.setRowCount(0);
    List<Vehicle> vehicles = manager.getAllVehicles();
    for (Vehicle v : vehicles) {
        String brand = "", model = "", transmission = "", engineCC = "";

        if (v instanceof Car c) {
            brand        = c.getBrand();
            model        = c.getModel();
            transmission = c.getTransmission();
            engineCC     = c.getEngineCC();
        } else if (v instanceof Motorcycle m) {
            brand        = m.getBrand();
            model        = m.getModel();
            transmission = m.getTransmission();
            engineCC     = m.getEngineCC();
        }

        tableModel.addRow(new Object[]{
            v.getVehicleId(), v.getLicensePlate(), v.getOwnerName(),
            v.getOwnerPhone(), v.getYear(), v.getVehicleType(),
            brand, model, transmission, engineCC
        });
    }
}

    /** Fills form from selected table row */
    private void fillFormFromSelection() {
        int row = vehicleTable.getSelectedRow();
        if (row < 0) return;
        String type = (String) tableModel.getValueAt(row, 5);
        cbVehicleType.setSelectedItem(type);
        tfLicensePlate.setText((String) tableModel.getValueAt(row, 1));
        tfOwnerName   .setText((String) tableModel.getValueAt(row, 2));
        tfOwnerPhone  .setText((String) tableModel.getValueAt(row, 3));
        tfYear        .setText(String.valueOf(tableModel.getValueAt(row, 4)));
        tfBrand       .setText((String) tableModel.getValueAt(row, 6));
        tfModel       .setText((String) tableModel.getValueAt(row, 7));
        tfTransmission.setText((String) tableModel.getValueAt(row, 8));
        tfEngineCC    .setText((String) tableModel.getValueAt(row, 9));
    }

    /** Deletes selected vehicle */
    private void deleteSelected() {
        int row = vehicleTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih kendaraan terlebih dahulu!"); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin hapus kendaraan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (manager.deleteVehicle(id)) { loadTable(); clearForm(); }
            else JOptionPane.showMessageDialog(this, "Gagal menghapus!");
        }
    }

    private void clearForm() {
        tfLicensePlate.setText("");
        tfOwnerName   .setText("");
        tfOwnerPhone  .setText("");
        tfYear        .setText("");
        tfBrand       .setText("");
        tfModel       .setText("");
        tfTransmission.setText("");
        tfEngineCC    .setText("");
        vehicleTable.clearSelection();
    }
}