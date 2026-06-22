package app.model.interfaces;

/**
 * Interface IMetodePembayaran sesuai class diagram.
 * Mendefinisikan kontrak untuk metode pembayaran.
 * Method: getMetode(), setMetode(String), getStatus(), setStatus(boolean),
 *         prosesPembayaran(total)
 */
public interface IMetodePembayaran {
    String getMetode();
    void setMetodeMetodePembayaran(String metodePembayaran);
    boolean getStatus();
    void setStatus(boolean status);
    boolean prosesPembayaran(int total);
}
