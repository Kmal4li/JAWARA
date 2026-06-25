package app.model.interfaces;


public interface IMetodePembayaran {
    String getMetode();
    void setMetodeMetodePembayaran(String metodePembayaran);
    boolean getStatus();
    void setStatus(boolean status);
    boolean prosesPembayaran(int total);
}
