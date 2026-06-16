package app.view.kasir;

import app.model.ItemCart;
import app.model.Produk;
import app.model.Voucher;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI Keranjang Belanja (ItemCart) untuk Kasir.
 *
 * CARA PENGGUNAAN:
 * ──────────────────────────────────────────────────────
 * 1. TAMBAH PRODUK  → Pilih produk dari dropdown → klik "➕ Tambah ke Keranjang"
 * 2. TAMBAH QTY     → Klik baris di keranjang → klik "▲ Tambah"
 * 3. KURANGI QTY    → Klik baris di keranjang → klik "▼ Kurangi"
 * 4. HAPUS ITEM     → Klik baris di keranjang → klik "🗑 Hapus Item"
 * 5. PAKAI VOUCHER  → Ketik kode voucher → klik "Terapkan"
 * 6. BAYAR          → Pilih metode bayar → klik "💳 Proses Pembayaran"
 * 7. BATAL SEMUA    → Klik "🗑 Kosongkan Keranjang"
 * ──────────────────────────────────────────────────────
 */
public class ItemCartGUI extends JFrame {

    // ── Komponen utama ───────────────────────────────────────────────────────
    private JComboBox<Produk>  cmbProduk;
    private JSpinner           spnQty;
    private JTable             tabelKeranjang;
    private DefaultTableModel  modelTabel;

    // ── Voucher & pembayaran ─────────────────────────────────────────────────
    private JTextField         txtKodeVoucher;
    private JLabel             lblDiskon, lblTotal, lblSubtotal;
    private JComboBox<String>  cmbMetodeBayar;
    private JLabel             lblStatusBayar;

    // ── Data ─────────────────────────────────────────────────────────────────
    private List<ItemCart>  keranjang     = new ArrayList<>();
    private List<Produk>    daftarProduk  = new ArrayList<>();
    private List<Voucher>   daftarVoucher = new ArrayList<>();
    private int             diskonAktif   = 0;
    private int             selectedRow   = -1;

    // ── Tema warna ───────────────────────────────────────────────────────────
    private static final Color BIRU      = new Color(41,  128, 185);
    private static final Color HIJAU     = new Color(39,  174,  96);
    private static final Color MERAH     = new Color(192,  57,  43);
    private static final Color ORANYE    = new Color(230, 126,  34);
    private static final Color ABU       = new Color(149, 165, 166);
    private static final Color BG        = new Color(245, 246, 250);
    private static final Color PUTIH     = Color.WHITE;
    private static final Color HEADER_BG = new Color(44,  62,  80);

    public ItemCartGUI() {
        setTitle("Keranjang Belanja — JAWARA POS");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        muatDataContoh();
        initKomponen();
        refreshTabel();
        refreshRingkasan();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // INISIALISASI
    // ─────────────────────────────────────────────────────────────────────────
    private void initKomponen() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        root.add(buatHeader(),       BorderLayout.NORTH);
        root.add(buatPanelTengah(),  BorderLayout.CENTER);
        root.add(buatPanelBawah(),   BorderLayout.SOUTH);

        add(root);
    }

    // ── HEADER ────────────────────────────────────────────────────────────────
    private JPanel buatHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(HEADER_BG);
        p.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel judul = new JLabel("🛒  Keranjang Belanja");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        judul.setForeground(PUTIH);

        JLabel sub = new JLabel("Pilih produk → tambahkan → proses pembayaran");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(180, 200, 220));

        p.add(judul, BorderLayout.WEST);
        p.add(sub,   BorderLayout.EAST);
        return p;
    }

    // ── PANEL TENGAH (kiri: pilih produk + tabel | kanan: ringkasan) ──────────
    private JPanel buatPanelTengah() {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(BG);
        p.add(buatPanelKiri(), BorderLayout.CENTER);
        p.add(buatPanelKanan(), BorderLayout.EAST);
        return p;
    }

    // ── PANEL KIRI ────────────────────────────────────────────────────────────
    private JPanel buatPanelKiri() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(BG);

        // ── Baris pilih produk ──
        JPanel barisPilih = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        barisPilih.setBackground(PUTIH);
        barisPilih.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));

        cmbProduk = new JComboBox<>(daftarProduk.toArray(new Produk[0]));
        cmbProduk.setPreferredSize(new Dimension(260, 30));
        cmbProduk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbProduk.setToolTipText("Pilih produk yang ingin ditambahkan ke keranjang");
        cmbProduk.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object val,
                    int idx, boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, val, idx, sel, focus);
                if (val instanceof Produk pd) {
                    setText(pd.getNamaProduk()
                            + "  —  Rp " + (int) pd.getHargaJual()
                            + "  (stok: " + pd.getStok() + ")");
                }
                return this;
            }
        });

        spnQty = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spnQty.setPreferredSize(new Dimension(65, 30));
        spnQty.setToolTipText("Jumlah yang ingin dibeli");

        JButton btnTambah = buatTombol("➕ Tambah ke Keranjang", HIJAU);
        btnTambah.setToolTipText("Klik untuk memasukkan produk ke keranjang");
        btnTambah.addActionListener(e -> tambahKeKeranjang());

        barisPilih.add(new JLabel("Produk:"));
        barisPilih.add(cmbProduk);
        barisPilih.add(new JLabel("  Qty:"));
        barisPilih.add(spnQty);
        barisPilih.add(btnTambah);

        // ── Tabel keranjang ──
        String[] kolom = {"#", "Nama Produk", "Harga Satuan", "Qty", "Subtotal"};
        modelTabel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelKeranjang = new JTable(modelTabel);
        tabelKeranjang.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelKeranjang.setRowHeight(30);
        tabelKeranjang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelKeranjang.getTableHeader().setBackground(HEADER_BG);
        tabelKeranjang.getTableHeader().setForeground(PUTIH);
        tabelKeranjang.setSelectionBackground(new Color(189, 215, 238));
        tabelKeranjang.setGridColor(new Color(230, 230, 230));
        tabelKeranjang.setShowVerticalLines(false);

        // Lebar kolom
        int[] lebar = {35, 260, 120, 60, 130};
        for (int i = 0; i < lebar.length; i++)
            tabelKeranjang.getColumnModel().getColumn(i).setPreferredWidth(lebar[i]);

        tabelKeranjang.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                selectedRow = tabelKeranjang.getSelectedRow();
            }
        });

        // ── Tombol aksi baris ──
        JPanel tombolBaris = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        tombolBaris.setBackground(BG);

        JButton btnPlus  = buatTombol("▲ Tambah Qty",  BIRU);
        JButton btnMinus = buatTombol("▼ Kurangi Qty", ORANYE);
        JButton btnHapus = buatTombol("🗑 Hapus Item",  MERAH);

        btnPlus .setToolTipText("Pilih baris → klik ini untuk menambah jumlah item");
        btnMinus.setToolTipText("Pilih baris → klik ini untuk mengurangi jumlah item");
        btnHapus.setToolTipText("Pilih baris → klik ini untuk menghapus item dari keranjang");

        btnPlus .addActionListener(e -> ubahQty(+1));
        btnMinus.addActionListener(e -> ubahQty(-1));
        btnHapus.addActionListener(e -> hapusItem());

        tombolBaris.add(btnPlus);
        tombolBaris.add(btnMinus);
        tombolBaris.add(btnHapus);

        JScrollPane scroll = new JScrollPane(tabelKeranjang);
        scroll.setBorder(new LineBorder(new Color(220, 220, 220)));

        p.add(barisPilih,  BorderLayout.NORTH);
        p.add(scroll,      BorderLayout.CENTER);
        p.add(tombolBaris, BorderLayout.SOUTH);
        return p;
    }

    // ── PANEL KANAN (ringkasan + voucher + bayar) ─────────────────────────────
    private JPanel buatPanelKanan() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(BG);
        p.setPreferredSize(new Dimension(260, 0));

        // ── Kartu ringkasan harga ──
        JPanel kartuHarga = new JPanel(new GridBagLayout());
        kartuHarga.setBackground(PUTIH);
        kartuHarga.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(4, 2, 4, 2);

        lblSubtotal = buatLabelHarga("Rp 0");
        lblDiskon   = buatLabelHarga("Rp 0");
        lblDiskon.setForeground(MERAH);
        lblTotal    = buatLabelHarga("Rp 0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(HIJAU);

        String[] namaLabel = {"Subtotal:", "Diskon:", "─────────────", "TOTAL BAYAR:"};
        JLabel[] nilaiLabel = {lblSubtotal, lblDiskon, new JLabel(""), lblTotal};

        for (int i = 0; i < namaLabel.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0.5;
            JLabel lbl = new JLabel(namaLabel[i]);
            lbl.setFont(new Font("Segoe UI", i == 3 ? Font.BOLD : Font.PLAIN, 13));
            kartuHarga.add(lbl, g);
            g.gridx = 1;
            kartuHarga.add(nilaiLabel[i], g);
        }

        // ── Kartu voucher ──
        JPanel kartuVoucher = new JPanel(new GridBagLayout());
        kartuVoucher.setBackground(PUTIH);
        kartuVoucher.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        GridBagConstraints gv = new GridBagConstraints();
        gv.fill = GridBagConstraints.HORIZONTAL;
        gv.insets = new Insets(4, 2, 4, 2);

        JLabel judulVoucher = new JLabel("🎟 Kode Voucher");
        judulVoucher.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gv.gridx = 0; gv.gridy = 0; gv.gridwidth = 2;
        kartuVoucher.add(judulVoucher, gv);

        txtKodeVoucher = new JTextField();
        txtKodeVoucher.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtKodeVoucher.setToolTipText("Ketik kode voucher, contoh: HEMAT10");
        gv.gridy = 1; gv.gridwidth = 2;
        kartuVoucher.add(txtKodeVoucher, gv);

        JButton btnVoucher  = buatTombol("Terapkan",    new Color(142, 68, 173));
        JButton btnHapusVou = buatTombol("Hapus Kode",  ABU);
        btnVoucher .setToolTipText("Klik untuk menerapkan kode voucher");
        btnHapusVou.setToolTipText("Klik untuk membatalkan voucher");
        btnVoucher .addActionListener(e -> terapkanVoucher());
        btnHapusVou.addActionListener(e -> hapusVoucher());

        gv.gridy = 2; gv.gridwidth = 1; gv.gridx = 0; gv.weightx = 0.5;
        kartuVoucher.add(btnVoucher, gv);
        gv.gridx = 1;
        kartuVoucher.add(btnHapusVou, gv);

        // ── Kartu metode bayar ──
        JPanel kartuBayar = new JPanel(new GridBagLayout());
        kartuBayar.setBackground(PUTIH);
        kartuBayar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        GridBagConstraints gb = new GridBagConstraints();
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.insets = new Insets(5, 2, 5, 2);

        JLabel judulBayar = new JLabel("💳 Metode Pembayaran");
        judulBayar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gb.gridx = 0; gb.gridy = 0; gb.gridwidth = 1;
        kartuBayar.add(judulBayar, gb);

        cmbMetodeBayar = new JComboBox<>(new String[]{"Tunai", "Transfer Bank", "QRIS", "Kartu Debit"});
        cmbMetodeBayar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbMetodeBayar.setToolTipText("Pilih cara pembayaran");
        gb.gridy = 1;
        kartuBayar.add(cmbMetodeBayar, gb);

        JButton btnBayar = buatTombol("💳 Proses Pembayaran", HIJAU);
        btnBayar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBayar.setToolTipText("Klik untuk memproses transaksi");
        btnBayar.addActionListener(e -> prosesPembayaran());
        gb.gridy = 2;
        kartuBayar.add(btnBayar, gb);

        lblStatusBayar = new JLabel(" ");
        lblStatusBayar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatusBayar.setHorizontalAlignment(SwingConstants.CENTER);
        gb.gridy = 3;
        kartuBayar.add(lblStatusBayar, gb);

        p.add(kartuHarga,   BorderLayout.NORTH);
        JPanel mid = new JPanel(new GridLayout(2, 1, 0, 10));
        mid.setBackground(BG);
        mid.add(kartuVoucher);
        mid.add(kartuBayar);
        p.add(mid, BorderLayout.CENTER);
        return p;
    }

    // ── PANEL BAWAH (kosongkan) ────────────────────────────────────────────────
    private JPanel buatPanelBawah() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        p.setBackground(BG);

        JButton btnKosong = buatTombol("🗑 Kosongkan Keranjang", MERAH);
        btnKosong.setToolTipText("Hapus semua item dari keranjang");
        btnKosong.addActionListener(e -> kosongkanKeranjang());
        p.add(btnKosong);
        return p;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOGIKA AKSI
    // ─────────────────────────────────────────────────────────────────────────
    private void tambahKeKeranjang() {
        Produk p = (Produk) cmbProduk.getSelectedItem();
        if (p == null) return;

        int qty = (int) spnQty.getValue();

        // Cek stok
        if (qty > p.getStok()) {
            JOptionPane.showMessageDialog(this,
                "Stok tidak cukup! Stok tersedia: " + p.getStok(),
                "Stok Habis", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Cek kalau produk sudah ada di keranjang → tambah qty saja
        for (ItemCart item : keranjang) {
            if (item.getProduk().getId() == p.getId()) {
                item.tambahKuantitas();
                refreshTabel();
                refreshRingkasan();
                return;
            }
        }

        keranjang.add(new ItemCart(p, qty));
        refreshTabel();
        refreshRingkasan();
    }

    private void ubahQty(int delta) {
        if (selectedRow < 0 || selectedRow >= keranjang.size()) {
            JOptionPane.showMessageDialog(this,
                "Pilih baris item di keranjang terlebih dahulu.",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ItemCart item = keranjang.get(selectedRow);
        if (delta > 0) {
            if (item.getKuantitas() >= item.getProduk().getStok()) {
                JOptionPane.showMessageDialog(this, "Stok maksimal tercapai!", "Stok", JOptionPane.WARNING_MESSAGE);
                return;
            }
            item.tambahKuantitas();
        } else {
            if (item.getKuantitas() <= 1) {
                // Tanya apakah mau hapus
                int ok = JOptionPane.showConfirmDialog(this,
                    "Kuantitas sudah 1. Hapus item ini?", "Hapus Item",
                    JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) keranjang.remove(selectedRow);
            } else {
                item.kurangiKuantitas(1);
            }
        }
        refreshTabel();
        refreshRingkasan();
    }

    private void hapusItem() {
        if (selectedRow < 0 || selectedRow >= keranjang.size()) {
            JOptionPane.showMessageDialog(this,
                "Pilih baris item di keranjang terlebih dahulu.",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        keranjang.remove(selectedRow);
        selectedRow = -1;
        refreshTabel();
        refreshRingkasan();
    }

    private void terapkanVoucher() {
        String kode = txtKodeVoucher.getText().trim();
        if (kode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan kode voucher terlebih dahulu.",
                "Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int subtotal = hitungSubtotal();
        for (Voucher v : daftarVoucher) {
            if (v.getKodeVoucher().equalsIgnoreCase(kode)) {
                if (!v.isValid()) {
                    JOptionPane.showMessageDialog(this, "Voucher sudah tidak aktif!",
                        "Tidak Valid", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                diskonAktif = v.hitungDiskon(subtotal);
                JOptionPane.showMessageDialog(this,
                    "Voucher berhasil! Diskon: Rp " + diskonAktif,
                    "Voucher Diterapkan", JOptionPane.INFORMATION_MESSAGE);
                refreshRingkasan();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Kode voucher tidak ditemukan.",
            "Tidak Ditemukan", JOptionPane.ERROR_MESSAGE);
    }

    private void hapusVoucher() {
        diskonAktif = 0;
        txtKodeVoucher.setText("");
        refreshRingkasan();
    }

    private void prosesPembayaran() {
        if (keranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!",
                "Keranjang Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int total  = hitungSubtotal() - diskonAktif;
        String met = (String) cmbMetodeBayar.getSelectedItem();

        int ok = JOptionPane.showConfirmDialog(this,
            "Proses pembayaran Rp " + total + " via " + met + "?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (ok == JOptionPane.YES_OPTION) {
            lblStatusBayar.setText("✅ Pembayaran berhasil!");
            lblStatusBayar.setForeground(HIJAU);
            // Reset keranjang setelah bayar
            keranjang.clear();
            diskonAktif = 0;
            txtKodeVoucher.setText("");
            refreshTabel();
            refreshRingkasan();
        }
    }

    private void kosongkanKeranjang() {
        if (keranjang.isEmpty()) return;
        int ok = JOptionPane.showConfirmDialog(this,
            "Yakin ingin mengosongkan semua item di keranjang?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            keranjang.clear();
            diskonAktif = 0;
            txtKodeVoucher.setText("");
            selectedRow = -1;
            refreshTabel();
            refreshRingkasan();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPER REFRESH
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshTabel() {
        modelTabel.setRowCount(0);
        int no = 1;
        for (ItemCart item : keranjang) {
            modelTabel.addRow(new Object[]{
                no++,
                item.getProduk().getNamaProduk(),
                "Rp " + item.getHargaSatuan(),
                item.getKuantitas(),
                "Rp " + item.hitungSubTotal()
            });
        }
        tabelKeranjang.clearSelection();
        selectedRow = -1;
    }

    private void refreshRingkasan() {
        int sub   = hitungSubtotal();
        int total = sub - diskonAktif;

        lblSubtotal.setText("Rp " + sub);
        lblDiskon  .setText("- Rp " + diskonAktif);
        lblTotal   .setText("Rp " + total);
    }

    private int hitungSubtotal() {
        return keranjang.stream().mapToInt(ItemCart::hitungSubTotal).sum();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DATA CONTOH
    // ─────────────────────────────────────────────────────────────────────────
    private void muatDataContoh() {
        daftarProduk.add(new Produk(1, "Kopi Hitam",    5000, 12000, 50, "PRD001", 1));
        daftarProduk.add(new Produk(2, "Es Teh Manis",  3000,  8000, 80, "PRD002", 1));
        daftarProduk.add(new Produk(3, "Roti Bakar",    8000, 18000, 30, "PRD003", 2));
        daftarProduk.add(new Produk(4, "Nasi Goreng",  12000, 25000, 20, "PRD004", 2));
        daftarProduk.add(new Produk(5, "Air Mineral",   2000,  5000, 100,"PRD005", 1));

        daftarVoucher.add(new Voucher(1, "HEMAT10",  "persen",   10, "2025-12-31", true));
        daftarVoucher.add(new Voucher(2, "DISKON5K", "nominal", 5000,"2025-06-30", true));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // FACTORY METHODS
    // ─────────────────────────────────────────────────────────────────────────
    private JLabel buatLabelHarga(String teks) {
        JLabel l = new JLabel(teks, SwingConstants.RIGHT);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private JButton buatTombol(String teks, Color warna) {
        JButton btn = new JButton(teks);
        btn.setBackground(warna);
        btn.setForeground(PUTIH);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ItemCartGUI().setVisible(true));
    }
}