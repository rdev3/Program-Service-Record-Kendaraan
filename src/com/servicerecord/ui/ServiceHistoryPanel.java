package com.servicerecord.ui;

import com.servicerecord.model.Vehicle;
import com.servicerecord.model.Car;
import com.servicerecord.model.Motorcycle;
import com.servicerecord.model.ServiceRecord;
import com.servicerecord.service.ServiceRecordManager;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Panel for viewing service record history with filtering.
 *
 * @author Rangga
 * @version 1.0
 */
public class ServiceHistoryPanel extends JPanel {

    private final ServiceRecordManager manager;
    private final JLabel statusLabel;

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField tfSearch;

    public ServiceHistoryPanel(ServiceRecordManager manager, JLabel statusLabel) {
        this.manager     = manager;
        this.statusLabel = statusLabel;
        initComponents();
        loadTable(manager.getAllRecords());
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari (Plat/Jenis/Status):"));
        tfSearch = new JTextField(20);
        JButton btnSearch  = new JButton("Cari");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnDelete  = new JButton("Hapus");
        JButton btnUpdateStatus = new JButton("Update Status");

        btnSearch.addActionListener(e -> searchRecords());
        btnRefresh.addActionListener(e -> loadTable(manager.getAllRecords()));
        btnDelete.addActionListener(e -> deleteSelected());
        btnUpdateStatus.addActionListener(e -> updateStatus());

        searchPanel.add(tfSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        searchPanel.add(btnDelete);
        searchPanel.add(btnUpdateStatus);

        // Table
        String[] cols = {"ID", "Plat Nomor", "Jenis Kendaraan" ,"Jenis Servis", "Teknisi",
                         "Tanggal", "Odometer", "Biaya (Rp)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setRowHeight(22);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Color rows by status
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                if (!sel) {
                    String status = (String) tableModel.getValueAt(row, 8);
                    if ("Selesai".equals(status))        setBackground(new Color(200, 255, 200));
                    else if ("Dalam Proses".equals(status)) setBackground(new Color(255, 255, 180));
                    else                                 setBackground(new Color(255, 220, 200));
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);

        add(searchPanel, BorderLayout.NORTH);
        add(scroll,      BorderLayout.CENTER);
    }

    /** Loads records into table */
private void loadTable(List<ServiceRecord> records) {
    tableModel.setRowCount(0);
    for (ServiceRecord r : records) {

        // Ambil info kendaraan dari database
        Vehicle v = manager.findVehicle(r.getVehicleLicensePlate());
        String jenisKendaraan = "";
        if (v != null) {
            jenisKendaraan = v.getVehicleType() + " - "
                + (v instanceof Car c ? c.getBrand() + " " + c.getModel()
                 : v instanceof Motorcycle m ? m.getBrand() + " " + m.getModel()
                 : "");
        }

        tableModel.addRow(new Object[]{
            r.getServiceId(),
            r.getVehicleLicensePlate(),
            jenisKendaraan,
            r.getServiceType(),
            r.getTechnicianName(),
            r.getServiceDate(),
            r.getMileage(),
            String.format("%.0f", r.getCost()),
            r.getStatus()
        });
    }
}

    /** Searches records by keyword */
    private void searchRecords() {
        String keyword = tfSearch.getText().trim().toLowerCase();
        List<ServiceRecord> all = manager.getAllRecords();

        // Filter using for-each loop (requirement: pengulangan)
        java.util.List<ServiceRecord> filtered = new java.util.ArrayList<>();
        for (ServiceRecord r : all) {
            if (r.getVehicleLicensePlate().toLowerCase().contains(keyword)
             || r.getServiceType().toLowerCase().contains(keyword)
             || r.getStatus().toLowerCase().contains(keyword)) {
                filtered.add(r);
            }
        }
        loadTable(filtered);
    }

    /** Updates status of selected record */
    private void updateStatus() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih record terlebih dahulu!"); return; }

        int id = (int) tableModel.getValueAt(row, 0);
        String[] options = ServiceRecord.STATUS_OPTIONS;
        String chosen = (String) JOptionPane.showInputDialog(
            this, "Pilih status baru:", "Update Status",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (chosen != null) {
            if (manager.updateStatus(id, chosen)) {
                loadTable(manager.getAllRecords());
                statusLabel.setText(" " + manager.generateSummary());
            } else {
                JOptionPane.showMessageDialog(this, "Gagal update status!");
            }
        }
    }

    /** Deletes selected record */
    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih record terlebih dahulu!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin hapus record #" + id + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (manager.deleteRecord(id)) loadTable(manager.getAllRecords());
            else JOptionPane.showMessageDialog(this, "Gagal menghapus!");
        }
    }
}