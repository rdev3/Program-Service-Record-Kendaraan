package com.servicerecord.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Login form displayed before accessing the main application.
 * Provides basic authentication for system access.
 *
 * @author Rangga
 * @version 1.0
 */
public class LoginFrame extends JFrame {

    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    // Data akun (array - memenuhi syarat penggunaan array)
    private static final String[] VALID_USERS     = {"admin", "123", "secretadmin"};
    private static final String[] VALID_PASSWORDS = {"123",  "123", "666"};

    /**
     * Constructor - initializes login form
     */
    public LoginFrame() {
        initComponents();
    }

    /**
     * Initializes all UI components for the login form
     */
    private void initComponents() {
        setTitle("Login - Sistem Service Record Kendaraan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setPreferredSize(new Dimension(0, 70));

        JLabel titleLabel = new JLabel("Sistem Service Record Kendaraan", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel subLabel = new JLabel("Silakan login untuk melanjutkan", SwingConstants.CENTER);
        subLabel.setForeground(new Color(200, 220, 255));
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JPanel headerContent = new JPanel(new GridLayout(2, 1));
        headerContent.setOpaque(false);
        headerContent.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerContent.add(titleLabel);
        headerContent.add(subLabel);
        headerPanel.add(headerContent, BorderLayout.CENTER);

        // ===== FORM =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        tfUsername = new JTextField(18);
        tfUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(tfUsername, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        pfPassword = new JPasswordField(18);
        pfPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        formPanel.add(pfPassword, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        // Enter key di password field langsung login
        pfPassword.addActionListener(e -> doLogin());

        // ===== BUTTONS =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        btnLogin  = new JButton("Login");
        btnCancel = new JButton("Keluar");

        btnLogin.setPreferredSize(new Dimension(100, 32));
        btnCancel.setPreferredSize(new Dimension(100, 32));

        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));

        btnCancel.setBackground(new Color(180, 180, 180));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setOpaque(true);
        btnCancel.setBorderPainted(false);
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        btnLogin .addActionListener(e -> doLogin());
        btnCancel.addActionListener(e -> System.exit(0));

        btnPanel.add(btnLogin);
        btnPanel.add(btnCancel);

        // ===== INFO AKUN =====
        JLabel infoLabel = new JLabel(
            "<html><center><font color='gray' size='2'>" +
            "Created by: Made Ranggadeva Ratryananda Sandhi</font></center></html>",
            SwingConstants.CENTER);

        // ===== LAYOUT UTAMA =====
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel,   BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(btnPanel,   BorderLayout.CENTER);
        bottomPanel.add(infoLabel,  BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles login authentication logic.
     * Uses array iteration to validate credentials.
     * Locks after MAX_ATTEMPTS failed tries.
     */
    private void doLogin() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword());

        // Validasi input kosong
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username dan password tidak boleh kosong!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cek login dengan perulangan for
        boolean loginSuccess = false;
        for (int i = 0; i < VALID_USERS.length; i++) {
            if (VALID_USERS[i].equals(username) && VALID_PASSWORDS[i].equals(password)) {
                loginSuccess = true;
                break;
            }
        }

        // if-else validate login
        if (loginSuccess) {
            JOptionPane.showMessageDialog(this,
                "Selamat datang, " + username + "!",
                "Login Berhasil", JOptionPane.INFORMATION_MESSAGE);

            // Buka MainFrame dan tutup LoginFrame
            Mainframe mainframe = new Mainframe();
            mainframe.setVisible(true);
            this.dispose();

        } else {
            loginAttempts++;
            int sisaCoba = MAX_ATTEMPTS - loginAttempts;

            if (loginAttempts >= MAX_ATTEMPTS) {
                JOptionPane.showMessageDialog(this,
                    "Terlalu banyak percobaan login!\nProgram akan ditutup.",
                    "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Username atau password salah!\nSisa percobaan: " + sisaCoba,
                    "Login Gagal", JOptionPane.ERROR_MESSAGE);
                pfPassword.setText("");
                pfPassword.requestFocus();
            }
        }
    }
}