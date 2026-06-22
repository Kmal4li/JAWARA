package app.model.interfaces;

/**
 * Interface IItemCart sesuai class diagram.
 * Mendefinisikan kontrak untuk operasi item keranjang.
 * Method: hitungSubTotal(), tambahKuantitas(jumlah), kurangKuantitas(jumlah)
 */
public interface IItemCart {
    int hitungSubTotal();
    void tambahKuantitas(int jumlah);
    void kurangKuantitas(int jumlah);
}
