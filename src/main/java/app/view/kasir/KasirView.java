package app.view.kasir;

import app.controller.KasirController;
import app.model.ItemCart;
import app.model.Produk;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KasirView extends JFrame {
    private KasirController controller;
    
    private JTable tblProduk;
    private JTable tblKeranjang;
    private DefaultTableModel modelProduk;
    private DefaultTableModel modelKeranjang;
    
    private JLabel lblTotal;
    private JTextField txtUangDiterima;
    private JComboBox<String> cbPayment;
    private JButton btnBayar;

    public KasirView() {
        setTitle("JAWARA POS - Kasir");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    public void setController(KasirController controller) {
        this.controller = controller;
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel Kiri: Daftar Produk
        modelProduk = new DefaultTableModel(new String[]{"ID", "Nama Produk", "Harga", "Stok"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblProduk = new JTable(modelProduk);
        JPanel pnlProduk = new JPanel(new BorderLayout());
        pnlProduk.setBorder(BorderFactory.createTitledBorder("Pilih Produk"));
        pnlProduk.add(new JScrollPane(tblProduk), BorderLayout.CENTER);
        
        JButton btnAddCart = new JButton("Tambah ke Keranjang");
        btnAddCart.setBackground(new Color(79, 70, 229)); // Primary color dari spec
        btnAddCart.setForeground(Color.WHITE);
        btnAddCart.addActionListener(e -> tambahKeKeranjang());
        pnlProduk.add(btnAddCart, BorderLayout.SOUTH);

        // Panel Kanan: Keranjang & Pembayaran
        JPanel pnlKanan = new JPanel(new BorderLayout(5, 5));
        
        modelKeranjang = new DefaultTableModel(new String[]{"Nama", "Qty", "Harga", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblKeranjang = new JTable(modelKeranjang);
        JScrollPane scrollKeranjang = new JScrollPane(tblKeranjang);
        scrollKeranjang.setBorder(BorderFactory.createTitledBorder("Keranjang"));
        pnlKanan.add(scrollKeranjang, BorderLayout.CENTER);

        JPanel pnlBayar = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlBayar.setBorder(BorderFactory.createTitledBorder("Pembayaran"));
        
        pnlBayar.add(new JLabel("Total Belanja:"));
        lblTotal = new JLabel("Rp0.0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(new Color(239, 68, 68)); // Danger color
        pnlBayar.add(lblTotal);

        pnlBayar.add(new JLabel("Uang Diterima:"));
        txtUangDiterima = new JTextField();
        pnlBayar.add(txtUangDiterima);

        pnlBayar.add(new JLabel("Metode:"));
        cbPayment = new JComboBox<>(new String[]{"Tunai", "Transfer", "QRIS"});
        pnlBayar.add(cbPayment);

        btnBayar = new JButton("PROSES PEMBAYARAN");
        btnBayar.setBackground(new Color(34, 197, 94)); // Success color
        btnBayar.setForeground(Color.WHITE);
        btnBayar.addActionListener(e -> prosesPembayaran());
        pnlBayar.add(new JLabel()); // empty space
        pnlBayar.add(btnBayar);

        pnlKanan.add(pnlBayar, BorderLayout.SOUTH);

        // Splitting View
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlProduk, pnlKanan);
        splitPane.setResizeWeight(0.5);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private List<Produk> currentProdukList;

    public void showProduk(List<Produk> list) {
        this.currentProdukList = list;
        modelProduk.setRowCount(0);
        for (Produk p : list) {
            modelProduk.addRow(new Object[]{p.getId(), p.getNamaProduk(), p.getHargaJual(), p.getStok()});
        }
    }

    private void tambahKeKeranjang() {
        int row = tblProduk.getSelectedRow();
        if (row < 0) {
            showError("Pilih produk dari tabel terlebih dahulu!");
            return;
        }
        String inputQty = JOptionPane.showInputDialog(this, "Masukkan jumlah (Qty):", "1");
        if (inputQty != null && !inputQty.trim().isEmpty()) {
            try {
                int qty = Integer.parseInt(inputQty);
                if (qty <= 0) throw new NumberFormatException();
                Produk p = currentProdukList.get(row);
                controller.tambahKeKeranjang(p, qty);
            } catch (NumberFormatException ex) {
                showError("Kuantitas harus berupa angka positif!");
            }
        }
    }

    public void updateKeranjang(List<ItemCart> keranjang) {
        modelKeranjang.setRowCount(0);
        for (ItemCart item : keranjang) {
            modelKeranjang.addRow(new Object[]{
                item.getProduk().getNamaProduk(),
                item.getQty(),
                item.getHargaSatuan(),
                item.getSubtotal()
            });
        }
    }

    public void setTotalBelanja(double total) {
        lblTotal.setText("Rp" + total);
    }

    public void resetFormBayar() {
        txtUangDiterima.setText("");
        cbPayment.setSelectedIndex(0);
    }

    private void prosesPembayaran() {
        try {
            double uang = Double.parseDouble(txtUangDiterima.getText());
            String method = cbPayment.getSelectedItem().toString();
            controller.prosesPembayaran(uang, method);
        } catch (NumberFormatException ex) {
            showError("Input uang tidak valid!");
        }
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
