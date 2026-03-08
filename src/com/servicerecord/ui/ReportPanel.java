package com.servicerecord.ui;

import com.servicerecord.service.ServiceRecordManager;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * Panel for generating and exporting reports.
 *
 * @author Rangga
 * @version 1.0
 */
public class ReportPanel extends JPanel {

    private final ServiceRecordManager manager;
    private JTextArea taReport;

    public ReportPanel(ServiceRecordManager manager) {
        this.manager = manager;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));

        JButton btnGenerate = new JButton("Generate Laporan");
        JButton btnExport   = new JButton("Export ke File TXT");
        JButton btnImport   = new JButton("Baca dari File");

        btnGenerate.setBackground(new Color(0, 102, 204));
        btnGenerate.setForeground(Color.WHITE);
        btnExport  .setBackground(new Color(0, 153, 76));
        btnExport  .setForeground(Color.WHITE);

        btnGenerate.addActionListener(e -> generateReport());
        btnExport  .addActionListener(e -> exportReport());
        btnImport  .addActionListener(e -> importFromFile());

        btnPanel.add(btnGenerate);
        btnPanel.add(btnExport);
        btnPanel.add(btnImport);
        
        btnGenerate.setOpaque(true);
        btnGenerate.setBorderPainted(false);
        btnExport.setOpaque(true);
        btnExport.setBorderPainted(false);
        
        taReport = new JTextArea();
        taReport.setEditable(false);
        taReport.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(taReport);
        scroll.setBorder(BorderFactory.createTitledBorder("Hasil Laporan"));

        add(btnPanel, BorderLayout.NORTH);
        add(scroll,   BorderLayout.CENTER);
    }

    private void generateReport() {
        taReport.setText(manager.generatePrintContent());
    }

    private void exportReport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan");
        chooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".txt")) path += ".txt";
            boolean ok = manager.exportToFile(path);
            JOptionPane.showMessageDialog(this,
                ok ? "Laporan berhasil disimpan ke:\n" + path : "Gagal menyimpan!");
        }
    }

    private void importFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Baca File Laporan");
        chooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String content = manager.readFromFile(chooser.getSelectedFile().getAbsolutePath());
            taReport.setText(content);
        }
    }
}