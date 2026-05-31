package app.model;

public class DetailTransaksi {
    private int id;
    private int transactionId;
    private int productId;
    private int qty;
    private double hargaSatuan;
    private double subtotal;

    public DetailTransaksi() {}

    public DetailTransaksi(int transactionId, int productId, int qty, double hargaSatuan, double subtotal) {
        this.transactionId = transactionId;
        this.productId = productId;
        this.qty = qty;
        this.hargaSatuan = hargaSatuan;
        this.subtotal = subtotal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public double getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(double hargaSatuan) { this.hargaSatuan = hargaSatuan; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
