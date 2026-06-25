package app.model.interfaces;


public interface IItemCart {
    int hitungSubTotal();
    void tambahKuantitas(int jumlah);
    void kurangKuantitas(int jumlah);
}
