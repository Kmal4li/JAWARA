package app.model.interfaces;

/**
 * Interface IDiskon sesuai class diagram.
 * Mendefinisikan kontrak untuk komponen yang bisa memberikan diskon (Voucher, Promo).
 * Method: hitungDiskon(totalBelanja), isValid(), getStatus(), setStatus(status)
 */
public interface IDiskon {
    int hitungDiskon(int totalBelanja);
    boolean isValid();
    boolean getStatus();
    void setStatus(boolean status);
}
