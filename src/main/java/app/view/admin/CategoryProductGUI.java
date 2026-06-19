package app.view.admin;

import app.model.CategoryProduct;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI untuk mengelola Kategori Produk sesuai class diagram.
 *
 * CARA PENGGUNAAN:
 * ─────────────────────────────────────────────
 * 1. TAMBAH KATEGORI → Isi semua kolom → klik "Tambah"
 * 2. EDIT KATEGORI   → Klik baris di tabel → ubah data → klik "Update"
 * 3. HAPUS KATEGORI  → Klik baris di tabel → klik "Hapus"
 * 4. BERSIHKAN FORM  → Klik "Batal"
 * ─────────────────────────────────────────────
 *
 * Atribut class diagram: idKategori, namaKategori, deskripsi
 * Method: getIdKategori(), setIdKategori(), getNamaKategori(), setNamaKategori(),
 *         getDeskripsi(), setDeskripsi(), tambahKategori(), updateKategori(), hapusKategori()
 */
public class CategoryProductGUI extends JFrame {

    // ── Form fields ──────────────────────────────────────────────────────────
    private JTextField txtIdKategori, txtNamaKategori;
    private JTextArea txtDeskripsi;

    // ── Tabel ─────────────────────────────────────────────────────────────────
    private JTable tabel;
    private DefaultTableModel modelTabel;

    // ── Data ──────────────────────────────────────────────────────────────────
    private List<CategoryProduct> daftarKategori = new ArrayList<>();
    private int idCounter = 1;
    private int selectedRow = -1;

    // ── Warna tema ────────────────────────────────────────────────────────────
    private static final Color WARNA_UTAMA  = new Color(142, 68, 173);
    private static final Color WARNA_SUKSES = new Color(39, 174, 96);
    private static final Color WARNA_BAHAYA = new Color(192, 57, 43);
    private static final Color WARNA_NETRAL = new Color(149, 165, 166);
    private static final Color WARNA_BG     = new Color(245, 246, 250);
    private static final Color WARNA_PANEL  = Color.WHITE;

    public CategoryProductGUI() {
        setTitle("Manajemen Kategori Produk");
        setSize(850, 520);
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
        panelUtama.add(buatPanelTengah(), BorderLayout.CENTER);

        add(panelUtama);
    }

    private JPanel buatHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WARNA_UTAMA);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel judul = new JLabel("🏷  Manajemen Kategori Produk");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);

        JLabel petunjuk = new JLabel("Klik baris tabel untuk memilih kategori");
        petunjuk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        petunjuk.setForeground(new Color(230, 210, 255));

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

        txtIdKategori   = buatTextField(false);
        txtNamaKategori = buatTextField(true);
        txtDeskripsi    = new JTextArea(4, 20);
        txtDeskripsi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDeskripsi.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);

        // Baris 0: ID Kategori
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        kartu.add(buatLabel("ID Kategori:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        txtIdKategori.setToolTipText("ID otomatis, tidak perlu diisi");
        kartu.add(txtIdKategori, gbc);

        // Baris 1: Nama Kategori
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        kartu.add(buatLabel("Nama Kategori:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        txtNamaKategori.setToolTipText("Contoh: Makanan, Minuman, Snack");
        kartu.add(txtNamaKategori, gbc);

        // Baris 2: Deskripsi
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.35;
        gbc.anchor = GridBagConstraints.NORTH;
        kartu.add(buatLabel("Deskripsi:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        txtDeskripsi.setToolTipText("Deskripsi singkat kategori produk");
        kartu.add(new JScrollPane(txtDeskripsi), gbc);

        panel.add(buatJudul("📝 Form Kategori"), BorderLayout.NORTH);
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

        btnTambah.setToolTipText("Isi form lalu klik ini untuk menyimpan kategori baru");
        btnUpdate.setToolTipText("Klik baris tabel → ubah data → klik Update");
        btnHapus.setToolTipText("Klik baris tabel → klik Hapus");
        btnBatal.setToolTipText("Kosongkan semua kolom form");

        btnTambah.addActionListener(e -> tambahKategori());
        btnUpdate.addActionListener(e -> updateKategori());
        btnHapus.addActionListener(e -> hapusKategori());
        btnBatal.addActionListener(e -> bersihkanForm());

        panel.add(btnTambah); panel.add(btnUpdate);
        panel.add(btnHapus);  panel.add(btnBatal);
        return panel;
    }

    // ── PANEL TABEL ───────────────────────────────────────────────────────────
    private JPanel buatPanelTabel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(WARNA_BG);

        String[] kolom = {"ID", "Nama Kategori", "Deskripsi"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabel.setRowHeight(28);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(WARNA_UTAMA);
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.setSelectionBackground(new Color(210, 190, 235));
        tabel.setGridColor(new Color(230, 230, 230));

        tabel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { pilihanBaris(); }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));

        panel.add(buatJudul("📋 Daftar Kategori"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIKA AKSI (sesuai method class diagram)
    // ─────────────────────────────────────────────────────────────────────────
    private void tambahKategori() {
        if (txtNamaKategori.getText().trim().isEmpty()) {
            tampilkanPesan("Nama kategori tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CategoryProduct cat = new CategoryProduct(
            idCounter++,
            txtNamaKategori.getText().trim(),
            txtDeskripsi.getText().trim()
        );
        daftarKategori.add(cat);
        refreshTabel();
        bersihkanForm();
        tampilkanPesan("Kategori berhasil ditambahkan!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateKategori() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih baris kategori di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (txtNamaKategori.getText().trim().isEmpty()) {
            tampilkanPesan("Nama kategori tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        CategoryProduct cat = daftarKategori.get(selectedRow);
        cat.setNamaKategori(txtNamaKategori.getText().trim());
        cat.setDeskripsi(txtDeskripsi.getText().trim());
        refreshTabel();
        bersihkanForm();
        tampilkanPesan("Kategori berhasil diupdate!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void hapusKategori() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih baris kategori di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus kategori ini?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            daftarKategori.remove(selectedRow);
            refreshTabel();
            bersihkanForm();
        }
    }

    private void pilihanBaris() {
        selectedRow = tabel.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < daftarKategori.size()) {
            CategoryProduct cat = daftarKategori.get(selectedRow);
            txtIdKategori.setText(String.valueOf(cat.getId()));
            txtNamaKategori.setText(cat.getNamaKategori());
            txtDeskripsi.setText(cat.getDeskripsi());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshTabel() {
        modelTabel.setRowCount(0);
        for (CategoryProduct cat : daftarKategori) {
            modelTabel.addRow(new Object[]{
                cat.getId(), cat.getNamaKategori(), cat.getDeskripsi()
            });
        }
    }

    private void bersihkanForm() {
        txtIdKategori.setText("");
        txtNamaKategori.setText("");
        txtDeskripsi.setText("");
        selectedRow = -1;
        tabel.clearSelection();
    }

    private void muatDataContoh() {
        daftarKategori.add(new CategoryProduct(idCounter++, "Makanan",  "Produk makanan ringan dan berat"));
        daftarKategori.add(new CategoryProduct(idCounter++, "Minuman",  "Minuman kemasan dan botol"));
        daftarKategori.add(new CategoryProduct(idCounter++, "Snack",    "Keripik, biskuit, dan camilan"));
    }

    private void tampilkanPesan(String pesan, String judul, int tipe) {
        JOptionPane.showMessageDialog(this, pesan, judul, tipe);
    }

    private JLabel buatLabel(String teks) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return lbl;
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
        SwingUtilities.invokeLater(() -> new CategoryProductGUI().setVisible(true));
    }
}
