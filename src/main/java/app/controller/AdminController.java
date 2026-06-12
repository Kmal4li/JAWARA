package app.controller;

import app.model.CategoryProduct;
import app.model.Produk;
import app.model.User;
import app.model.Transaksi;
import app.service.CategoryService;
import app.service.ProdukService;
import app.service.UserService;
import app.service.TransaksiService;
import app.service.AuthService;
import app.view.admin.AdminDashboardView;
import app.view.admin.MetodePembayaranGUI;
import app.view.admin.VoucherGUI;
import app.view.auth.LoginView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class AdminController {
    private AdminDashboardView view;
    private ProdukService produkService;
    private CategoryService categoryService;
    private UserService userService;
    private TransaksiService transaksiService;
    private User currentAdmin;

    private int selectedProductId = -1;
    private int selectedCategoryId = -1;
    private int selectedUserId = -1;

    public AdminController(AdminDashboardView view, ProdukService produkService, 
                           CategoryService categoryService, UserService userService, 
                           TransaksiService transaksiService, User currentAdmin) {
        this.view = view;
        this.produkService = produkService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.transaksiService = transaksiService;
        this.currentAdmin = currentAdmin;
        
        this.view.setController(this);
        initEventHandlers();
    }

    public void start() {
        refreshOverviewStats();
        // Load default lists
        refreshProdukTable();
        refreshKategoriTable();
        refreshUserTable();
        refreshTransaksiTable();
        
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });
    }

    private void initEventHandlers() {
        // --- Product CRUD Event Handlers ---
        view.getBtnProdTambah().addActionListener(e -> handleAddProduct());
        view.getBtnProdUpdate().addActionListener(e -> handleUpdateProduct());
        view.getBtnProdHapus().addActionListener(e -> handleDeleteProduct());
        view.getBtnProdBatal().addActionListener(e -> {
            view.clearProdFields();
            selectedProductId = -1;
            view.getTblProduk().clearSelection();
        });
        view.getTblProduk().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTblProduk().getSelectedRow();
                if (row >= 0) {
                    selectedProductId = (int) view.getTblProduk().getValueAt(row, 0);
                    String nama = (String) view.getTblProduk().getValueAt(row, 1);
                    double hargaBeli = (double) view.getTblProduk().getValueAt(row, 2);
                    double hargaJual = (double) view.getTblProduk().getValueAt(row, 3);
                    int stok = (int) view.getTblProduk().getValueAt(row, 4);
                    String barcode = (String) view.getTblProduk().getValueAt(row, 5);
                    String categoryName = (String) view.getTblProduk().getValueAt(row, 6);
                    int categoryId = 0;
                    for (CategoryProduct c : categoryService.getAllCategories()) {
                        if (c.getNamaKategori().equals(categoryName)) {
                            categoryId = c.getId();
                            break;
                        }
                    }
                    view.setProdFields(nama, hargaBeli, hargaJual, stok, barcode, categoryId);
                }
            }
        });

        // --- Category CRUD Event Handlers ---
        view.getBtnKatTambah().addActionListener(e -> handleAddCategory());
        view.getBtnKatUpdate().addActionListener(e -> handleUpdateCategory());
        view.getBtnKatHapus().addActionListener(e -> handleDeleteCategory());
        view.getBtnKatBatal().addActionListener(e -> {
            view.clearKatFields();
            selectedCategoryId = -1;
            view.getTblKategori().clearSelection();
        });
        view.getTblKategori().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTblKategori().getSelectedRow();
                if (row >= 0) {
                    selectedCategoryId = (int) view.getTblKategori().getValueAt(row, 0);
                    String nama = (String) view.getTblKategori().getValueAt(row, 1);
                    String deskripsi = (String) view.getTblKategori().getValueAt(row, 2);
                    view.setKatFields(nama, deskripsi);
                }
            }
        });

        // --- User CRUD Event Handlers ---
        view.getBtnUserTambah().addActionListener(e -> handleAddUser());
        view.getBtnUserUpdate().addActionListener(e -> handleUpdateUser());
        view.getBtnUserHapus().addActionListener(e -> handleDeleteUser());
        view.getBtnUserBatal().addActionListener(e -> {
            view.clearUserFields();
            selectedUserId = -1;
            view.getTblUser().clearSelection();
        });
        view.getTblUser().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTblUser().getSelectedRow();
                if (row >= 0) {
                    selectedUserId = (int) view.getTblUser().getValueAt(row, 0);
                    String nama = (String) view.getTblUser().getValueAt(row, 1);
                    String username = (String) view.getTblUser().getValueAt(row, 2);
                    String password = (String) view.getTblUser().getValueAt(row, 3);
                    String role = (String) view.getTblUser().getValueAt(row, 4);
                    view.setUserFields(nama, username, password, role);
                }
            }
        });
    }

    // ==========================================
    // REFRESH DATA & TABLES
    // ==========================================
    public void refreshOverviewStats() {
        List<Transaksi> transactions = transaksiService.getAllTransaksi();
        List<Produk> products = produkService.getAllProduk();
        List<User> users = userService.getAllUsers();

        double totalSales = 0;
        for (Transaksi t : transactions) {
            totalSales += t.getTotalBayar();
        }

        view.setOverviewStats(totalSales, products.size(), users.size(), transactions.size());
    }

    public void refreshProdukTable() {
        List<Produk> list = produkService.getAllProduk();
        List<CategoryProduct> categories = categoryService.getAllCategories();
        view.setProdukList(list, categories);
    }

    public void refreshKategoriTable() {
        List<CategoryProduct> list = categoryService.getAllCategories();
        view.setKategoriList(list);
    }

    public void refreshUserTable() {
        List<User> list = userService.getAllUsers();
        view.setUserList(list);
    }

    public void refreshTransaksiTable() {
        List<Transaksi> list = transaksiService.getAllTransaksi();
        view.setTransaksiList(list);
    }

    // ==========================================
    // PRODUCT ACTIONS
    // ==========================================
    private void handleAddProduct() {
        try {
            String nama = view.getProdNama();
            double hargaBeli = view.getProdHargaBeli();
            double hargaJual = view.getProdHargaJual();
            int stok = view.getProdStok();
            String barcode = view.getProdBarcode();
            CategoryProduct kat = view.getProdKategori();

            int categoryId = (kat != null) ? kat.getId() : 0;

            Produk p = new Produk(0, nama, hargaBeli, hargaJual, stok, barcode, categoryId);
            produkService.addProduk(p);
            
            view.showMessage("Produk berhasil ditambahkan!");
            view.clearProdFields();
            refreshProdukTable();
            refreshOverviewStats();
        } catch (NumberFormatException ex) {
            view.showError("Harga dan Stok harus berupa angka!");
        } catch (Exception ex) {
            view.showError("Gagal menambahkan produk: " + ex.getMessage());
        }
    }

    private void handleUpdateProduct() {
        if (selectedProductId == -1) {
            view.showError("Pilih produk di tabel terlebih dahulu!");
            return;
        }
        try {
            String nama = view.getProdNama();
            double hargaBeli = view.getProdHargaBeli();
            double hargaJual = view.getProdHargaJual();
            int stok = view.getProdStok();
            String barcode = view.getProdBarcode();
            CategoryProduct kat = view.getProdKategori();

            int categoryId = (kat != null) ? kat.getId() : 0;

            Produk p = new Produk(selectedProductId, nama, hargaBeli, hargaJual, stok, barcode, categoryId);
            produkService.updateProduk(p);

            view.showMessage("Produk berhasil diperbarui!");
            view.clearProdFields();
            selectedProductId = -1;
            refreshProdukTable();
            refreshOverviewStats();
        } catch (NumberFormatException ex) {
            view.showError("Harga dan Stok harus berupa angka!");
        } catch (Exception ex) {
            view.showError("Gagal memperbarui produk: " + ex.getMessage());
        }
    }

    private void handleDeleteProduct() {
        if (selectedProductId == -1) {
            view.showError("Pilih produk di tabel terlebih dahulu!");
            return;
        }
        int confirm = view.showConfirm("Apakah Anda yakin ingin menghapus produk ini?", "Konfirmasi Hapus");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                produkService.deleteProduk(selectedProductId);
                view.showMessage("Produk berhasil dihapus!");
                view.clearProdFields();
                selectedProductId = -1;
                refreshProdukTable();
                refreshOverviewStats();
            } catch (Exception ex) {
                view.showError("Gagal menghapus produk: " + ex.getMessage());
            }
        }
    }

    // ==========================================
    // CATEGORY ACTIONS
    // ==========================================
    private void handleAddCategory() {
        try {
            String nama = view.getKatNama();
            String desc = view.getKatDeskripsi();

            CategoryProduct c = new CategoryProduct(0, nama, desc);
            categoryService.addCategory(c);

            view.showMessage("Kategori berhasil ditambahkan!");
            view.clearKatFields();
            refreshKategoriTable();
            refreshProdukTable(); // Refresh category list in product combo
        } catch (Exception ex) {
            view.showError("Gagal menambahkan kategori: " + ex.getMessage());
        }
    }

    private void handleUpdateCategory() {
        if (selectedCategoryId == -1) {
            view.showError("Pilih kategori di tabel terlebih dahulu!");
            return;
        }
        try {
            String nama = view.getKatNama();
            String desc = view.getKatDeskripsi();

            CategoryProduct c = new CategoryProduct(selectedCategoryId, nama, desc);
            categoryService.updateCategory(c);

            view.showMessage("Kategori berhasil diperbarui!");
            view.clearKatFields();
            selectedCategoryId = -1;
            refreshKategoriTable();
            refreshProdukTable();
        } catch (Exception ex) {
            view.showError("Gagal memperbarui kategori: " + ex.getMessage());
        }
    }

    private void handleDeleteCategory() {
        if (selectedCategoryId == -1) {
            view.showError("Pilih kategori di tabel terlebih dahulu!");
            return;
        }
        int confirm = view.showConfirm("Apakah Anda yakin ingin menghapus kategori ini?", "Konfirmasi Hapus");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                categoryService.deleteCategory(selectedCategoryId);
                view.showMessage("Kategori berhasil dihapus!");
                view.clearKatFields();
                selectedCategoryId = -1;
                refreshKategoriTable();
                refreshProdukTable();
            } catch (Exception ex) {
                view.showError("Gagal menghapus kategori: " + ex.getMessage());
            }
        }
    }

    // ==========================================
    // USER ACTIONS
    // ==========================================
    private void handleAddUser() {
        try {
            String nama = view.getUserNama();
            String username = view.getUserUsername();
            String password = view.getUserPassword();
            String role = view.getUserRole();

            User u = new User(0, nama, username, password, role);
            userService.addUser(u);

            view.showMessage("Pengguna berhasil ditambahkan!");
            view.clearUserFields();
            refreshUserTable();
            refreshOverviewStats();
        } catch (Exception ex) {
            view.showError("Gagal menambahkan pengguna: " + ex.getMessage());
        }
    }

    private void handleUpdateUser() {
        if (selectedUserId == -1) {
            view.showError("Pilih pengguna di tabel terlebih dahulu!");
            return;
        }
        try {
            String nama = view.getUserNama();
            String username = view.getUserUsername();
            String password = view.getUserPassword();
            String role = view.getUserRole();

            User u = new User(selectedUserId, nama, username, password, role);
            userService.updateUser(u);

            view.showMessage("Pengguna berhasil diperbarui!");
            view.clearUserFields();
            selectedUserId = -1;
            refreshUserTable();
            refreshOverviewStats();
        } catch (Exception ex) {
            view.showError("Gagal memperbarui pengguna: " + ex.getMessage());
        }
    }

    private void handleDeleteUser() {
        if (selectedUserId == -1) {
            view.showError("Pilih pengguna di tabel terlebih dahulu!");
            return;
        }
        if (selectedUserId == currentAdmin.getId()) {
            view.showError("Anda tidak dapat menghapus akun Anda sendiri!");
            return;
        }
        int confirm = view.showConfirm("Apakah Anda yakin ingin menghapus pengguna ini?", "Konfirmasi Hapus");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                userService.deleteUser(selectedUserId);
                view.showMessage("Pengguna berhasil dihapus!");
                view.clearUserFields();
                selectedUserId = -1;
                refreshUserTable();
                refreshOverviewStats();
            } catch (Exception ex) {
                view.showError("Gagal menghapus pengguna: " + ex.getMessage());
            }
        }
    }

    // ==========================================
    // OTHER NAVIGATION ACTIONS
    // ==========================================
    public void openVoucherGUI() {
        SwingUtilities.invokeLater(() -> {
            VoucherGUI voucherGUI = new VoucherGUI();
            voucherGUI.setVisible(true);
        });
    }

    public void openMetodePembayaranGUI() {
        SwingUtilities.invokeLater(() -> {
            MetodePembayaranGUI paymentGUI = new MetodePembayaranGUI();
            paymentGUI.setVisible(true);
        });
    }

    public void logout() {
        view.dispose();
        // Return to login
        AuthService authService = new AuthService();
        LoginView loginView = new LoginView();
        LoginController loginController = new LoginController(loginView, authService);
        loginController.start();
    }
}
