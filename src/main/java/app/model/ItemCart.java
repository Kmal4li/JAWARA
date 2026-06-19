package app.model;

/**
 * Class ItemCart sesuai class diagram.
 * Atribut: kuantitas, hargaSatuan, subTotal
 * Method: hitungSubTotal(), tambahKuantitas(), kurangKuantitas()
 */
public class ItemCart {
    private Produk produk;
    private int qty;
    private double hargaSatuan;
    private double subtotal;

    public ItemCart(Produk produk, int qty) {
        this.produk = produk;
        this.qty = qty;
        this.hargaSatuan = produk.getHargaJual();
        this.subtotal = this.hargaSatuan * qty;
    }

    // Sesuai class diagram: <<create>> ItemCart(kuantitas, hargaSatuan, subTotal)
    public ItemCart(int kuantitas, int hargaSatuan, int subTotal) {
        this.qty = kuantitas;
        this.hargaSatuan = hargaSatuan;
        this.subtotal = subTotal;
    }

    public Produk getProduk() { return produk; }
    public void setProduk(Produk produk) { this.produk = produk; calculateSubtotal(); }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; calculateSubtotal(); }
    public double getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(double hargaSatuan) { this.hargaSatuan = hargaSatuan; calculateSubtotal(); }
    public double getSubtotal() { return subtotal; }

    private void calculateSubtotal() {
        this.subtotal = this.hargaSatuan * this.qty;
    }

    // Method sesuai class diagram
    public int hitungSubTotal() {
        return (int) (this.hargaSatuan * this.qty);
    }

    public void tambahKuantitas(int jumlah) {
        this.qty += jumlah;
        calculateSubtotal();
    }

    public void kurangKuantitas(int jumlah) {
        if (this.qty - jumlah >= 0) {
            this.qty -= jumlah;
            calculateSubtotal();
        }
    }
}

