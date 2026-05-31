


public class Kasir extends User {



    public Kasir() {
        super();
        this.role = "Kasir";
    }

    public Kasir(int id, String nama, String password) {
        super(id, nama, password, "Kasir");
    }


    public void prosesTransaksi() {
        System.out.println("[KASIR] " + nama + " memulai transaksi baru.");
        System.out.println("  > Alur: tambahItemCart -> hitungTotal -> prosesPembayaran -> selesai");
    }

 
    public void cetakStruk() {
        System.out.println("[KASIR] " + nama + " mencetak struk transaksi.");
        System.out.println("  > Struk berisi: tanggal, detail item, total bayar, kembalian");
    }

 
    @Override
    public String toString() {
        return "Kasir{id=" + id + ", nama='" + nama + "', role='" + role + "'}";
    }
}