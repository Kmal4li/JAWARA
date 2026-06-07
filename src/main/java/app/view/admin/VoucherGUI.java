package app.gui;

import app.model.Voucher;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI untuk mengelola Voucher.
 *
 * CARA PENGGUNAAN:
 * ─────────────────────────────────────────────
 * 1. TAMBAH VOUCHER  → Isi semua kolom di panel kiri → klik tombol "Tambah"
 * 2. EDIT VOUCHER    → Klik baris di tabel → data otomatis terisi → ubah → klik "Update"
 * 3. HAPUS VOUCHER   → Klik baris di tabel → klik "Hapus"
 * 4. CEK DISKON      → Masukkan kode voucher + total belanja → klik "Cek Diskon"
 * 5. BERSIHKAN FORM  → Klik "Batal" untuk mengosongkan semua kolom
 * ─────────────────────────────────────────────
 */
public class VoucherGUI extends JFrame {

    // ── Form fields ──────────────────────────────────────────────────────────
    private JTextField txtIdVoucher, txtKodeVoucher, txtNilaiDiskon, txtTanggalKadaluarsa;
    private JComboBox<String> cmbTipePromo;
    private JCheckBox chkStatus;

    // ── Tabel ─────────────────────────────────────────────────────────────────
    private JTable tabel;
    private DefaultTableModel modelTabel;

    // ── Panel cek diskon ──────────────────────────────────────────────────────
    private JTextField txtCekKode, txtTotalBelanja;
    private JLabel lblHasilDiskon;

    // ── Data ──────────────────────────────────────────────────────────────────
    private List<Voucher> daftarVoucher = new ArrayList<>();
    private int idCounter = 1;
    private int selectedRow = -1;

    // ── Warna tema ────────────────────────────────────────────────────────────
    private static final Color WARNA_UTAMA  = new Color(41, 128, 185);
    private static final Color WARNA_SUKSES = new Color(39, 174, 96);
    private static final Color WARNA_BAHAYA = new Color(192, 57, 43);
    private static final Color WARNA_NETRAL = new Color(149, 165, 166);
    private static final Color WARNA_BG     = new Color(245, 246, 250);
    private static final Color WARNA_PANEL  = Color.WHITE;

    public VoucherGUI() {
        setTitle("Manajemen Voucher");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(WARNA_BG);

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
        panelUtama.add(buatPanelCekDiskon(), BorderLayout.SOUTH);

        add(panelUtama);
    }

    /** Banner judul di atas. */
    private JPanel buatHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WARNA_UTAMA);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel judul = new JLabel("🎟  Manajemen Voucher");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);

        JLabel petunjuk = new JLabel("Klik baris tabel untuk memilih voucher");
        petunjuk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        petunjuk.setForeground(new Color(200, 230, 255));

        panel.add(judul, BorderLayout.WEST);
        panel.add(petunjuk, BorderLayout.EAST);
        return panel;
    }

    /** Panel tengah: form (kiri) + tabel (kanan). */
    private JSplitPane buatPanelTengah() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buatPanelForm(), buatPanelTabel());
        split.setDividerLocation(340);
        split.setDividerSize(6);
        split.setBorder(null);
        return split;
    }

    // ── PANEL FORM ────────────────────────────────────────────────────────────
    private JPanel buatPanelForm() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(WARNA_BG);

        // ── Kartu form ──
        JPanel kartu = new JPanel(new GridBagLayout());
        kartu.setBackground(WARNA_PANEL);
        kartu.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Label & field helpers
        txtIdVoucher       = buatTextField(false);  // read-only, auto-generate
        txtKodeVoucher     = buatTextField(true);
        txtNilaiDiskon     = buatTextField(true);
        txtTanggalKadaluarsa = buatTextField(true);
        cmbTipePromo       = new JComboBox<>(new String[]{"persen", "nominal"});
        chkStatus          = new JCheckBox("Aktif");
        chkStatus.setBackground(WARNA_PANEL);
        chkStatus.setSelected(true);

        String[][] baris = {
            {"ID Voucher",        null},
            {"Kode Voucher",      null},
            {"Tipe Promo",        null},
            {"Nilai Diskon",      null},
            {"Tanggal Kadaluarsa (YYYY-MM-DD)", null},
            {"Status",            null},
        };

        JComponent[] fields = {txtIdVoucher, txtKodeVoucher, cmbTipePromo,
                                txtNilaiDiskon, txtTanggalKadaluarsa, chkStatus};

        String[] tooltips = {
            "ID otomatis, tidak perlu diisi",
            "Contoh: DISKON10, HEMAT20",
            "Pilih 'persen' atau 'nominal' (rupiah)",
            "Angka, contoh: 10 (persen) atau 5000 (rupiah)",
            "Format: 2025-12-31",
            "Centang jika voucher masih berlaku"
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

        panel.add(buatJudul("📝 Form Voucher"), BorderLayout.NORTH);
        panel.add(kartu, BorderLayout.CENTER);
        panel.add(buatPanelTombol(), BorderLayout.SOUTH);
        return panel;
    }

    /** Tombol aksi utama. */
    private JPanel buatPanelTombol() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.setBackground(WARNA_BG);

        JButton btnTambah = buatTombol("➕ Tambah",  WARNA_SUKSES);
        JButton btnUpdate = buatTombol("✏ Update",   WARNA_UTAMA);
        JButton btnHapus  = buatTombol("🗑 Hapus",   WARNA_BAHAYA);
        JButton btnBatal  = buatTombol("✖ Batal",    WARNA_NETRAL);

        btnTambah.addActionListener(e -> tambahVoucher());
        btnUpdate.addActionListener(e -> updateVoucher());
        btnHapus .addActionListener(e -> hapusVoucher());
        btnBatal .addActionListener(e -> bersihkanForm());

        // Tooltip tombol
        btnTambah.setToolTipText("Isi form lalu klik ini untuk menyimpan voucher baru");
        btnUpdate.setToolTipText("Klik baris tabel → ubah data → klik Update");
        btnHapus .setToolTipText("Klik baris tabel → klik Hapus untuk menghapus");
        btnBatal .setToolTipText("Kosongkan semua kolom form");

        panel.add(btnTambah); panel.add(btnUpdate);
        panel.add(btnHapus);  panel.add(btnBatal);
        return panel;
    }

    // ── PANEL TABEL ───────────────────────────────────────────────────────────
    private JPanel buatPanelTabel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(WARNA_BG);

        String[] kolom = {"ID", "Kode Voucher", "Tipe Promo", "Nilai Diskon",
                          "Tanggal Kadaluarsa", "Status"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabel.setRowHeight(26);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(WARNA_UTAMA);
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.setSelectionBackground(new Color(189, 215, 238));
        tabel.setGridColor(new Color(230, 230, 230));

        // Saat baris diklik → isi form otomatis
        tabel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { pilihanBaris(); }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));

        panel.add(buatJudul("📋 Daftar Voucher"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── PANEL CEK DISKON ──────────────────────────────────────────────────────
    private JPanel buatPanelCekDiskon() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(WARNA_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));

        txtCekKode     = buatTextField(true); txtCekKode.setPreferredSize(new Dimension(130, 28));
        txtTotalBelanja = buatTextField(true); txtTotalBelanja.setPreferredSize(new Dimension(130, 28));
        lblHasilDiskon = new JLabel("—");
        lblHasilDiskon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblHasilDiskon.setForeground(WARNA_SUKSES);

        JButton btnCek = buatTombol("🔍 Cek Diskon", WARNA_UTAMA);
        btnCek.setPreferredSize(new Dimension(140, 30));
        btnCek.addActionListener(e -> cekDiskon());
        btnCek.setToolTipText("Masukkan kode voucher dan total belanja, lalu klik ini");

        panel.add(new JLabel("💡 Cek Diskon:"));
        panel.add(new JLabel("Kode:")); panel.add(txtCekKode);
        panel.add(new JLabel("Total (Rp):")); panel.add(txtTotalBelanja);
        panel.add(btnCek);
        panel.add(new JLabel("Hasil:"));
        panel.add(lblHasilDiskon);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIKA AKSI
    // ─────────────────────────────────────────────────────────────────────────
    private void tambahVoucher() {
        if (!validasiForm()) return;

        Voucher v = new Voucher(
                idCounter++,
                txtKodeVoucher.getText().trim(),
                (String) cmbTipePromo.getSelectedItem(),
                Integer.parseInt(txtNilaiDiskon.getText().trim()),
                txtTanggalKadaluarsa.getText().trim(),
                chkStatus.isSelected()
        );
        daftarVoucher.add(v);
        refreshTabel();
        bersihkanForm();
        tampilkanPesan("Voucher berhasil ditambahkan!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateVoucher() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih baris voucher di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validasiForm()) return;

        Voucher v = daftarVoucher.get(selectedRow);
        v.setKodeVoucher(txtKodeVoucher.getText().trim());
        v.setTipePromo((String) cmbTipePromo.getSelectedItem());
        v.setNilaiDiskon(Integer.parseInt(txtNilaiDiskon.getText().trim()));
        v.setTanggalKadaluarsa(txtTanggalKadaluarsa.getText().trim());
        v.setStatus(chkStatus.isSelected());

        refreshTabel();
        bersihkanForm();
        tampilkanPesan("Voucher berhasil diupdate!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void hapusVoucher() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih baris voucher di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus voucher ini?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            daftarVoucher.remove(selectedRow);
            refreshTabel();
            bersihkanForm();
        }
    }

    private void cekDiskon() {
        String kode = txtCekKode.getText().trim();
        String totalStr = txtTotalBelanja.getText().trim();

        if (kode.isEmpty() || totalStr.isEmpty()) {
            lblHasilDiskon.setText("Isi kode dan total!");
            lblHasilDiskon.setForeground(WARNA_BAHAYA);
            return;
        }
        try {
            int total = Integer.parseInt(totalStr);
            for (Voucher v : daftarVoucher) {
                if (v.getKodeVoucher().equalsIgnoreCase(kode)) {
                    int diskon = v.hitungDiskon(total);
                    lblHasilDiskon.setText(
                        v.isValid()
                            ? "Diskon: Rp " + diskon + "  →  Bayar: Rp " + (total - diskon)
                            : "Voucher sudah tidak aktif!"
                    );
                    lblHasilDiskon.setForeground(v.isValid() ? WARNA_SUKSES : WARNA_BAHAYA);
                    return;
                }
            }
            lblHasilDiskon.setText("Kode voucher tidak ditemukan.");
            lblHasilDiskon.setForeground(WARNA_BAHAYA);
        } catch (NumberFormatException ex) {
            lblHasilDiskon.setText("Total belanja harus angka!");
            lblHasilDiskon.setForeground(WARNA_BAHAYA);
        }
    }

    private void pilihanBaris() {
        selectedRow = tabel.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < daftarVoucher.size()) {
            Voucher v = daftarVoucher.get(selectedRow);
            txtIdVoucher.setText(String.valueOf(v.getIdVoucher()));
            txtKodeVoucher.setText(v.getKodeVoucher());
            cmbTipePromo.setSelectedItem(v.getTipePromo());
            txtNilaiDiskon.setText(String.valueOf(v.getNilaiDiskon()));
            txtTanggalKadaluarsa.setText(v.getTanggalKadaluarsa());
            chkStatus.setSelected(v.getStatus());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshTabel() {
        modelTabel.setRowCount(0);
        for (Voucher v : daftarVoucher) {
            modelTabel.addRow(new Object[]{
                v.getIdVoucher(), v.getKodeVoucher(), v.getTipePromo(),
                (v.getTipePromo().equals("persen") ? v.getNilaiDiskon() + "%" : "Rp " + v.getNilaiDiskon()),
                v.getTanggalKadaluarsa(), v.getStatus() ? "✅ Aktif" : "❌ Nonaktif"
            });
        }
    }

    private void bersihkanForm() {
        txtIdVoucher.setText("");
        txtKodeVoucher.setText("");
        txtNilaiDiskon.setText("");
        txtTanggalKadaluarsa.setText("");
        cmbTipePromo.setSelectedIndex(0);
        chkStatus.setSelected(true);
        selectedRow = -1;
        tabel.clearSelection();
    }

    private boolean validasiForm() {
        if (txtKodeVoucher.getText().trim().isEmpty()) {
            tampilkanPesan("Kode voucher tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtNilaiDiskon.getText().trim());
        } catch (NumberFormatException e) {
            tampilkanPesan("Nilai diskon harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtTanggalKadaluarsa.getText().trim().isEmpty()) {
            tampilkanPesan("Tanggal kadaluarsa tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void tampilkanPesan(String pesan, String judul, int tipe) {
        JOptionPane.showMessageDialog(this, pesan, judul, tipe);
    }

    private void muatDataContoh() {
        daftarVoucher.add(new Voucher(idCounter++, "HEMAT10", "persen",   10, "2025-12-31", true));
        daftarVoucher.add(new Voucher(idCounter++, "DISKON5K","nominal", 5000,"2025-06-30", true));
        daftarVoucher.add(new Voucher(idCounter++, "EXPIRED", "persen",   20, "2024-01-01", false));
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
        SwingUtilities.invokeLater(() -> new VoucherGUI().setVisible(true));
    }
}