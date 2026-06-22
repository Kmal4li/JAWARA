package app.model.interfaces;

/**
 * Interface IAdmin sesuai class diagram.
 * Mendefinisikan kontrak untuk fitur-fitur Admin.
 * Method: kelolaProduk(), kelolaVoucher(), kelolaPromo(), lihat_laporanKeuangan()
 */
public interface IAdmin {
    void kelolaProduk();
    void kelolaVoucher();
    void kelolaPromo();
    void lihat_laporanKeuangan();
}
