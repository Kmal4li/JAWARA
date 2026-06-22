package app.model.interfaces;

/**
 * Interface IStockProduct sesuai class diagram.
 * Mendefinisikan kontrak untuk operasi kelola stok.
 * Method: getJumlahStok(), tambahStok(jumlah), kurangStok(jumlah)
 */
public interface IStockProduct {
    int getJumlahStok();
    void tambahStok(int jumlah);
    void kurangStok(int jumlah);
}
