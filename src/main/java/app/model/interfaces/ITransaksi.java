package app.model.interfaces;


public interface ITransaksi {
    int hitungTotal();
    int hitungPromo();
    boolean prosesPembayaran();
    double getTotal();
    double getKembalian();
    void setKembalian(double kembalian);
}
