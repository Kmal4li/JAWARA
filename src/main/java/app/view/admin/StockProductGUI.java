package app.view.admin;

import app.model.Produk;
import app.model.StockProduct;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GUI untuk mengelola Stok Produk sesuai class diagram.
 *
 * CARA PENGGUNAAN:
 * ─────────────────────────────────────────────
 * 1. Pilih produk di tabel
 * 2. Masukkan jumlah stok
 * 3. Klik "Tambah Stok" atau "Kurang Stok"
 * ─────────────────────────────────────────────
 *
 * Atribut class diagram StockProduct: jumlahStok
 * Method: getJumlahStok(), tambahStok(jumlah), kurangStok(jumlah)
 */
public class StockProductGUI extends JFrame {

    // ── Form fields ──────────────────────────────────────────────────────────
    private JTextField txtJumlahStok;
    private JLabel lblProdukTerpilih;

    // ── Tabel ─────────────────────────────────────────────────────────────────
    private JTable tabel;
    private DefaultTableModel modelTabel;

    // ── Data ──────────────────────────────────────────────────────────────────
    private List<Produk> daftarProduk = new ArrayList<>();
    private Map<Integer, StockProduct> mapStok = new HashMap<>();
    private int selectedRow = -1;

    // ── Warna tema ────────────────────────────────────────────────────────────
    private static final Color WARNA_UTAMA  = new Color(52, 73, 94);
    private static final Color WARNA_SUKSES = new Color(39, 174, 96);
    private static final Color WARNA_BAHAYA = new Color(192, 57, 43);
    private static final Color WARNA_NETRAL = new Color(149, 165, 166);
    private static final Color WARNA_BG     = new Color(245, 246, 250);
    private static final Color WARNA_PANEL  = Color.WHITE;

    public StockProductGUI() {
        setTitle("Kelola Stok Produk");
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initKomponen();
        muatDataContoh();
        refreshTabel();
    }

    private void initKomponen() {
        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        panelUtama.setBackground(WARNA_BG);
        panelUtama.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelUtama.add(buatHeader(), BorderLayout.NORTH);
        panelUtama.add(buatPanelTabel(), BorderLayout.CENTER);
        panelUtama.add(buatPanelAksi(), BorderLayout.SOUTH);

        add(panelUtama);
    }

    private JPanel buatHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WARNA_UTAMA);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel judul = new JLabel("📊  Kelola Stok Produk");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);

        JLabel petunjuk = new JLabel("Pilih produk → masukkan jumlah → Tambah/Kurang Stok");
        petunjuk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        petunjuk.setForeground(new Color(189, 195, 199));

        panel.add(judul, BorderLayout.WEST);
        panel.add(petunjuk, BorderLayout.EAST);
        return panel;
    }

    // ── PANEL TABEL ───────────────────────────────────────────────────────────
    private JPanel buatPanelTabel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(WARNA_BG);

        String[] kolom = {"ID", "Nama Produk", "Harga Jual", "Jumlah Stok"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabel.setRowHeight(28);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(WARNA_UTAMA);
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.setSelectionBackground(new Color(210, 215, 225));
        tabel.setGridColor(new Color(230, 230, 230));

        tabel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { pilihanBaris(); }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));

        panel.add(buatJudul("📋 Daftar Produk & Stok"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── PANEL AKSI ────────────────────────────────────────────────────────────
    private JPanel buatPanelAksi() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(WARNA_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        lblProdukTerpilih = new JLabel("Belum ada produk dipilih");
        lblProdukTerpilih.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblProdukTerpilih.setForeground(WARNA_NETRAL);

        txtJumlahStok = new JTextField(8);
        txtJumlahStok.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtJumlahStok.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        txtJumlahStok.setToolTipText("Masukkan jumlah stok yang akan ditambah/dikurangi");

        JButton btnTambah = buatTombol("📥 Tambah Stok", WARNA_SUKSES);
        btnTambah.setPreferredSize(new Dimension(150, 30));
        btnTambah.addActionListener(e -> tambahStok());
        btnTambah.setToolTipText("Tambah stok produk yang dipilih");

        JButton btnKurang = buatTombol("📤 Kurang Stok", WARNA_BAHAYA);
        btnKurang.setPreferredSize(new Dimension(150, 30));
        btnKurang.addActionListener(e -> kurangStok());
        btnKurang.setToolTipText("Kurangi stok produk yang dipilih");

        panel.add(new JLabel("📦 Produk:"));
        panel.add(lblProdukTerpilih);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(new JLabel("Jumlah:"));
        panel.add(txtJumlahStok);
        panel.add(btnTambah);
        panel.add(btnKurang);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIKA AKSI (sesuai method class diagram StockProduct)
    // ─────────────────────────────────────────────────────────────────────────
    private void tambahStok() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih produk di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int jumlah = Integer.parseInt(txtJumlahStok.getText().trim());
            if (jumlah <= 0) {
                tampilkanPesan("Jumlah harus lebih dari 0!", "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Produk p = daftarProduk.get(selectedRow);
            StockProduct sp = mapStok.get(p.getId());
            sp.tambahStok(jumlah);    // Method dari class diagram
            p.setStok(sp.getJumlahStok());
            refreshTabel();
            txtJumlahStok.setText("");
            tampilkanPesan("Stok " + p.getNamaProduk() + " berhasil ditambah " + jumlah + " unit!",
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            tampilkanPesan("Jumlah harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void kurangStok() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih produk di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int jumlah = Integer.parseInt(txtJumlahStok.getText().trim());
            if (jumlah <= 0) {
                tampilkanPesan("Jumlah harus lebih dari 0!", "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Produk p = daftarProduk.get(selectedRow);
            StockProduct sp = mapStok.get(p.getId());
            if (sp.getJumlahStok() < jumlah) {
                tampilkanPesan("Stok tidak mencukupi! Stok saat ini: " + sp.getJumlahStok(),
                    "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            sp.kurangStok(jumlah);    // Method dari class diagram
            p.setStok(sp.getJumlahStok());
            refreshTabel();
            txtJumlahStok.setText("");
            tampilkanPesan("Stok " + p.getNamaProduk() + " berhasil dikurangi " + jumlah + " unit!",
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            tampilkanPesan("Jumlah harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void pilihanBaris() {
        selectedRow = tabel.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < daftarProduk.size()) {
            Produk p = daftarProduk.get(selectedRow);
            lblProdukTerpilih.setText(p.getNamaProduk() + " (Stok: " + p.getStok() + ")");
            lblProdukTerpilih.setForeground(WARNA_UTAMA);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshTabel() {
        modelTabel.setRowCount(0);
        for (Produk p : daftarProduk) {
            StockProduct sp = mapStok.get(p.getId());
            modelTabel.addRow(new Object[]{
                p.getId(), p.getNamaProduk(),
                "Rp " + (int) p.getHargaJual(),
                sp != null ? sp.getJumlahStok() : p.getStok()
            });
        }
        // Update label jika masih terpilih
        if (selectedRow >= 0 && selectedRow < daftarProduk.size()) {
            Produk p = daftarProduk.get(selectedRow);
            lblProdukTerpilih.setText(p.getNamaProduk() + " (Stok: " + p.getStok() + ")");
        }
    }

    private void muatDataContoh() {
        Produk p1 = new Produk(1, "Indomie Goreng",     2500, 3500, 100, null, 0);
        Produk p2 = new Produk(2, "Coca-Cola 330ml",    4000, 5500, 50, null, 0);
        Produk p3 = new Produk(3, "Roti Tawar Sari Roti", 12000, 15000, 30, null, 0);
        Produk p4 = new Produk(4, "Aqua 600ml",         2000, 3000, 200, null, 0);

        daftarProduk.add(p1); mapStok.put(p1.getId(), new StockProduct(p1.getStok()));
        daftarProduk.add(p2); mapStok.put(p2.getId(), new StockProduct(p2.getStok()));
        daftarProduk.add(p3); mapStok.put(p3.getId(), new StockProduct(p3.getStok()));
        daftarProduk.add(p4); mapStok.put(p4.getId(), new StockProduct(p4.getStok()));
    }

    private void tampilkanPesan(String pesan, String judul, int tipe) {
        JOptionPane.showMessageDialog(this, pesan, judul, tipe);
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
        SwingUtilities.invokeLater(() -> new StockProductGUI().setVisible(true));
    }
}
