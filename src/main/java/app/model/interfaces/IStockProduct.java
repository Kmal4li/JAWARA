package app.model.interfaces;

public interface IStockProduct {
    int getJumlahStok();
    void tambahStok(int jumlah);
    void kurangStok(int jumlah);
}
