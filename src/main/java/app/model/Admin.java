package app.model;

import app.model.interfaces.IAdmin;


public class Admin extends User implements IAdmin {
    public Admin() {
        super();
        this.role = "Admin";
    }

    public Admin(int id, String nama, String username, String password) {
        super(id, nama, username, password, "Admin");
    }

    // Method sesuai class diagram
    public void kelolaProduk() {
        System.out.println("Admin " + getNama() + " membuka kelola produk.");
    }

    public void kelolaVoucher() {
        System.out.println("Admin " + getNama() + " membuka kelola voucher.");
    }

    public void kelolaPromo() {
        System.out.println("Admin " + getNama() + " membuka kelola promo.");
    }

    public void lihat_laporanKeuangan() {
        System.out.println("Admin " + getNama() + " membuka laporan keuangan.");
    }
}
