package app.view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import app.model.Promo;


public class PromoGUI extends JFrame {

    // ── Form fields ──────────────────────────────────────────────────────────
    private JTextField txtNamaPromo, txtNilaiDiskon, txtTanggalMulai, txtTanggalAkhir;
    private JComboBox<String> cmbTipePromo;
    private JCheckBox chkStatus;

    // ── Tabel ─────────────────────────────────────────────────────────────────
    private JTable tabel;
    private DefaultTableModel modelTabel;

    // ── Panel cek diskon ──────────────────────────────────────────────────────
    private JTextField txtCekTotal;
    private JComboBox<String> cmbPilihPromo;
    private JLabel lblHasilDiskon;

    // ── Data ──────────────────────────────────────────────────────────────────
    private List<Promo> daftarPromo = new ArrayList<>();
    private int selectedRow = -1;

    // ── Warna tema ────────────────────────────────────────────────────────────
    private static final Color WARNA_UTAMA  = new Color(243, 156, 18);
    private static final Color WARNA_SUKSES = new Color(39, 174, 96);
    private static final Color WARNA_BAHAYA = new Color(192, 57, 43);
    private static final Color WARNA_NETRAL = new Color(149, 165, 166);
    private static final Color WARNA_BG     = new Color(245, 246, 250);
    private static final Color WARNA_PANEL  = Color.WHITE;

    public PromoGUI() {
        setTitle("Manajemen Promo");
        setSize(1000, 650);
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
        panelUtama.add(buatPanelCekDiskon(), BorderLayout.SOUTH);

        add(panelUtama);
    }

    private JPanel buatHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WARNA_UTAMA);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel judul = new JLabel("🎉  Manajemen Promo");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 20));
        judul.setForeground(Color.WHITE);

        JLabel petunjuk = new JLabel("Klik baris tabel untuk memilih promo");
        petunjuk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        petunjuk.setForeground(new Color(255, 240, 200));

        panel.add(judul, BorderLayout.WEST);
        panel.add(petunjuk, BorderLayout.EAST);
        return panel;
    }

    private JSplitPane buatPanelTengah() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buatPanelForm(), buatPanelTabel());
        split.setDividerLocation(360);
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
        gbc.insets = new Insets(5, 5, 5, 5);

        txtNamaPromo    = buatTextField(true);
        cmbTipePromo    = new JComboBox<>(new String[]{"persen", "nominal"});
        txtNilaiDiskon  = buatTextField(true);
        txtTanggalMulai = buatTextField(true);
        txtTanggalAkhir = buatTextField(true);
        chkStatus       = new JCheckBox("Aktif");
        chkStatus.setBackground(WARNA_PANEL);
        chkStatus.setSelected(true);

        String[][] baris = {
            {"Nama Promo", null},
            {"Tipe Promo", null},
            {"Nilai Diskon", null},
            {"Tanggal Mulai (YYYY-MM-DD)", null},
            {"Tanggal Akhir (YYYY-MM-DD)", null},
            {"Status", null},
        };

        JComponent[] fields = {txtNamaPromo, cmbTipePromo, txtNilaiDiskon,
                                txtTanggalMulai, txtTanggalAkhir, chkStatus};

        String[] tooltips = {
            "Contoh: Diskon Akhir Tahun, Promo Kemerdekaan",
            "Pilih 'persen' atau 'nominal' (rupiah)",
            "Angka, contoh: 10 (persen) atau 5000 (rupiah)",
            "Format: 2025-01-01",
            "Format: 2025-12-31",
            "Centang jika promo masih berlaku"
        };

        for (int i = 0; i < fields.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.40;
            JLabel lbl = new JLabel(baris[i][0] + ":");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            kartu.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.60;
            fields[i].setToolTipText(tooltips[i]);
            kartu.add(fields[i], gbc);
        }

        panel.add(buatJudul("📝 Form Promo"), BorderLayout.NORTH);
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

        btnTambah.setToolTipText("Isi form lalu klik ini untuk menyimpan promo baru");
        btnUpdate.setToolTipText("Klik baris tabel → ubah data → klik Update");
        btnHapus.setToolTipText("Klik baris tabel → klik Hapus");
        btnBatal.setToolTipText("Kosongkan semua kolom form");

        btnTambah.addActionListener(e -> tambahPromo());
        btnUpdate.addActionListener(e -> updatePromo());
        btnHapus.addActionListener(e -> hapusPromo());
        btnBatal.addActionListener(e -> bersihkanForm());

        panel.add(btnTambah); panel.add(btnUpdate);
        panel.add(btnHapus);  panel.add(btnBatal);
        return panel;
    }

    // ── PANEL TABEL ───────────────────────────────────────────────────────────
    private JPanel buatPanelTabel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(WARNA_BG);

        String[] kolom = {"Nama Promo", "Tipe", "Nilai Diskon", "Mulai", "Akhir", "Status"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabel.setRowHeight(26);
        tabel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(WARNA_UTAMA);
        tabel.getTableHeader().setForeground(Color.WHITE);
        tabel.setSelectionBackground(new Color(255, 230, 180));
        tabel.setGridColor(new Color(230, 230, 230));

        tabel.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { pilihanBaris(); }
        });

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));

        panel.add(buatJudul("📋 Daftar Promo"), BorderLayout.NORTH);
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

        txtCekTotal = buatTextField(true);
        txtCekTotal.setPreferredSize(new Dimension(130, 28));
        txtCekTotal.setToolTipText("Masukkan total belanja dalam rupiah");

        cmbPilihPromo = new JComboBox<>();
        cmbPilihPromo.setPreferredSize(new Dimension(180, 28));
        cmbPilihPromo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbPilihPromo.setToolTipText("Pilih promo yang aktif");

        lblHasilDiskon = new JLabel("—");
        lblHasilDiskon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblHasilDiskon.setForeground(WARNA_SUKSES);

        JButton btnCek = buatTombol("🔍 Cek Diskon", new Color(41, 128, 185));
        btnCek.setPreferredSize(new Dimension(140, 30));
        btnCek.addActionListener(e -> cekDiskon());
        btnCek.setToolTipText("Masukkan total belanja dan pilih promo, lalu klik ini");

        panel.add(new JLabel("💡 Cek Diskon:"));
        panel.add(new JLabel("Total (Rp):")); panel.add(txtCekTotal);
        panel.add(new JLabel("Promo:")); panel.add(cmbPilihPromo);
        panel.add(btnCek);
        panel.add(new JLabel("Hasil:")); panel.add(lblHasilDiskon);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIKA AKSI
    // ─────────────────────────────────────────────────────────────────────────
    private void tambahPromo() {
        if (!validasiForm()) return;

        Promo p = new Promo(
            txtNamaPromo.getText().trim(),
            (String) cmbTipePromo.getSelectedItem(),
            Integer.parseInt(txtNilaiDiskon.getText().trim()),
            txtTanggalMulai.getText().trim(),
            txtTanggalAkhir.getText().trim(),
            chkStatus.isSelected()
        );
        daftarPromo.add(p);
        refreshTabel();
        bersihkanForm();
        tampilkanPesan("Promo berhasil ditambahkan!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updatePromo() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih baris promo di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validasiForm()) return;

        Promo p = daftarPromo.get(selectedRow);
        p.setNamaPromo(txtNamaPromo.getText().trim());
        p.setTipePromo((String) cmbTipePromo.getSelectedItem());
        p.setNilaiDiskon(Integer.parseInt(txtNilaiDiskon.getText().trim()));
        p.setTanggalMulai(txtTanggalMulai.getText().trim());
        p.setTanggalAkhir(txtTanggalAkhir.getText().trim());
        p.setStatus(chkStatus.isSelected());

        refreshTabel();
        bersihkanForm();
        tampilkanPesan("Promo berhasil diupdate!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void hapusPromo() {
        if (selectedRow < 0) {
            tampilkanPesan("Pilih baris promo di tabel terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus promo ini?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            daftarPromo.remove(selectedRow);
            refreshTabel();
            bersihkanForm();
        }
    }

    private void cekDiskon() {
        String totalStr = txtCekTotal.getText().trim();
        String namaPromo = (String) cmbPilihPromo.getSelectedItem();

        if (totalStr.isEmpty() || namaPromo == null) {
            lblHasilDiskon.setText("Isi total dan pilih promo!");
            lblHasilDiskon.setForeground(WARNA_BAHAYA);
            return;
        }
        try {
            int total = Integer.parseInt(totalStr);
            for (Promo p : daftarPromo) {
                if (p.getNamaPromo().equals(namaPromo)) {
                    int diskon = p.hitungJumlahDiskon(total);
                    int bayar  = p.applyPromoToDiskon(total);
                    if (p.isValid()) {
                        lblHasilDiskon.setText("Diskon: Rp" + diskon + "  →  Bayar: Rp" + bayar);
                        lblHasilDiskon.setForeground(WARNA_SUKSES);
                    } else {
                        lblHasilDiskon.setText("Promo sudah tidak aktif!");
                        lblHasilDiskon.setForeground(WARNA_BAHAYA);
                    }
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            lblHasilDiskon.setText("Total harus berupa angka!");
            lblHasilDiskon.setForeground(WARNA_BAHAYA);
        }
    }

    private void pilihanBaris() {
        selectedRow = tabel.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < daftarPromo.size()) {
            Promo p = daftarPromo.get(selectedRow);
            txtNamaPromo.setText(p.getNamaPromo());
            cmbTipePromo.setSelectedItem(p.getTipePromo());
            txtNilaiDiskon.setText(String.valueOf(p.getNilaiDiskon()));
            txtTanggalMulai.setText(p.getTanggalMulai());
            txtTanggalAkhir.setText(p.getTanggalAkhir());
            chkStatus.setSelected(p.getStatus());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPER
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshTabel() {
        modelTabel.setRowCount(0);
        cmbPilihPromo.removeAllItems();
        for (Promo p : daftarPromo) {
            modelTabel.addRow(new Object[]{
                p.getNamaPromo(), p.getTipePromo(),
                (p.getTipePromo().equals("persen") ? p.getNilaiDiskon() + "%" : "Rp " + p.getNilaiDiskon()),
                p.getTanggalMulai(), p.getTanggalAkhir(),
                p.getStatus() ? "✅ Aktif" : "❌ Nonaktif"
            });
            if (p.getStatus()) cmbPilihPromo.addItem(p.getNamaPromo());
        }
    }

    private void bersihkanForm() {
        txtNamaPromo.setText("");
        cmbTipePromo.setSelectedIndex(0);
        txtNilaiDiskon.setText("");
        txtTanggalMulai.setText("");
        txtTanggalAkhir.setText("");
        chkStatus.setSelected(true);
        selectedRow = -1;
        tabel.clearSelection();
    }

    private boolean validasiForm() {
        if (txtNamaPromo.getText().trim().isEmpty()) {
            tampilkanPesan("Nama promo tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtNilaiDiskon.getText().trim());
        } catch (NumberFormatException e) {
            tampilkanPesan("Nilai diskon harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtTanggalMulai.getText().trim().isEmpty()) {
            tampilkanPesan("Tanggal mulai tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtTanggalAkhir.getText().trim().isEmpty()) {
            tampilkanPesan("Tanggal akhir tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void muatDataContoh() {
        daftarPromo.add(new Promo("Diskon Akhir Tahun", "persen", 15, "2025-12-01", "2025-12-31", true));
        daftarPromo.add(new Promo("Promo Kemerdekaan",  "nominal", 10000, "2025-08-01", "2025-08-31", true));
        daftarPromo.add(new Promo("Flash Sale",          "persen", 25, "2025-01-01", "2025-01-07", false));
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
        SwingUtilities.invokeLater(() -> new PromoGUI().setVisible(true));
    }
}
