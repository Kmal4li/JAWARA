import java.util.List;

public class Struk {

    // ==================== ATRIBUT ====================

    private String tanggal;
    private int kembalian;     

    private Transaksi transaksi;                  
    private List<DetailTransaksi> detailTransaksi; 

    // ==================== CONSTRUCTOR ====================

    public Struk(String tanggal, int kembalian, Transaksi transaksi, List<DetailTransaksi> detailTransaksi) {
        this.tanggal = tanggal;
        this.kembalian = kembalian;
        this.transaksi = transaksi;
        this.detailTransaksi = detailTransaksi;
    }

    // ==================== METODE UTAMA ====================

    public void cetakStruk() {
        System.out.println("============================================");
        System.out.println("           JAWARA - Jagoan Warung           ");
        System.out.println("============================================");
        System.out.println("Tanggal       : " + tanggal);
        System.out.println("ID Transaksi  : " + transaksi.getIdTransaksi());
        System.out.println("Status        : " + transaksi.getStatus());
        System.out.println("--------------------------------------------");
        System.out.println(String.format("%-20s %5s %12s", "Produk", "Qty", "Subtotal"));
        System.out.println("--------------------------------------------");

        if (detailTransaksi != null && !detailTransaksi.isEmpty()) {
            for (DetailTransaksi detail : detailTransaksi) {
                System.out.println(String.format(
                    "%-20s %5d %12s",
                    detail.getNamaProduk(),
                    detail.getKuantitas(),
                    "Rp" + detail.hitungSubTotal()
                ));
            }
        } else {
            System.out.println("(Tidak ada detail item)");
        }

        System.out.println("--------------------------------------------");
        System.out.println("Total Harga   : Rp" + transaksi.getTotalHarga());
        System.out.println("Total Bayar   : Rp" + transaksi.getTotalBayar());
        System.out.println("Kembalian     : Rp" + kembalian);
        System.out.println("============================================");
        System.out.println("       Terima kasih telah berbelanja!       ");
        System.out.println("============================================");
    }

    // ==================== GETTER & SETTER ====================
    
    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getKembalian() {
        return kembalian;
    }

    public void setKembalian(int kembalian) {
        this.kembalian = kembalian;
    }

    public Transaksi getTransaksi() {
        return transaksi;
    }

    public List<DetailTransaksi> getDetailTransaksi() {
        return detailTransaksi;
    }
}