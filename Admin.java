
public class Admin extends User {

    public Admin() {
        super();
        this.role = "Admin";
    }

    public Admin(int id, String nama, String password) {
        super(id, nama, password, "Admin");
    }

    public void kelolaProduk() {
        System.out.println("[ADMIN] " + nama + " mengakses manajemen produk.");
        System.out.println("  > Tersedia: tambahProduk | updateProduk | hapusProduk");
    }

    
    public void kelolaPromo() {
        System.out.println("[ADMIN] " + nama + " mengakses manajemen promo.");
        System.out.println("  > Tersedia: tambahPromo | updatePromo | aktifkan | nonaktifkan");
    }

   
    @Override
    public void kelolaVoucher() {
        System.out.println("[ADMIN] " + nama + " mengakses manajemen voucher.");
        System.out.println("  > Tersedia: tambahVoucher | hapusVoucher | nonaktifkanVoucher");
    }

    @Override
    public String toString() {
        return "Admin{id=" + id + ", nama='" + nama + "', role='" + role + "'}";
    }
}