public class StockProduct {
    // ==================== ATRIBUT ====================

    private int stok; 
    private Produk produk;

    // ==================== CONSTRUCTOR ====================

    public StockProduct(Produk produk, int stokAwal) {
        this.produk = produk;
        this.stok = stokAwal;
    }

    // ==================== METODE ====================

    public int getJumlahStok() {
        return stok;
    }

    public void setJumlahStok(int stok) {
        this.stok = stok;
    }

    public void tambahStok(int jumlah) {
        if (jumlah > 0) {
            this.stok += jumlah;
        }
    }

    public boolean kurangiStok(int jumlah) {
        if (jumlah > 0 && this.stok >= jumlah) {
            this.stok -= jumlah;
            return true;
        }
        return false;
    }

    public boolean isStokTersedia() {
        return this.stok > 0;
    }

    // ==================== GETTER PRODUK ====================

    public Produk getProduk() {
        return produk;
    }

    public void tampilkanInfo() {
        System.out.println("=== Info Stok Produk ===");
        System.out.println("Produk     : " + produk.getNamaProduk());
        System.out.println("Stok Saat Ini : " + stok + " unit");
        System.out.println("Status     : " + (isStokTersedia() ? "Tersedia" : "Habis"));
    }
}