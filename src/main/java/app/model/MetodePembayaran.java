package app.model;

import app.model.interfaces.IMetodePembayaran;

public class MetodePembayaran implements IMetodePembayaran {
    private String metodePembayaran;
    private boolean status;

    public MetodePembayaran() {}

    public MetodePembayaran(String metodePembayaran, boolean status) {
        this.metodePembayaran = metodePembayaran;
        this.status = status;
    }

    // Getters & Setters
    public String getMetode() { return metodePembayaran; }
    public void setMetodeMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }

    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public boolean prosesPembayaran(int total) {
        if (!status) return false;
        System.out.println("Memproses pembayaran Rp" + total + " via " + metodePembayaran);
        return true;
    }

    @Override
    public String toString() {
        return metodePembayaran;
    }
}