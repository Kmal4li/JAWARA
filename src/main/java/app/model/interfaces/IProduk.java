package app.model.interfaces;

public interface IProduk {
    void tambahProduk();
    void updateProduk();
    void hapusProduk();
    int getIdProduk();
    String getNamaProduk();
    double getHargaBeli();
    double getHargaJual();
}
