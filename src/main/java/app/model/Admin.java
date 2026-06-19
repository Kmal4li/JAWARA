package app.model;

/**
 * Class Admin extends User sesuai class diagram.
 * Method: kelolaProduk(), kelolaVoucher(), kelolaPromo(), lihat_laporanKeuangan()
 */
public class Admin extends User {
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
