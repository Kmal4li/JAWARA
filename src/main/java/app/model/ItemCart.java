package app.model;

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
}
