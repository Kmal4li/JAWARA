package app.model;

/**
 * Class Kasir extends User sesuai class diagram.
 * Method: prosesTransaksi(), cetakStruk()
 */
public class Kasir extends User {
    public Kasir() {
        super();
        this.role = "Kasir";
    }

    public Kasir(int id, String nama, String username, String password) {
        super(id, nama, username, password, "Kasir");
    }

    // Method sesuai class diagram
    public void prosesTransaksi() {
        System.out.println("Kasir " + getNama() + " memproses transaksi.");
    }

    public void cetakStruk() {
        System.out.println("Kasir " + getNama() + " mencetak struk.");
    }
}
