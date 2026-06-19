package app.view.admin;

import app.model.LaporanStock;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * GUI untuk Laporan Stok sesuai class diagram.
 *
 * CARA PENGGUNAAN:
 * ─────────────────────────────────────────────
 * 1. Klik "Generate Laporan" untuk memperbarui laporan stok
 * 2. Lihat ringkasan stok masuk & keluar di panel atas
 * 3. Lihat detail per produk di tabel
 * ─────────────────────────────────────────────
 *
 * Atribut class diagram: stokMasuk, stokKeluar
 * Method: getStokMasuk(), getStokKeluar(), generateLaporan()
 */
public class LaporanStockGUI extends JFrame {

    // ── Display fields ────────────────────────────────────────────────────────
    private JLabel lblStokMasuk, lblStokKeluar, lblSelisih;

    // ── Tabel ─────────────────────────────────────────────────────────────────
    private JTable tabel;
    private DefaultTableModel modelTabel;

    // ── Data ──────────────────────────────────────────────────────────────────
    private List<Object[]> daftarLaporanStok = new ArrayList<>();

    // ── Warna tema ────────────────────────────────────────────────────────────
    private static final Color WARNA_UTAMA  = new Color(127, 140, 141);
    private static final Color WARNA_SUKSES = new Color(39, 174, 96);
    private static final Color WARNA_BAHAYA = new Color(192, 57, 43);
    private static final Color WARNA_BG     = new Color(245, 246, 250);
    private static final Color WARNA_PANEL  = Color.WHITE;

    public LaporanStockGUI() {
        setTitle("Laporan Stok");
        setSize(800, 520);
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
        panel.setBackground(new Color(52, 73, 94));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel judul = new JLabel("📈  Laporan Stok");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);

        JLabel petunjuk = new JLabel("Klik 'Generate Laporan' untuk memperbarui");
        petunjuk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        petunjuk.setForeground(new Color(189, 195, 199));

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

        lblStokMasuk = new JLabel("0", SwingConstants.CENTER);
        lblStokMasuk.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblStokMasuk.setForeground(WARNA_SUKSES);
        panel.add(buatKartuRingkasan("📥 Total Stok Masuk", lblStokMasuk));

        lblStokKeluar = new JLabel("0", SwingConstants.CENTER);
        lblStokKeluar.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblStokKeluar.setForeground(WARNA_BAHAYA);
        panel.add(buatKartuRingkasan("📤 Total Stok Keluar", lblStokKeluar));

        lblSelisih = new JLabel("0", SwingConstants.CENTER);
        lblSelisih.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblSelisih.setForeground(new Color(41, 128, 185));
        panel.add(buatKartuRingkasan("📊 Selisih Stok", lblSelisih));

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

        String[] kolom = {"Nama Produk", "Stok Masuk", "Stok Keluar", "Stok Akhir", "Tanggal"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabel.setRowHeight(26);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(new Color(52, 73, 94));
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.setSelectionBackground(new Color(210, 215, 220));
        tabel.setGridColor(new Color(230, 230, 230));

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));

        panel.add(buatJudul("📋 Detail Perubahan Stok"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── PANEL TOMBOL ──────────────────────────────────────────────────────────
    private JPanel buatPanelTombol() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        panel.setBackground(WARNA_BG);

        JButton btnGenerate = buatTombol("📊 Generate Laporan", new Color(52, 73, 94));
        btnGenerate.setPreferredSize(new Dimension(200, 35));
        btnGenerate.addActionListener(e -> generateLaporan());
        btnGenerate.setToolTipText("Klik untuk menghitung ulang laporan stok");

        panel.add(btnGenerate);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIKA (sesuai method class diagram)
    // ─────────────────────────────────────────────────────────────────────────
    private void generateLaporan() {
        int totalMasuk = 0;
        int totalKeluar = 0;

        for (Object[] row : daftarLaporanStok) {
            totalMasuk  += (int) row[1];
            totalKeluar += (int) row[2];
        }

        LaporanStock laporan = new LaporanStock(totalMasuk, totalKeluar);

        lblStokMasuk.setText(String.valueOf(laporan.getStokMasuk()));
        lblStokKeluar.setText(String.valueOf(laporan.getStokKeluar()));
        lblSelisih.setText(String.valueOf(laporan.getStokMasuk() - laporan.getStokKeluar()));

        refreshTabel();
        laporan.generateLaporan();
    }

    private void refreshTabel() {
        modelTabel.setRowCount(0);
        for (Object[] row : daftarLaporanStok) {
            modelTabel.addRow(new Object[]{
                row[0],                      // Nama Produk
                row[1],                      // Stok Masuk
                row[2],                      // Stok Keluar
                (int) row[1] - (int) row[2], // Stok Akhir
                row[3]                       // Tanggal
            });
        }
    }

    private void muatDataContoh() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String hari = sdf.format(new Date());

        daftarLaporanStok.add(new Object[]{"Indomie Goreng",     50,  12, hari});
        daftarLaporanStok.add(new Object[]{"Coca-Cola 330ml",    30,   8, hari});
        daftarLaporanStok.add(new Object[]{"Roti Tawar Sari Roti", 20,  5, hari});
        daftarLaporanStok.add(new Object[]{"Aqua 600ml",         100, 35, hari});
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
        SwingUtilities.invokeLater(() -> new LaporanStockGUI().setVisible(true));
    }
}
