package app.model;

import app.model.interfaces.IStockProduct;

/**
 * Class StockProduct sesuai class diagram.
 * Atribut: jumlahStok
 */
public class StockProduct implements IStockProduct {
    private int jumlahStok;

    public StockProduct() {}

    public StockProduct(int jumlahStok) {
        this.jumlahStok = jumlahStok;
    }

    // Getters & Setters
    public int getJumlahStok() { return jumlahStok; }
    public void setJumlahStok(int jumlahStok) { this.jumlahStok = jumlahStok; }

    // Method sesuai class diagram
    public void tambahStok(int jumlah) {
        this.jumlahStok += jumlah;
    }

    public void kurangStok(int jumlah) {
        if (this.jumlahStok >= jumlah) {
            this.jumlahStok -= jumlah;
        }
    }

    @Override
    public String toString() {
        return "StockProduct{jumlahStok=" + jumlahStok + "}";
    }
}
