package app.view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;


public class AdminDashboardGUI extends JFrame {

    // ── Tema warna ─────────────────────────────────────────────────────────
    private static final Color WARNA_BG       = new Color(245, 246, 250);
    private static final Color WARNA_HEADER   = new Color(44, 62, 80);
    private static final Color WARNA_PANEL    = Color.WHITE;

    // Warna tombol menu
    private static final Color WARNA_PRODUK     = new Color(41, 128, 185);
    private static final Color WARNA_KATEGORI   = new Color(142, 68, 173);
    private static final Color WARNA_VOUCHER    = new Color(39, 174, 96);
    private static final Color WARNA_PROMO      = new Color(243, 156, 18);
    private static final Color WARNA_METODE     = new Color(22, 160, 133);
    private static final Color WARNA_STOK       = new Color(52, 73, 94);
    private static final Color WARNA_LAP_KEUANGAN = new Color(192, 57, 43);
    private static final Color WARNA_LAP_STOK   = new Color(127, 140, 141);
    private static final Color WARNA_LOGOUT     = new Color(189, 195, 199);

    private String namaAdmin;

    public AdminDashboardGUI(String namaAdmin) {
        this.namaAdmin = namaAdmin;
        setTitle("JAWARA POS - Dashboard Admin");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initKomponen();
    }

    private void initKomponen() {
        JPanel panelUtama = new JPanel(new BorderLayout(0, 0));
        panelUtama.setBackground(WARNA_BG);

        panelUtama.add(buatHeader(), BorderLayout.NORTH);
        panelUtama.add(buatPanelMenu(), BorderLayout.CENTER);
        panelUtama.add(buatFooter(), BorderLayout.SOUTH);

        add(panelUtama);
    }

    // ── HEADER ──────────────────────────────────────────────────────────────
    private JPanel buatHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WARNA_HEADER);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel judul = new JLabel("🏠  Dashboard Admin");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        judul.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("👤 " + namaAdmin);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(new Color(189, 195, 199));

        panel.add(judul, BorderLayout.WEST);
        panel.add(lblUser, BorderLayout.EAST);
        return panel;
    }

    // ── PANEL MENU ──────────────────────────────────────────────────────────
    private JPanel buatPanelMenu() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(WARNA_BG);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        JLabel lblMenu = new JLabel("📋 Menu Utama");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMenu.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel grid = new JPanel(new GridLayout(3, 3, 15, 15));
        grid.setBackground(WARNA_BG);

        // Baris 1: Produk, Kategori Produk, Voucher
        grid.add(buatKartuMenu("📦", "Kelola Produk",       "CRUD data produk",       WARNA_PRODUK,     e -> bukaProdukGUI()));
        grid.add(buatKartuMenu("🏷", "Kategori Produk",     "CRUD kategori produk",   WARNA_KATEGORI,   e -> bukaCategoryProductGUI()));
        grid.add(buatKartuMenu("🎟", "Kelola Voucher",      "CRUD voucher diskon",    WARNA_VOUCHER,    e -> bukaVoucherGUI()));

        // Baris 2: Promo, Metode Pembayaran, Kelola Stok
        grid.add(buatKartuMenu("🎉", "Kelola Promo",        "CRUD promo diskon",      WARNA_PROMO,      e -> bukaPromoGUI()));
        grid.add(buatKartuMenu("💳", "Metode Pembayaran",   "CRUD metode bayar",      WARNA_METODE,     e -> bukaMetodePembayaranGUI()));
        grid.add(buatKartuMenu("📊", "Kelola Stok",         "Tambah/kurang stok",     WARNA_STOK,       e -> bukaStockProductGUI()));

        // Baris 3: Laporan Keuangan, Laporan Stok, Logout
        grid.add(buatKartuMenu("💰", "Laporan Keuangan",    "Pendapatan & transaksi", WARNA_LAP_KEUANGAN, e -> bukaLaporanKeuanganGUI()));
        grid.add(buatKartuMenu("📈", "Laporan Stok",        "Stok masuk & keluar",    WARNA_LAP_STOK,   e -> bukaLaporanStockGUI()));
        grid.add(buatKartuMenu("🚪", "Logout",              "Keluar dari sistem",     WARNA_LOGOUT,     e -> logout()));

        wrapper.add(lblMenu, BorderLayout.NORTH);
        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buatKartuMenu(String ikon, String judul, String deskripsi, Color warna, ActionListener aksi) {
        JPanel kartu = new JPanel(new BorderLayout(5, 5));
        kartu.setBackground(WARNA_PANEL);
        kartu.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        kartu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Strip warna di atas
        JPanel strip = new JPanel();
        strip.setBackground(warna);
        strip.setPreferredSize(new Dimension(0, 5));

        JLabel lblIkon = new JLabel(ikon, SwingConstants.CENTER);
        lblIkon.setFont(new Font("Segoe UI", Font.PLAIN, 32));

        JLabel lblJudul = new JLabel(judul, SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblDeskripsi = new JLabel(deskripsi, SwingConstants.CENTER);
        lblDeskripsi.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDeskripsi.setForeground(new Color(149, 165, 166));

        JPanel konten = new JPanel(new GridLayout(3, 1, 0, 2));
        konten.setBackground(WARNA_PANEL);
        konten.add(lblIkon);
        konten.add(lblJudul);
        konten.add(lblDeskripsi);

        kartu.add(strip, BorderLayout.NORTH);
        kartu.add(konten, BorderLayout.CENTER);

        // Hover effect
        kartu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                kartu.setBackground(new Color(235, 240, 250));
                konten.setBackground(new Color(235, 240, 250));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                kartu.setBackground(WARNA_PANEL);
                konten.setBackground(WARNA_PANEL);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                aksi.actionPerformed(new ActionEvent(kartu, ActionEvent.ACTION_PERFORMED, judul));
            }
        });

        return kartu;
    }

    // ── FOOTER ──────────────────────────────────────────────────────────────
    private JPanel buatFooter() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(WARNA_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        JLabel lbl = new JLabel("JAWARA POS System v1.0 © 2026");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(new Color(149, 165, 166));
        panel.add(lbl);
        return panel;
    }

    // ── NAVIGASI KE GUI ─────────────────────────────────────────────────────
    private void bukaProdukGUI() {
        SwingUtilities.invokeLater(() -> new ProdukGUI().setVisible(true));
    }

    private void bukaCategoryProductGUI() {
        SwingUtilities.invokeLater(() -> new CategoryProductGUI().setVisible(true));
    }

    private void bukaVoucherGUI() {
        SwingUtilities.invokeLater(() -> new VoucherGUI().setVisible(true));
    }

    private void bukaPromoGUI() {
        SwingUtilities.invokeLater(() -> new PromoGUI().setVisible(true));
    }

    private void bukaMetodePembayaranGUI() {
        SwingUtilities.invokeLater(() -> new MetodePembayaranGUI().setVisible(true));
    }

    private void bukaStockProductGUI() {
        SwingUtilities.invokeLater(() -> new StockProductGUI().setVisible(true));
    }

    private void bukaLaporanKeuanganGUI() {
        SwingUtilities.invokeLater(() -> new LaporanKeuanganGUI().setVisible(true));
    }

    private void bukaLaporanStockGUI() {
        SwingUtilities.invokeLater(() -> new LaporanStockGUI().setVisible(true));
    }

    private void logout() {
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin logout?", "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            dispose();
            // Kembali ke login
            app.service.AuthService authService = new app.service.AuthService();
            app.view.auth.LoginView loginView = new app.view.auth.LoginView();
            app.controller.LoginController loginController = new app.controller.LoginController(loginView, authService);
            loginController.start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboardGUI("Admin Utama").setVisible(true));
    }
}
