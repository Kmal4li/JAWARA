package app.view.admin;

import app.controller.AdminController;
import app.model.CategoryProduct;
import app.model.Produk;
import app.model.User;
import app.model.Transaksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdminDashboardView extends JFrame {
    private AdminController controller;

    // Theme Colors
    private static final Color COLOR_PRIMARY = new Color(79, 70, 229); // Indigo
    private static final Color COLOR_SECONDARY = new Color(124, 58, 237); // Purple
    private static final Color COLOR_BG = new Color(248, 250, 252); // Slate light bg
    private static final Color COLOR_SIDEBAR = new Color(15, 23, 42); // Slate dark sidebar
    private static final Color COLOR_TEXT_DARK = new Color(15, 23, 42);
    private static final Color COLOR_TEXT_LIGHT = Color.WHITE;
    private static final Color COLOR_SUCCESS = new Color(16, 185, 129); // Emerald
    private static final Color COLOR_DANGER = new Color(239, 68, 68); // Red
    private static final Color COLOR_WARNING = new Color(245, 158, 11); // Amber
    private static final Color COLOR_BORDER = new Color(226, 232, 240);

    // Sidebar navigation buttons
    private JButton btnNavOverview;
    private JButton btnNavProduk;
    private JButton btnNavKategori;
    private JButton btnNavUser;
    private JButton btnNavTransaksi;
    private JButton btnNavVoucher;
    private JButton btnNavPembayaran;
    private JButton btnNavLogout;

    // Card Layout for main content panel
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    // --- Overview Panel Components ---
    private JLabel lblTotalPenjualanVal;
    private JLabel lblTotalProdukVal;
    private JLabel lblTotalUserVal;
    private JLabel lblTotalTransaksiVal;

    // --- Product Panel Components ---
    private JTable tblProduk;
    private DefaultTableModel modelProduk;
    private JTextField txtProdNama;
    private JTextField txtProdHargaBeli;
    private JTextField txtProdHargaJual;
    private JTextField txtProdStok;
    private JTextField txtProdBarcode;
    private JComboBox<CategoryProduct> cbProdKategori;
    private JButton btnProdTambah;
    private JButton btnProdUpdate;
    private JButton btnProdHapus;
    private JButton btnProdBatal;

    // --- Category Panel Components ---
    private JTable tblKategori;
    private DefaultTableModel modelKategori;
    private JTextField txtKatNama;
    private JTextArea txtKatDeskripsi;
    private JButton btnKatTambah;
    private JButton btnKatUpdate;
    private JButton btnKatHapus;
    private JButton btnKatBatal;

    // --- User Panel Components ---
    private JTable tblUser;
    private DefaultTableModel modelUser;
    private JTextField txtUserNama;
    private JTextField txtUserUsername;
    private JPasswordField txtUserPassword;
    private JComboBox<String> cbUserRole;
    private JButton btnUserTambah;
    private JButton btnUserUpdate;
    private JButton btnUserHapus;
    private JButton btnUserBatal;

    // --- Transaction Panel Components ---
    private JTable tblTransaksi;
    private DefaultTableModel modelTransaksi;

    public AdminDashboardView(String adminName) {
        setTitle("JAWARA POS - Admin Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents(adminName);
        styleWindow();
    }

    public void setController(AdminController controller) {
        this.controller = controller;
    }

    private void styleWindow() {
        try {
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
        } catch (Exception e) {
            // Ignore
        }
    }

    private void initComponents(String adminName) {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(COLOR_BG);

        // 1. Sidebar Panel (Left)
        JPanel sidebarPanel = buatSidebar(adminName);
        containerPanel.add(sidebarPanel, BorderLayout.WEST);

        // 2. Main Content Panel (Center)
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(COLOR_BG);

        // Add panels to card layout
        mainContentPanel.add(buatPanelOverview(), "Overview");
        mainContentPanel.add(buatPanelProduk(), "Produk");
        mainContentPanel.add(buatPanelKategori(), "Kategori");
        mainContentPanel.add(buatPanelUser(), "User");
        mainContentPanel.add(buatPanelTransaksi(), "Transaksi");

        containerPanel.add(mainContentPanel, BorderLayout.CENTER);
        add(containerPanel);

        // Add listeners for Navigation
        btnNavOverview.addActionListener(e -> switchCard("Overview"));
        btnNavProduk.addActionListener(e -> {
            switchCard("Produk");
            if (controller != null) controller.refreshProdukTable();
        });
        btnNavKategori.addActionListener(e -> {
            switchCard("Kategori");
            if (controller != null) controller.refreshKategoriTable();
        });
        btnNavUser.addActionListener(e -> {
            switchCard("User");
            if (controller != null) controller.refreshUserTable();
        });
        btnNavTransaksi.addActionListener(e -> {
            switchCard("Transaksi");
            if (controller != null) controller.refreshTransaksiTable();
        });
        btnNavVoucher.addActionListener(e -> {
            if (controller != null) controller.openVoucherGUI();
        });
        btnNavPembayaran.addActionListener(e -> {
            if (controller != null) controller.openMetodePembayaranGUI();
        });
        btnNavLogout.addActionListener(e -> {
            if (controller != null) controller.logout();
        });
    }

    private JPanel buatSidebar(String adminName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 750));
        panel.setBackground(COLOR_SIDEBAR);
        panel.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Header (Logo/App Title)
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        JLabel lblLogo = new JLabel("JAWARA POS");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(COLOR_TEXT_LIGHT);
        JLabel lblAdminName = new JLabel("Halo, " + adminName);
        lblAdminName.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblAdminName.setForeground(new Color(148, 163, 184)); // Slate light gray
        headerPanel.add(lblLogo);
        headerPanel.add(lblAdminName);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Menu items
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        btnNavOverview = buatSidebarButton("Ringkasan");
        btnNavProduk = buatSidebarButton("Kelola Produk");
        btnNavKategori = buatSidebarButton("Kelola Kategori");
        btnNavUser = buatSidebarButton("Kelola Pengguna");
        btnNavTransaksi = buatSidebarButton("Riwayat Transaksi");
        btnNavVoucher = buatSidebarButton("Kelola Voucher");
        btnNavPembayaran = buatSidebarButton("Metode Pembayaran");

        menuPanel.add(btnNavOverview);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnNavProduk);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnNavKategori);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnNavUser);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnNavTransaksi);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(btnNavVoucher);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(btnNavPembayaran);

        panel.add(menuPanel, BorderLayout.CENTER);

        // Logout
        btnNavLogout = buatSidebarButton("Keluar");
        btnNavLogout.setBackground(COLOR_DANGER);
        panel.add(btnNavLogout, BorderLayout.SOUTH);

        return panel;
    }

    private JButton buatSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setPreferredSize(new Dimension(220, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBackground(COLOR_SIDEBAR);
        btn.setForeground(new Color(203, 213, 225));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 15, 0, 15));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getBackground() != COLOR_DANGER) {
                    btn.setBackground(new Color(30, 41, 59));
                    btn.setForeground(Color.WHITE);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.getBackground() != COLOR_DANGER) {
                    btn.setBackground(COLOR_SIDEBAR);
                    btn.setForeground(new Color(203, 213, 225));
                }
            }
        });
        return btn;
    }

    private void switchCard(String cardName) {
        cardLayout.show(mainContentPanel, cardName);
        btnNavOverview.setForeground(cardName.equals("Overview") ? Color.WHITE : new Color(203, 213, 225));
        btnNavProduk.setForeground(cardName.equals("Produk") ? Color.WHITE : new Color(203, 213, 225));
        btnNavKategori.setForeground(cardName.equals("Kategori") ? Color.WHITE : new Color(203, 213, 225));
        btnNavUser.setForeground(cardName.equals("User") ? Color.WHITE : new Color(203, 213, 225));
        btnNavTransaksi.setForeground(cardName.equals("Transaksi") ? Color.WHITE : new Color(203, 213, 225));
    }

    // ==========================================
    // 1. OVERVIEW PANEL
    // ==========================================
    private JPanel buatPanelOverview() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("Ringkasan Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(COLOR_TEXT_DARK);
        titlePanel.add(title);
        panel.add(titlePanel, BorderLayout.NORTH);

        JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 20, 20));
        cardsGrid.setOpaque(false);

        lblTotalPenjualanVal = new JLabel("Rp0");
        lblTotalProdukVal = new JLabel("0");
        lblTotalUserVal = new JLabel("0");
        lblTotalTransaksiVal = new JLabel("0");

        cardsGrid.add(buatStatCard("Total Pendapatan", lblTotalPenjualanVal, COLOR_PRIMARY));
        cardsGrid.add(buatStatCard("Jumlah Produk", lblTotalProdukVal, COLOR_SECONDARY));
        cardsGrid.add(buatStatCard("Total Pengguna", lblTotalUserVal, COLOR_SUCCESS));
        cardsGrid.add(buatStatCard("Total Transaksi", lblTotalTransaksiVal, COLOR_WARNING));

        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(Color.WHITE);
        bodyPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(40, 40, 40, 40)
        ));

        JLabel lblWelcome = new JLabel("Selamat Datang di Portal Admin JAWARA POS");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblWelcome.setForeground(COLOR_TEXT_DARK);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea txtWelcomeDesc = new JTextArea(
                "Gunakan menu di samping kiri untuk mengelola master data produk, kategori produk, user kasir/admin, " +
                "serta memantau performa penjualan secara real-time.\n\n" +
                "Proyek ini dibangun mengikuti struktur Clean Architecture & Model-View-Controller (MVC) " +
                "untuk menjamin performa cepat, aman, dan mudah dimodifikasi di kemudian hari."
        );
        txtWelcomeDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtWelcomeDesc.setForeground(new Color(71, 85, 105));
        txtWelcomeDesc.setEditable(false);
        txtWelcomeDesc.setLineWrap(true);
        txtWelcomeDesc.setWrapStyleWord(true);
        txtWelcomeDesc.setOpaque(false);
        txtWelcomeDesc.setBorder(new EmptyBorder(20, 0, 0, 0));

        bodyPanel.add(lblWelcome, BorderLayout.NORTH);
        bodyPanel.add(txtWelcomeDesc, BorderLayout.CENTER);

        JPanel mainOverviewPanel = new JPanel(new BorderLayout(20, 20));
        mainOverviewPanel.setOpaque(false);
        mainOverviewPanel.add(cardsGrid, BorderLayout.NORTH);
        mainOverviewPanel.add(bodyPanel, BorderLayout.CENTER);

        panel.add(mainOverviewPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buatStatCard(String title, JLabel valLabel, Color indicatorColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel line = new JPanel();
        line.setBackground(indicatorColor);
        line.setPreferredSize(new Dimension(100, 4));
        card.add(line, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(100, 116, 139));

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valLabel.setForeground(COLOR_TEXT_DARK);

        JPanel content = new JPanel(new GridLayout(2, 1, 5, 5));
        content.setOpaque(false);
        content.add(lblTitle);
        content.add(valLabel);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    // ==========================================
    // 2. PRODUCT PANEL
    // ==========================================
    private JPanel buatPanelProduk() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Manajemen Produk");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buatFormProduk(), buatTabelProduk());
        split.setDividerLocation(380);
        split.setDividerSize(6);
        split.setBorder(null);
        panel.add(split, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buatFormProduk() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        txtProdNama = buatStyledTextField();
        txtProdHargaBeli = buatStyledTextField();
        txtProdHargaJual = buatStyledTextField();
        txtProdStok = buatStyledTextField();
        txtProdBarcode = buatStyledTextField();
        cbProdKategori = new JComboBox<>();
        cbProdKategori.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        String[] labels = {"Nama Produk*", "Harga Beli*", "Harga Jual*", "Stok*", "Barcode", "Kategori"};
        JComponent[] fields = {txtProdNama, txtProdHargaBeli, txtProdHargaJual, txtProdStok, txtProdBarcode, cbProdKategori};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.35;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(COLOR_TEXT_DARK);
            card.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.65;
            card.add(fields[i], gbc);
        }

        JLabel formTitle = new JLabel("Input / Edit Produk");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(COLOR_TEXT_DARK);

        panel.add(formTitle, BorderLayout.NORTH);
        panel.add(card, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        btnProdTambah = buatStyledButton("Tambah", COLOR_SUCCESS);
        btnProdUpdate = buatStyledButton("Update", COLOR_PRIMARY);
        btnProdHapus = buatStyledButton("Hapus", COLOR_DANGER);
        btnProdBatal = buatStyledButton("Batal", new Color(148, 163, 184));

        btnPanel.add(btnProdTambah);
        btnPanel.add(btnProdUpdate);
        btnPanel.add(btnProdHapus);
        btnPanel.add(btnProdBatal);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buatTabelProduk() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(0, 15, 0, 0));

        modelProduk = new DefaultTableModel(new String[]{"ID", "Nama Produk", "Harga Beli", "Harga Jual", "Stok", "Barcode", "Kategori"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
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

        JScrollPane scroll = new JScrollPane(tblProduk);
        scroll.setBorder(new LineBorder(COLOR_BORDER));

        JLabel tblTitle = new JLabel("Daftar Produk");
        tblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblTitle.setForeground(COLOR_TEXT_DARK);

        panel.add(tblTitle, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ==========================================
    // 3. CATEGORY PANEL
    // ==========================================
    private JPanel buatPanelKategori() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Manajemen Kategori");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buatFormKategori(), buatTabelKategori());
        split.setDividerLocation(380);
        split.setDividerSize(6);
        split.setBorder(null);
        panel.add(split, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buatFormKategori() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 5, 8, 5);

        txtKatNama = buatStyledTextField();
        txtKatDeskripsi = new JTextArea(4, 20);
        txtKatDeskripsi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtKatDeskripsi.setLineWrap(true);
        txtKatDeskripsi.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtKatDeskripsi);
        scrollDesc.setBorder(new LineBorder(new Color(203, 213, 225)));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35; gbc.weighty = 0.1;
        JLabel lblNama = new JLabel("Nama Kategori*");
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNama.setForeground(COLOR_TEXT_DARK);
        card.add(lblNama, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        card.add(txtKatNama, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35; gbc.weighty = 0.9;
        JLabel lblDesc = new JLabel("Deskripsi");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDesc.setForeground(COLOR_TEXT_DARK);
        card.add(lblDesc, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        card.add(scrollDesc, gbc);

        JLabel formTitle = new JLabel("Input / Edit Kategori");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(COLOR_TEXT_DARK);

        panel.add(formTitle, BorderLayout.NORTH);
        panel.add(card, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        btnKatTambah = buatStyledButton("Tambah", COLOR_SUCCESS);
        btnKatUpdate = buatStyledButton("Update", COLOR_PRIMARY);
        btnKatHapus = buatStyledButton("Hapus", COLOR_DANGER);
        btnKatBatal = buatStyledButton("Batal", new Color(148, 163, 184));

        btnPanel.add(btnKatTambah);
        btnPanel.add(btnKatUpdate);
        btnPanel.add(btnKatHapus);
        btnPanel.add(btnKatBatal);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buatTabelKategori() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(0, 15, 0, 0));

        modelKategori = new DefaultTableModel(new String[]{"ID", "Nama Kategori", "Deskripsi"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblKategori = new JTable(modelKategori);
        tblKategori.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblKategori.setForeground(COLOR_TEXT_DARK);
        tblKategori.setSelectionForeground(COLOR_TEXT_DARK);
        tblKategori.setSelectionBackground(new Color(238, 242, 255));
        tblKategori.setGridColor(new Color(241, 245, 249));
        tblKategori.setRowHeight(28);
        tblKategori.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblKategori.getTableHeader().setBackground(COLOR_BORDER);
        tblKategori.getTableHeader().setForeground(COLOR_TEXT_DARK);

        JScrollPane scroll = new JScrollPane(tblKategori);
        scroll.setBorder(new LineBorder(COLOR_BORDER));

        JLabel tblTitle = new JLabel("Daftar Kategori");
        tblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblTitle.setForeground(COLOR_TEXT_DARK);

        panel.add(tblTitle, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ==========================================
    // 4. USER PANEL
    // ==========================================
    private JPanel buatPanelUser() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Manajemen Pengguna");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buatFormUser(), buatTabelUser());
        split.setDividerLocation(380);
        split.setDividerSize(6);
        split.setBorder(null);
        panel.add(split, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buatFormUser() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(COLOR_BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        txtUserNama = buatStyledTextField();
        txtUserUsername = buatStyledTextField();
        txtUserPassword = new JPasswordField();
        txtUserPassword.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        cbUserRole = new JComboBox<>(new String[]{"Admin", "Kasir"});
        cbUserRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        String[] labels = {"Nama*", "Username*", "Password*", "Role"};
        JComponent[] fields = {txtUserNama, txtUserUsername, txtUserPassword, cbUserRole};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.35;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(COLOR_TEXT_DARK);
            card.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.65;
            card.add(fields[i], gbc);
        }

        JLabel formTitle = new JLabel("Input / Edit Pengguna");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(COLOR_TEXT_DARK);

        panel.add(formTitle, BorderLayout.NORTH);
        panel.add(card, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);
        btnUserTambah = buatStyledButton("Tambah", COLOR_SUCCESS);
        btnUserUpdate = buatStyledButton("Update", COLOR_PRIMARY);
        btnUserHapus = buatStyledButton("Hapus", COLOR_DANGER);
        btnUserBatal = buatStyledButton("Batal", new Color(148, 163, 184));

        btnPanel.add(btnUserTambah);
        btnPanel.add(btnUserUpdate);
        btnPanel.add(btnUserHapus);
        btnPanel.add(btnUserBatal);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buatTabelUser() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(0, 15, 0, 0));

        modelUser = new DefaultTableModel(new String[]{"ID", "Nama", "Username", "Password", "Role"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblUser = new JTable(modelUser);
        tblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblUser.setForeground(COLOR_TEXT_DARK);
        tblUser.setSelectionForeground(COLOR_TEXT_DARK);
        tblUser.setSelectionBackground(new Color(238, 242, 255));
        tblUser.setGridColor(new Color(241, 245, 249));
        tblUser.setRowHeight(28);
        tblUser.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblUser.getTableHeader().setBackground(COLOR_BORDER);
        tblUser.getTableHeader().setForeground(COLOR_TEXT_DARK);

        JScrollPane scroll = new JScrollPane(tblUser);
        scroll.setBorder(new LineBorder(COLOR_BORDER));

        JLabel tblTitle = new JLabel("Daftar Pengguna");
        tblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblTitle.setForeground(COLOR_TEXT_DARK);

        panel.add(tblTitle, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ==========================================
    // 5. TRANSACTION PANEL
    // ==========================================
    private JPanel buatPanelTransaksi() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Riwayat Transaksi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_TEXT_DARK);
        panel.add(title, BorderLayout.NORTH);

        modelTransaksi = new DefaultTableModel(new String[]{"ID Transaksi", "Tanggal", "Nama Kasir", "Total Belanja", "Diskon", "Total Bayar", "Kembalian", "Metode Pembayaran", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblTransaksi = new JTable(modelTransaksi);
        tblTransaksi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblTransaksi.setForeground(COLOR_TEXT_DARK);
        tblTransaksi.setSelectionForeground(COLOR_TEXT_DARK);
        tblTransaksi.setSelectionBackground(new Color(238, 242, 255));
        tblTransaksi.setGridColor(new Color(241, 245, 249));
        tblTransaksi.setRowHeight(28);
        tblTransaksi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblTransaksi.getTableHeader().setBackground(COLOR_BORDER);
        tblTransaksi.getTableHeader().setForeground(COLOR_TEXT_DARK);

        JScrollPane scroll = new JScrollPane(tblTransaksi);
        scroll.setBorder(new LineBorder(COLOR_BORDER));

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ==========================================
    // UI FORM BUILDERS
    // ==========================================
    private JTextField buatStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        return tf;
    }

    private JButton buatStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 32));
        return btn;
    }

    // ==========================================
    // GETTERS & DATA POPULATION
    // ==========================================
    public void setOverviewStats(double totalSales, int totalProducts, int totalUsers, int totalTransactions) {
        lblTotalPenjualanVal.setText("Rp " + String.format("%,.0f", totalSales));
        lblTotalProdukVal.setText(String.valueOf(totalProducts));
        lblTotalUserVal.setText(String.valueOf(totalUsers));
        lblTotalTransaksiVal.setText(String.valueOf(totalTransactions));
    }

    public void setProdukList(List<Produk> list, List<CategoryProduct> categories) {
        modelProduk.setRowCount(0);
        cbProdKategori.removeAllItems();
        cbProdKategori.addItem(new CategoryProduct(0, "Pilih Kategori...", ""));
        for (CategoryProduct c : categories) {
            cbProdKategori.addItem(c);
        }

        for (Produk p : list) {
            String categoryName = "-";
            for (CategoryProduct c : categories) {
                if (c.getId() == p.getCategoryId()) {
                    categoryName = c.getNamaKategori();
                    break;
                }
            }
            modelProduk.addRow(new Object[]{
                    p.getId(), p.getNamaProduk(), p.getHargaBeli(), p.getHargaJual(), p.getStok(), p.getBarcode(), categoryName
            });
        }
    }

    public void setKategoriList(List<CategoryProduct> list) {
        modelKategori.setRowCount(0);
        for (CategoryProduct c : list) {
            modelKategori.addRow(new Object[]{
                    c.getId(), c.getNamaKategori(), c.getDeskripsi()
            });
        }
    }

    public void setUserList(List<User> list) {
        modelUser.setRowCount(0);
        for (User u : list) {
            modelUser.addRow(new Object[]{
                    u.getId(), u.getNama(), u.getUsername(), u.getPassword(), u.getRole()
            });
        }
    }

    public void setTransaksiList(List<Transaksi> list) {
        modelTransaksi.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Transaksi t : list) {
            modelTransaksi.addRow(new Object[]{
                    t.getId(),
                    t.getTanggal() != null ? sdf.format(t.getTanggal()) : "-",
                    t.getKasirNama() != null ? t.getKasirNama() : "ID: " + t.getKasirId(),
                    t.getTotalHarga(),
                    t.getDiskon(),
                    t.getTotalBayar(),
                    t.getKembalian(),
                    t.getPaymentMethod(),
                    t.getStatus()
            });
        }
    }

    public JTable getTblProduk() { return tblProduk; }
    public JTable getTblKategori() { return tblKategori; }
    public JTable getTblUser() { return tblUser; }

    public JButton getBtnProdTambah() { return btnProdTambah; }
    public JButton getBtnProdUpdate() { return btnProdUpdate; }
    public JButton getBtnProdHapus() { return btnProdHapus; }
    public JButton getBtnProdBatal() { return btnProdBatal; }

    public JButton getBtnKatTambah() { return btnKatTambah; }
    public JButton getBtnKatUpdate() { return btnKatUpdate; }
    public JButton getBtnKatHapus() { return btnKatHapus; }
    public JButton getBtnKatBatal() { return btnKatBatal; }

    public JButton getBtnUserTambah() { return btnUserTambah; }
    public JButton getBtnUserUpdate() { return btnUserUpdate; }
    public JButton getBtnUserHapus() { return btnUserHapus; }
    public JButton getBtnUserBatal() { return btnUserBatal; }

    public String getProdNama() { return txtProdNama.getText().trim(); }
    public double getProdHargaBeli() throws NumberFormatException { return Double.parseDouble(txtProdHargaBeli.getText().trim()); }
    public double getProdHargaJual() throws NumberFormatException { return Double.parseDouble(txtProdHargaJual.getText().trim()); }
    public int getProdStok() throws NumberFormatException { return Integer.parseInt(txtProdStok.getText().trim()); }
    public String getProdBarcode() { return txtProdBarcode.getText().trim(); }
    public CategoryProduct getProdKategori() { return (CategoryProduct) cbProdKategori.getSelectedItem(); }

    public String getKatNama() { return txtKatNama.getText().trim(); }
    public String getKatDeskripsi() { return txtKatDeskripsi.getText().trim(); }

    public String getUserNama() { return txtUserNama.getText().trim(); }
    public String getUserUsername() { return txtUserUsername.getText().trim(); }
    public String getUserPassword() { return new String(txtUserPassword.getPassword()).trim(); }
    public String getUserRole() { return (String) cbUserRole.getSelectedItem(); }

    public void setProdFields(String nama, double hargaBeli, double hargaJual, int stok, String barcode, int categoryId) {
        txtProdNama.setText(nama);
        txtProdHargaBeli.setText(String.valueOf(hargaBeli));
        txtProdHargaJual.setText(String.valueOf(hargaJual));
        txtProdStok.setText(String.valueOf(stok));
        txtProdBarcode.setText(barcode);
        
        for (int i = 0; i < cbProdKategori.getItemCount(); i++) {
            CategoryProduct c = cbProdKategori.getItemAt(i);
            if (c.getId() == categoryId) {
                cbProdKategori.setSelectedIndex(i);
                break;
            }
        }
    }

    public void clearProdFields() {
        txtProdNama.setText("");
        txtProdHargaBeli.setText("");
        txtProdHargaJual.setText("");
        txtProdStok.setText("");
        txtProdBarcode.setText("");
        cbProdKategori.setSelectedIndex(0);
    }

    public void setKatFields(String nama, String deskripsi) {
        txtKatNama.setText(nama);
        txtKatDeskripsi.setText(deskripsi);
    }

    public void clearKatFields() {
        txtKatNama.setText("");
        txtKatDeskripsi.setText("");
    }

    public void setUserFields(String nama, String username, String password, String role) {
        txtUserNama.setText(nama);
        txtUserUsername.setText(username);
        txtUserPassword.setText(password);
        cbUserRole.setSelectedItem(role);
    }

    public void clearUserFields() {
        txtUserNama.setText("");
        txtUserUsername.setText("");
        txtUserPassword.setText("");
        cbUserRole.setSelectedIndex(0);
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public int showConfirm(String msg, String title) {
        return JOptionPane.showConfirmDialog(this, msg, title, JOptionPane.YES_NO_OPTION);
    }
}
