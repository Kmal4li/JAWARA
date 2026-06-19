package app.view.admin;

import app.model.LaporanKeuangan;
import app.model.Transaksi;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * GUI untuk Laporan Keuangan sesuai class diagram.
 *
 * CARA PENGGUNAAN:
 * ─────────────────────────────────────────────
 * 1. Klik "Generate Laporan" untuk menghitung total pendapatan dan total transaksi
 * 2. Lihat ringkasan di panel atas
 * 3. Lihat detail transaksi di tabel
 * ─────────────────────────────────────────────
 *
 * Atribut class diagram: totalPendapatan, totalTransaksi, tanggalCetak
 * Method: hitungTotalPendapatan(List<Transaksi>), hitungTotalTransaksi(List<Transaksi>),
 *         generateLaporan()
 */
public class LaporanKeuanganGUI extends JFrame {

    // ── Display fields ────────────────────────────────────────────────────────
    private JLabel lblTotalPendapatan, lblTotalTransaksi, lblTanggalCetak;

    // ── Tabel ─────────────────────────────────────────────────────────────────
    private JTable tabel;
    private DefaultTableModel modelTabel;

    // ── Data ──────────────────────────────────────────────────────────────────
    private List<Transaksi> daftarTransaksi = new ArrayList<>();
    private LaporanKeuangan laporan = new LaporanKeuangan();

    // ── Warna tema ────────────────────────────────────────────────────────────
    private static final Color WARNA_UTAMA  = new Color(192, 57, 43);
    private static final Color WARNA_SUKSES = new Color(39, 174, 96);
    private static final Color WARNA_BG     = new Color(245, 246, 250);
    private static final Color WARNA_PANEL  = Color.WHITE;

    public LaporanKeuanganGUI() {
        setTitle("Laporan Keuangan");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initKomponen();
        muatDataContoh();
        generateLaporan();
    }

    private void initKomponen() {
        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        panelUtama.setBackground(WARNA_BG);
        panelUtama.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelUtama.add(buatHeader(), BorderLayout.NORTH);
        panelUtama.add(buatPanelKonten(), BorderLayout.CENTER);
        panelUtama.add(buatPanelTombol(), BorderLayout.SOUTH);

        add(panelUtama);
    }

    private JPanel buatHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WARNA_UTAMA);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel judul = new JLabel("💰  Laporan Keuangan");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);

        JLabel petunjuk = new JLabel("Klik 'Generate Laporan' untuk memperbarui data");
        petunjuk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        petunjuk.setForeground(new Color(255, 200, 200));

        panel.add(judul, BorderLayout.WEST);
        panel.add(petunjuk, BorderLayout.EAST);
        return panel;
    }

    private JPanel buatPanelKonten() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(WARNA_BG);

        panel.add(buatPanelRingkasan(), BorderLayout.NORTH);
        panel.add(buatPanelTabel(), BorderLayout.CENTER);

        return panel;
    }

    // ── PANEL RINGKASAN ───────────────────────────────────────────────────────
    private JPanel buatPanelRingkasan() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 12, 0));
        panel.setBackground(WARNA_BG);

        // Kartu: Total Pendapatan
        lblTotalPendapatan = new JLabel("Rp 0", SwingConstants.CENTER);
        lblTotalPendapatan.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotalPendapatan.setForeground(WARNA_SUKSES);
        panel.add(buatKartuRingkasan("💵 Total Pendapatan", lblTotalPendapatan));

        // Kartu: Total Transaksi
        lblTotalTransaksi = new JLabel("0", SwingConstants.CENTER);
        lblTotalTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotalTransaksi.setForeground(new Color(41, 128, 185));
        panel.add(buatKartuRingkasan("📊 Total Transaksi", lblTotalTransaksi));

        // Kartu: Tanggal Cetak
        lblTanggalCetak = new JLabel("-", SwingConstants.CENTER);
        lblTanggalCetak.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTanggalCetak.setForeground(new Color(142, 68, 173));
        panel.add(buatKartuRingkasan("📅 Tanggal Cetak", lblTanggalCetak));

        return panel;
    }

    private JPanel buatKartuRingkasan(String judul, JLabel lblNilai) {
        JPanel kartu = new JPanel(new BorderLayout(0, 5));
        kartu.setBackground(WARNA_PANEL);
        kartu.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel lblJudul = new JLabel(judul, SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblJudul.setForeground(new Color(100, 100, 100));

        kartu.add(lblJudul, BorderLayout.NORTH);
        kartu.add(lblNilai, BorderLayout.CENTER);
        return kartu;
    }

    // ── PANEL TABEL ───────────────────────────────────────────────────────────
    private JPanel buatPanelTabel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(WARNA_BG);

        String[] kolom = {"ID", "Tanggal", "Total Harga", "Total Bayar", "Kembalian", "Metode"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabel.setRowHeight(26);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(WARNA_UTAMA);
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.setSelectionBackground(new Color(255, 210, 210));
        tabel.setGridColor(new Color(230, 230, 230));

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));

        panel.add(buatJudul("📋 Detail Transaksi"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── PANEL TOMBOL ──────────────────────────────────────────────────────────
    private JPanel buatPanelTombol() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        panel.setBackground(WARNA_BG);

        JButton btnGenerate = buatTombol("📊 Generate Laporan", WARNA_UTAMA);
        btnGenerate.setPreferredSize(new Dimension(200, 35));
        btnGenerate.addActionListener(e -> generateLaporan());
        btnGenerate.setToolTipText("Klik untuk menghitung ulang laporan keuangan");

        JButton btnCetak = buatTombol("🖨 Cetak Laporan", new Color(41, 128, 185));
        btnCetak.setPreferredSize(new Dimension(200, 35));
        btnCetak.addActionListener(e -> cetakLaporan());
        btnCetak.setToolTipText("Cetak laporan ke konsol");

        panel.add(btnGenerate);
        panel.add(btnCetak);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIKA (sesuai method class diagram)
    // ─────────────────────────────────────────────────────────────────────────
    private void generateLaporan() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tanggal = sdf.format(new Date());

        int totalPendapatan = laporan.hitungTotalPendapatan(daftarTransaksi);
        int totalTransaksi  = laporan.hitungTotalTransaksi(daftarTransaksi);
        laporan.setTanggalCetak(tanggal);

        lblTotalPendapatan.setText("Rp " + String.format("%,d", totalPendapatan));
        lblTotalTransaksi.setText(String.valueOf(totalTransaksi));
        lblTanggalCetak.setText(tanggal);

        refreshTabel();
    }

    private void cetakLaporan() {
        laporan.generateLaporan();
        JOptionPane.showMessageDialog(this,
            "Laporan berhasil dicetak ke konsol!\n\n" +
            "Total Pendapatan: Rp " + String.format("%,d", laporan.getTotalPendapatan()) + "\n" +
            "Total Transaksi: " + laporan.getTotalTransaksi() + "\n" +
            "Tanggal Cetak: " + laporan.getTanggalCetak(),
            "Cetak Laporan", JOptionPane.INFORMATION_MESSAGE);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshTabel() {
        modelTabel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Transaksi t : daftarTransaksi) {
            modelTabel.addRow(new Object[]{
                t.getId(),
                t.getTanggal() != null ? sdf.format(t.getTanggal()) : "-",
                "Rp " + (int) t.getTotalHarga(),
                "Rp " + (int) t.getTotalBayar(),
                "Rp " + (int) t.getKembalian(),
                t.getPaymentMethod()
            });
        }
    }

    private void muatDataContoh() {
        // Data contoh transaksi
        Transaksi t1 = new Transaksi();
        t1.setId(1); t1.setTanggal(new Date()); t1.setTotalHarga(50000);
        t1.setTotalBayar(50000); t1.setKembalian(0); t1.setPaymentMethod("Tunai");

        Transaksi t2 = new Transaksi();
        t2.setId(2); t2.setTanggal(new Date()); t2.setTotalHarga(125000);
        t2.setTotalBayar(125000); t2.setKembalian(5000); t2.setPaymentMethod("Transfer Bank");

        Transaksi t3 = new Transaksi();
        t3.setId(3); t3.setTanggal(new Date()); t3.setTotalHarga(75000);
        t3.setTotalBayar(75000); t3.setKembalian(0); t3.setPaymentMethod("QRIS");

        daftarTransaksi.add(t1);
        daftarTransaksi.add(t2);
        daftarTransaksi.add(t3);
    }

    private JButton buatTombol(String teks, Color warna) {
        JButton btn = new JButton(teks);
        btn.setBackground(warna);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel buatJudul(String teks) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        return lbl;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LaporanKeuanganGUI().setVisible(true));
    }
}
