package app.view.kasir;

import app.controller.KasirController;
import app.model.ItemCart;
import app.model.Produk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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

    // Premium Color Palette
    private static final Color COLOR_BG = new Color(248, 250, 252); // Slate light bg
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_PRIMARY = new Color(79, 70, 229); // Indigo
    private static final Color COLOR_PRIMARY_HOVER = new Color(67, 56, 202);
    private static final Color COLOR_SUCCESS = new Color(16, 185, 129); // Emerald
    private static final Color COLOR_SUCCESS_HOVER = new Color(5, 150, 105);
    private static final Color COLOR_DANGER = new Color(239, 68, 68); // Red
    private static final Color COLOR_TEXT_DARK = new Color(15, 23, 42);
    private static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);
    private static final Color COLOR_BORDER = new Color(226, 232, 240);

    public KasirView() {
        setTitle("JAWARA POS - Kasir");
        setSize(950, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    public void setController(KasirController controller) {
        this.controller = controller;
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Top Header Panel (App Title & Logout)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel("JAWARA POS - POS Kasir");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JButton btnLogout = new JButton("Keluar");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(COLOR_DANGER);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(80, 32));
        btnLogout.addActionListener(e -> logout());
        headerPanel.add(btnLogout, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 2. Left Panel: Product List
        JPanel pnlProduk = new JPanel(new BorderLayout(0, 10));
        pnlProduk.setBackground(COLOR_BG);
        
        JLabel lblProdTitle = new JLabel("Pilih Produk");
        lblProdTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblProdTitle.setForeground(COLOR_TEXT_DARK);
        pnlProduk.add(lblProdTitle, BorderLayout.NORTH);

        modelProduk = new DefaultTableModel(new String[]{"ID", "Nama Produk", "Harga", "Stok"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblProduk = new JTable(modelProduk);
        tblProduk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblProduk.setForeground(COLOR_TEXT_DARK);
        tblProduk.setSelectionForeground(COLOR_TEXT_DARK);
        tblProduk.setSelectionBackground(new Color(238, 242, 255));
        tblProduk.setGridColor(new Color(241, 245, 249));
        tblProduk.setRowHeight(28);
        tblProduk.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblProduk.getTableHeader().setBackground(COLOR_BORDER);
        tblProduk.getTableHeader().setForeground(COLOR_TEXT_DARK);

        JScrollPane scrollProduk = new JScrollPane(tblProduk);
        scrollProduk.setBorder(new LineBorder(COLOR_BORDER));
        pnlProduk.add(scrollProduk, BorderLayout.CENTER);
        
        JButton btnAddCart = new JButton("Tambah ke Keranjang");
        btnAddCart.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAddCart.setBackground(COLOR_PRIMARY); 
        btnAddCart.setForeground(Color.WHITE);
        btnAddCart.setBorderPainted(false);
        btnAddCart.setFocusPainted(false);
        btnAddCart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAddCart.setPreferredSize(new Dimension(200, 38));
        btnAddCart.addActionListener(e -> tambahKeKeranjang());
        
        btnAddCart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddCart.setBackground(COLOR_PRIMARY_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddCart.setBackground(COLOR_PRIMARY);
            }
        });
        pnlProduk.add(btnAddCart, BorderLayout.SOUTH);

        // 3. Right Panel: Cart & Payment
        JPanel pnlKanan = new JPanel(new BorderLayout(0, 15));
        pnlKanan.setBackground(COLOR_BG);

        // Cart Scroll Pane
        JPanel pnlCartHeader = new JPanel(new BorderLayout());
        pnlCartHeader.setOpaque(false);
        JLabel lblCartTitle = new JLabel("Keranjang Belanja");
        lblCartTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCartTitle.setForeground(COLOR_TEXT_DARK);
        pnlCartHeader.add(lblCartTitle, BorderLayout.WEST);
        
        JPanel pnlCartWrapper = new JPanel(new BorderLayout(0, 10));
        pnlCartWrapper.setBackground(COLOR_BG);
        pnlCartWrapper.add(pnlCartHeader, BorderLayout.NORTH);

        modelKeranjang = new DefaultTableModel(new String[]{"Nama", "Qty", "Harga", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblKeranjang = new JTable(modelKeranjang);
        tblKeranjang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblKeranjang.setForeground(COLOR_TEXT_DARK);
        tblKeranjang.setSelectionForeground(COLOR_TEXT_DARK);
        tblKeranjang.setSelectionBackground(new Color(238, 242, 255));
        tblKeranjang.setGridColor(new Color(241, 245, 249));
        tblKeranjang.setRowHeight(28);
        tblKeranjang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblKeranjang.getTableHeader().setBackground(COLOR_BORDER);
        tblKeranjang.getTableHeader().setForeground(COLOR_TEXT_DARK);

        JScrollPane scrollKeranjang = new JScrollPane(tblKeranjang);
        scrollKeranjang.setBorder(new LineBorder(COLOR_BORDER));
        pnlCartWrapper.add(scrollKeranjang, BorderLayout.CENTER);
        pnlKanan.add(pnlCartWrapper, BorderLayout.CENTER);

        // Payment Panel Card
        JPanel pnlBayarCard = new JPanel(new BorderLayout(10, 10));
        pnlBayarCard.setBackground(COLOR_CARD);
        pnlBayarCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JPanel pnlFormBayar = new JPanel(new GridBagLayout());
        pnlFormBayar.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);

        // Row 1: Total
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        JLabel lblTotalBelanjaTitle = new JLabel("Total Belanja:");
        lblTotalBelanjaTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTotalBelanjaTitle.setForeground(COLOR_TEXT_DARK);
        pnlFormBayar.add(lblTotalBelanjaTitle, gbc);

        gbc.gridx = 1; gbc.weightx = 0.6;
        lblTotal = new JLabel("Rp 0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(COLOR_DANGER);
        pnlFormBayar.add(lblTotal, gbc);

        // Row 2: Uang Diterima
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4;
        JLabel lblUangTitle = new JLabel("Uang Diterima:");
        lblUangTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUangTitle.setForeground(COLOR_TEXT_DARK);
        pnlFormBayar.add(lblUangTitle, gbc);

        gbc.gridx = 1; gbc.weightx = 0.6;
        txtUangDiterima = new JTextField();
        txtUangDiterima.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtUangDiterima.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        pnlFormBayar.add(txtUangDiterima, gbc);

        // Row 3: Metode
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.4;
        JLabel lblMetodeTitle = new JLabel("Metode:");
        lblMetodeTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMetodeTitle.setForeground(COLOR_TEXT_DARK);
        pnlFormBayar.add(lblMetodeTitle, gbc);

        gbc.gridx = 1; gbc.weightx = 0.6;
        cbPayment = new JComboBox<>(new String[]{"Tunai", "Transfer", "QRIS"});
        cbPayment.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnlFormBayar.add(cbPayment, gbc);

        // Row 4: Process Button
        btnBayar = new JButton("PROSES PEMBAYARAN");
        btnBayar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBayar.setBackground(COLOR_SUCCESS);
        btnBayar.setForeground(Color.WHITE);
        btnBayar.setBorderPainted(false);
        btnBayar.setFocusPainted(false);
        btnBayar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBayar.setPreferredSize(new Dimension(200, 36));
        btnBayar.addActionListener(e -> prosesPembayaran());
        btnBayar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBayar.setBackground(COLOR_SUCCESS_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBayar.setBackground(COLOR_SUCCESS);
            }
        });

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 0, 0, 0);
        pnlFormBayar.add(btnBayar, gbc);

        pnlBayarCard.add(pnlFormBayar, BorderLayout.CENTER);
        pnlKanan.add(pnlBayarCard, BorderLayout.SOUTH);

        // 4. Split Pane Setup
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnlProduk, pnlKanan);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(6);
        splitPane.setBorder(null);
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
        lblTotal.setText("Rp " + String.format("%,.0f", total));
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
        JOptionPane.showMessageDialog(this, msg, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        dispose();
        app.service.AuthService authService = new app.service.AuthService();
        app.view.auth.LoginView loginView = new app.view.auth.LoginView();
        app.controller.LoginController loginController = new app.controller.LoginController(loginView, authService);
        loginController.start();
    }
}
