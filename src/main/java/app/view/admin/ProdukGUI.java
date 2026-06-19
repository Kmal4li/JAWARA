package app.view.admin;

import app.model.Produk;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI untuk mengelola Produk sesuai class diagram.
 *
 * CARA PENGGUNAAN:
 * ─────────────────────────────────────────────
 * 1. TAMBAH PRODUK  → Isi semua kolom → klik "Tambah"
 * 2. EDIT PRODUK    → Klik baris di tabel → ubah data → klik "Update"
 * 3. HAPUS PRODUK   → Klik baris di tabel → klik "Hapus"
 * 4. BERSIHKAN FORM → Klik "Batal"
 * ─────────────────────────────────────────────
 *
 * Atribut class diagram: idProduk, namaProduk, hargaBeli, hargaJual
 * Method: tambahProduk(), updateProduk(), hapusProduk(), getIdProduk(),
 *         setIdProduk(), getNamaProduk(), setNamaProduk(), getHargaJual(),
 *         setHargaJual(), getHargaBeli(), setHargaBeli()
 */
public class ProdukGUI extends JFrame {

    // ── Form fields ──────────────────────────────────────────────────────────
    private JTextField txtIdProduk, txtNamaProduk, txtHargaBeli, txtHargaJual;

    // ── Tabel ─────────────────────────────────────────────────────────────────
    private JTable tabel;
    private DefaultTableModel modelTabel;

    // ── Data ──────────────────────────────────────────────────────────────────
    private List<Produk> daftarProduk = new ArrayList<>();
    private int idCounter = 1;
    private int selectedRow = -1;

    // ── Warna tema ────────────────────────────────────────────────────────────
    private static final Color WARNA_UTAMA  = new Color(41, 128, 185);
    private static final Color WARNA_SUKSES = new Color(39, 174, 96);
    private static final Color WARNA_BAHAYA = new Color(192, 57, 43);
    private static final Color WARNA_NETRAL = new Color(149, 165, 166);
    private static final Color WARNA_BG     = new Color(245, 246, 250);
    private static final Color WARNA_PANEL  = Color.WHITE;

    public ProdukGUI() {
        setTitle("Manajemen Produk");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initKomponen();
        muatDataContoh();
        refreshTabel();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // INISIALISASI KOMPONEN
    // ─────────────────────────────────────────────────────────────────────────
    private void initKomponen() {
        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        panelUtama.setBackground(WARNA_BG);
        panelUtama.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelUtama.add(buatHeader(), BorderLayout.NORTH);
        panelUtama.add(buatPanelTengah(), BorderLayout.CENTER);

        add(panelUtama);
    }

    private JPanel buatHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WARNA_UTAMA);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel judul = new JLabel("📦  Manajemen Produk");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);

        JLabel petunjuk = new JLabel("Klik baris tabel untuk memilih produk");
        petunjuk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        petunjuk.setForeground(new Color(200, 230, 255));

        panel.add(judul, BorderLayout.WEST);
        panel.add(petunjuk, BorderLayout.EAST);
        return panel;
    }

    private JSplitPane buatPanelTengah() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buatPanelForm(), buatPanelTabel());
        split.setDividerLocation(320);
        split.setDividerSize(6);
        split.setBorder(null);
        return split;
    }

    // ── PANEL FORM ────────────────────────────────────────────────────────────
    private JPanel buatPanelForm() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(WARNA_BG);

        JPanel kartu = new JPanel(new GridBagLayout());
        kartu.setBackground(WARNA_PANEL);
        kartu.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        txtIdProduk   = buatTextField(false); // read-only
        txtNamaProduk = buatTextField(true);
        txtHargaBeli  = buatTextField(true);
        txtHargaJual  = buatTextField(true);

        String[][] baris = {
            {"ID Produk", null},
            {"Nama Produk", null},
            {"Harga Beli (Rp)", null},
            {"Harga Jual (Rp)", null},
        };

        JComponent[] fields = {txtIdProduk, txtNamaProduk, txtHargaBeli, txtHargaJual};

        String[] tooltips = {
            "ID otomatis, tidak perlu diisi",
            "Masukkan nama produk",
            "Harga beli dalam rupiah, contoh: 10000",
            "Harga jual dalam rupiah, contoh: 15000"
        };

        for (int i = 0; i < fields.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.35;
            JLabel lbl = new JLabel(baris[i][0] + ":");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            kartu.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.65;
            fields[i].setToolTipText(tooltips[i]);
            kartu.add(fields[i], gbc);
        }

        panel.add(buatJudul("📝 Form Produk"), BorderLayout.NORTH);
        panel.add(kartu, BorderLayout.CENTER);
        panel.add(buatPanelTombol(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buatPanelTombol() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.setBackground(WARNA_BG);

        JButton btnTambah = buatTombol("➕ Tambah",  WARNA_SUKSES);
        JButton btnUpdate = buatTombol("✏ Update",   WARNA_UTAMA);
        JButton btnHapus  = buatTombol("🗑 Hapus",   WARNA_BAHAYA);
        JButton btnBatal  = buatTombol("✖ Batal",    WARNA_NETRAL);

        btnTambah.setToolTipText("Isi form lalu klik ini untuk menyimpan produk baru");
        btnUpdate.setToolTipText("Klik baris tabel → ubah data → klik Update");
        btnHapus.setToolTipText("Klik baris tabel → klik Hapus untuk menghapus");
        btnBatal.setToolTipText("Kosongkan semua kolom form");

        btnTambah.addActionListener(e -> tambahProduk());
        btnUpdate.addActionListener(e -> updateProduk());
        btnHapus.addActionListener(e -> hapusProduk());
        btnBatal.addActionListener(e -> bersihkanForm());

        panel.add(btnTambah); panel.add(btnUpdate);
        panel.add(btnHapus);  panel.add(btnBatal);
        return panel;
    }

    // ── PANEL TABEL ───────────────────────────────────────────────────────────
    private JPanel buatPanelTabel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(WARNA_BG);

        String[] kolom = {"ID", "Nama Produk", "Harga Beli", "Harga Jual"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabel.setRowHeight(28);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(WARNA_UTAMA);
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.setSelectionBackground(new Color(189, 215, 238));
        tabel.setGridColor(new Color(230, 230, 230));

        tabel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { pilihanBaris(); }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));

        panel.add(buatJudul("📋 Daftar Produk"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIKA AKSI (sesuai method class diagram)
    // ─────────────────────────────────────────────────────────────────────────
    private void tambahProduk() {
        if (!validasiForm()) return;

        Produk p = new Produk(
            idCounter++,
            txtNamaProduk.getText().trim(),
            Double.parseDouble(txtHargaBeli.getText().trim()),
            Double.parseDouble(txtHargaJual.getText().trim()),
            0, null, 0
        );
        daftarProduk.add(p);
        refreshTabel();
        bersihkanForm();
        tampilkanPesan("Produk berhasil ditambahkan!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateProduk() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih baris produk di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validasiForm()) return;

        Produk p = daftarProduk.get(selectedRow);
        p.setNamaProduk(txtNamaProduk.getText().trim());
        p.setHargaBeli(Double.parseDouble(txtHargaBeli.getText().trim()));
        p.setHargaJual(Double.parseDouble(txtHargaJual.getText().trim()));

        refreshTabel();
        bersihkanForm();
        tampilkanPesan("Produk berhasil diupdate!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void hapusProduk() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih baris produk di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus produk ini?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            daftarProduk.remove(selectedRow);
            refreshTabel();
            bersihkanForm();
        }
    }

    private void pilihanBaris() {
        selectedRow = tabel.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < daftarProduk.size()) {
            Produk p = daftarProduk.get(selectedRow);
            txtIdProduk.setText(String.valueOf(p.getId()));
            txtNamaProduk.setText(p.getNamaProduk());
            txtHargaBeli.setText(String.valueOf((int) p.getHargaBeli()));
            txtHargaJual.setText(String.valueOf((int) p.getHargaJual()));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshTabel() {
        modelTabel.setRowCount(0);
        for (Produk p : daftarProduk) {
            modelTabel.addRow(new Object[]{
                p.getId(), p.getNamaProduk(),
                "Rp " + (int) p.getHargaBeli(),
                "Rp " + (int) p.getHargaJual()
            });
        }
    }

    private void bersihkanForm() {
        txtIdProduk.setText("");
        txtNamaProduk.setText("");
        txtHargaBeli.setText("");
        txtHargaJual.setText("");
        selectedRow = -1;
        tabel.clearSelection();
    }

    private boolean validasiForm() {
        if (txtNamaProduk.getText().trim().isEmpty()) {
            tampilkanPesan("Nama produk tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(txtHargaBeli.getText().trim());
        } catch (NumberFormatException e) {
            tampilkanPesan("Harga beli harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(txtHargaJual.getText().trim());
        } catch (NumberFormatException e) {
            tampilkanPesan("Harga jual harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void muatDataContoh() {
        daftarProduk.add(new Produk(idCounter++, "Indomie Goreng",    2500, 3500, 100, null, 0));
        daftarProduk.add(new Produk(idCounter++, "Coca-Cola 330ml",   4000, 5500, 50, null, 0));
        daftarProduk.add(new Produk(idCounter++, "Roti Tawar Sari Roti", 12000, 15000, 30, null, 0));
    }

    private void tampilkanPesan(String pesan, String judul, int tipe) {
        JOptionPane.showMessageDialog(this, pesan, judul, tipe);
    }

    private JTextField buatTextField(boolean editable) {
        JTextField tf = new JTextField();
        tf.setEditable(editable);
        tf.setBackground(editable ? Color.WHITE : new Color(245, 245, 245));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        return tf;
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
        SwingUtilities.invokeLater(() -> new ProdukGUI().setVisible(true));
    }
}
