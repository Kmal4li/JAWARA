package app.model;

import app.model.interfaces.IStockProduct;


public class StockProduct implements IStockProduct {
    private int jumlahStok;

    public StockProduct() {}

    public StockProduct(int jumlahStok) {
        this.jumlahStok = jumlahStok;
    }

    public int getJumlahStok() {
         return jumlahStok;
         }
    public void setJumlahStok(int jumlahStok) {
         this.jumlahStok = jumlahStok; 
        }

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
