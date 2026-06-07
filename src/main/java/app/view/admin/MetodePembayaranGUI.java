package app.view.admin;

import app.model.MetodePembayaran;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI untuk mengelola Metode Pembayaran.
 *
 * CARA PENGGUNAAN:
 * ─────────────────────────────────────────────
 * 1. TAMBAH METODE  → Isi nama metode → pilih status → klik "Tambah"
 * 2. EDIT METODE    → Klik baris di tabel → data otomatis terisi → ubah → klik "Update"
 * 3. HAPUS METODE   → Klik baris di tabel → klik "Hapus"
 * 4. PROSES BAYAR   → Masukkan total + pilih metode → klik "Proses Pembayaran"
 * 5. BERSIHKAN FORM → Klik "Batal"
 * ─────────────────────────────────────────────
 */
public class MetodePembayaranGUI extends JFrame {

    // ── Form fields ────────────────────────────────────────────────────────
    private JTextField txtMetode;
    private JCheckBox  chkStatus;

    // ── Tabel ──────────────────────────────────────────────────────────────
    private JTable tabel;
    private DefaultTableModel modelTabel;

    // ── Panel proses bayar ─────────────────────────────────────────────────
    private JTextField txtTotal;
    private JComboBox<String> cmbPilihMetode;
    private JLabel lblHasilProses;

    // ── Data ───────────────────────────────────────────────────────────────
    private List<MetodePembayaran> daftarMetode = new ArrayList<>();
    private int selectedRow = -1;

    // ── Tema warna ─────────────────────────────────────────────────────────
    private static final Color WARNA_UTAMA  = new Color(41, 128, 185);
    private static final Color WARNA_SUKSES = new Color(39, 174, 96);
    private static final Color WARNA_BAHAYA = new Color(192, 57, 43);
    private static final Color WARNA_NETRAL = new Color(149, 165, 166);
    private static final Color WARNA_BG     = new Color(245, 246, 250);
    private static final Color WARNA_PANEL  = Color.WHITE;

    public MetodePembayaranGUI() {
        setTitle("Manajemen Metode Pembayaran");
        setSize(820, 560);
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

        panelUtama.add(buatHeader(),        BorderLayout.NORTH);
        panelUtama.add(buatPanelTengah(),   BorderLayout.CENTER);
        panelUtama.add(buatPanelProsesBayar(), BorderLayout.SOUTH);

        add(panelUtama);
    }

    private JPanel buatHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(39, 174, 96));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel judul = new JLabel("💳  Manajemen Metode Pembayaran");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);

        JLabel petunjuk = new JLabel("Klik baris tabel untuk memilih metode");
        petunjuk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        petunjuk.setForeground(new Color(200, 255, 220));

        panel.add(judul,    BorderLayout.WEST);
        panel.add(petunjuk, BorderLayout.EAST);
        return panel;
    }

    private JSplitPane buatPanelTengah() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buatPanelForm(), buatPanelTabel());
        split.setDividerLocation(300);
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
        gbc.insets = new Insets(8, 5, 8, 5);

        txtMetode = buatTextField(true);
        chkStatus = new JCheckBox("Aktif");
        chkStatus.setBackground(WARNA_PANEL);
        chkStatus.setSelected(true);

        // Baris 0: Nama Metode
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        JLabel lblMetode = new JLabel("Nama Metode:");
        lblMetode.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        kartu.add(lblMetode, gbc);

        gbc.gridx = 1; gbc.weightx = 0.6;
        txtMetode.setToolTipText("Contoh: Tunai, Transfer Bank, QRIS, Kartu Debit");
        kartu.add(txtMetode, gbc);

        // Baris 1: Status
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        kartu.add(lblStatus, gbc);

        gbc.gridx = 1; gbc.weightx = 0.6;
        chkStatus.setToolTipText("Centang jika metode ini sedang tersedia/aktif");
        kartu.add(chkStatus, gbc);

        // Info box
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JTextArea info = new JTextArea(
            "ℹ️ Contoh metode:\n" +
            "• Tunai\n• Transfer Bank\n• QRIS\n• Kartu Debit\n• Kartu Kredit");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        info.setBackground(new Color(235, 245, 255));
        info.setEditable(false);
        info.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        kartu.add(info, gbc);

        panel.add(buatJudul("📝 Form Metode"), BorderLayout.NORTH);
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

        btnTambah.setToolTipText("Isi nama metode lalu klik ini");
        btnUpdate.setToolTipText("Klik baris tabel → edit → klik Update");
        btnHapus .setToolTipText("Klik baris tabel → klik Hapus");
        btnBatal .setToolTipText("Kosongkan form");

        btnTambah.addActionListener(e -> tambahMetode());
        btnUpdate.addActionListener(e -> updateMetode());
        btnHapus .addActionListener(e -> hapusMetode());
        btnBatal .addActionListener(e -> bersihkanForm());

        panel.add(btnTambah); panel.add(btnUpdate);
        panel.add(btnHapus);  panel.add(btnBatal);
        return panel;
    }

    // ── PANEL TABEL ────────────────────────────────────────────────────────────
    private JPanel buatPanelTabel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(WARNA_BG);

        modelTabel = new DefaultTableModel(new String[]{"Metode Pembayaran", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabel.setRowHeight(30);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabel.getTableHeader().setBackground(new Color(39, 174, 96));
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.setSelectionBackground(new Color(189, 238, 210));
        tabel.setGridColor(new Color(230, 230, 230));

        tabel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { pilihanBaris(); }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));

        panel.add(buatJudul("📋 Daftar Metode Pembayaran"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── PANEL PROSES BAYAR ─────────────────────────────────────────────────────
    private JPanel buatPanelProsesBayar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(WARNA_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));

        txtTotal = buatTextField(true);
        txtTotal.setPreferredSize(new Dimension(140, 28));
        txtTotal.setToolTipText("Masukkan total belanja dalam rupiah, contoh: 50000");

        cmbPilihMetode = new JComboBox<>();
        cmbPilihMetode.setPreferredSize(new Dimension(150, 28));
        cmbPilihMetode.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbPilihMetode.setToolTipText("Pilih metode pembayaran yang tersedia");

        lblHasilProses = new JLabel("—");
        lblHasilProses.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblHasilProses.setForeground(WARNA_SUKSES);

        JButton btnProses = buatTombol("💳 Proses Pembayaran", new Color(142, 68, 173));
        btnProses.setPreferredSize(new Dimension(180, 30));
        btnProses.addActionListener(e -> prosesPembayaran());
        btnProses.setToolTipText("Masukkan total + pilih metode → klik ini");

        panel.add(new JLabel("💡 Proses Pembayaran:"));
        panel.add(new JLabel("Total (Rp):")); panel.add(txtTotal);
        panel.add(new JLabel("Metode:")); panel.add(cmbPilihMetode);
        panel.add(btnProses);
        panel.add(new JLabel("Hasil:")); panel.add(lblHasilProses);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIKA AKSI
    // ─────────────────────────────────────────────────────────────────────────
    private void tambahMetode() {
        String nama = txtMetode.getText().trim();
        if (nama.isEmpty()) {
            tampilkanPesan("Nama metode tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        daftarMetode.add(new MetodePembayaran(nama, chkStatus.isSelected()));
        refreshTabel();
        bersihkanForm();
        tampilkanPesan("Metode pembayaran berhasil ditambahkan!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateMetode() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih baris metode di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nama = txtMetode.getText().trim();
        if (nama.isEmpty()) {
            tampilkanPesan("Nama metode tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        MetodePembayaran m = daftarMetode.get(selectedRow);
        m.setMetodeMetodePembayaran(nama);
        m.setStatus(chkStatus.isSelected());
        refreshTabel();
        bersihkanForm();
        tampilkanPesan("Metode berhasil diupdate!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void hapusMetode() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih baris metode di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus metode ini?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            daftarMetode.remove(selectedRow);
            refreshTabel();
            bersihkanForm();
        }
    }

    private void prosesPembayaran() {
        String totalStr = txtTotal.getText().trim();
        String metodeNama = (String) cmbPilihMetode.getSelectedItem();
        if (totalStr.isEmpty() || metodeNama == null) {
            lblHasilProses.setText("Isi total dan pilih metode!");
            lblHasilProses.setForeground(WARNA_BAHAYA);
            return;
        }
        try {
            int total = Integer.parseInt(totalStr);
            for (MetodePembayaran m : daftarMetode) {
                if (m.getMetode().equals(metodeNama)) {
                    boolean berhasil = m.prosesPembayaran(total);
                    lblHasilProses.setText(berhasil
                        ? "✅ Pembayaran Rp " + total + " via " + metodeNama + " berhasil!"
                        : "❌ Metode tidak aktif, gunakan metode lain.");
                    lblHasilProses.setForeground(berhasil ? WARNA_SUKSES : WARNA_BAHAYA);
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            lblHasilProses.setText("Total harus berupa angka!");
            lblHasilProses.setForeground(WARNA_BAHAYA);
        }
    }

    private void pilihanBaris() {
        selectedRow = tabel.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < daftarMetode.size()) {
            MetodePembayaran m = daftarMetode.get(selectedRow);
            txtMetode.setText(m.getMetode());
            chkStatus.setSelected(m.getStatus());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshTabel() {
        modelTabel.setRowCount(0);
        cmbPilihMetode.removeAllItems();
        for (MetodePembayaran m : daftarMetode) {
            modelTabel.addRow(new Object[]{
                m.getMetode(), m.getStatus() ? "✅ Aktif" : "❌ Nonaktif"
            });
            if (m.getStatus()) cmbPilihMetode.addItem(m.getMetode());
        }
    }

    private void bersihkanForm() {
        txtMetode.setText("");
        chkStatus.setSelected(true);
        selectedRow = -1;
        tabel.clearSelection();
    }

    private void muatDataContoh() {
        daftarMetode.add(new MetodePembayaran("Tunai",        true));
        daftarMetode.add(new MetodePembayaran("Transfer Bank", true));
        daftarMetode.add(new MetodePembayaran("QRIS",          true));
        daftarMetode.add(new MetodePembayaran("Kartu Debit",   false));
    }

    private void tampilkanPesan(String pesan, String judul, int tipe) {
        JOptionPane.showMessageDialog(this, pesan, judul, tipe);
    }

    private JTextField buatTextField(boolean editable) {
        JTextField tf = new JTextField();
        tf.setEditable(editable);
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
        SwingUtilities.invokeLater(() -> new MetodePembayaranGUI().setVisible(true));
    }
}