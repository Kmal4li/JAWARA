package app.model.interfaces;

/**
 * Interface IProduk sesuai class diagram.
 * Mendefinisikan kontrak untuk operasi CRUD produk.
 * Method: tambahProduk(), updateProduk(), hapusProduk(),
 *         getIdProduk(), getNamaProduk(), getHargaBeli(), getHargaJual()
 */
public interface IProduk {
    void tambahProduk();
    void updateProduk();
    void hapusProduk();
    int getIdProduk();
    String getNamaProduk();
    double getHargaBeli();
    double getHargaJual();
}
