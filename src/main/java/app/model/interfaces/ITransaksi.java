package app.model.interfaces;

/**
 * Interface ITransaksi sesuai class diagram.
 * Mendefinisikan kontrak untuk operasi transaksi.
 * Method: hitungTotal(), hitungPromo(), prosesPembayaran(), getTotal(), getBeli(),
 *         getKembalian(), setKembalian()
 */
public interface ITransaksi {
    int hitungTotal();
    int hitungPromo();
    boolean prosesPembayaran();
    double getTotal();
    double getKembalian();
    void setKembalian(double kembalian);
}
