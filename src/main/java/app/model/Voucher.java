package app.model;

import app.model.interfaces.IDiskon;

public class Voucher implements IDiskon {
    private int idVoucher;
    private String kodeVoucher;
    private String tipePromo;
    private int nilaiDiskon;
    private String tanggalKadaluarsa;
    private boolean status;

    public Voucher() {}

    public Voucher(int idVoucher, String kodeVoucher, String tipePromo, int nilaiDiskon,
                   String tanggalKadaluarsa, boolean status) {
        this.idVoucher = idVoucher;
        this.kodeVoucher = kodeVoucher;
        this.tipePromo = tipePromo;
        this.nilaiDiskon = nilaiDiskon;
        this.tanggalKadaluarsa = tanggalKadaluarsa;
        this.status = status;
    }

    // Getters & Setters
    public int getIdVoucher() { return idVoucher; }
    public void setIdVoucher(int idVoucher) { this.idVoucher = idVoucher; }

    public String getKodeVoucher() { return kodeVoucher; }
    public void setKodeVoucher(String kodeVoucher) { this.kodeVoucher = kodeVoucher; }

    public String getTipePromo() { return tipePromo; }
    public void setTipePromo(String tipePromo) { this.tipePromo = tipePromo; }

    public int getNilaiDiskon() { return nilaiDiskon; }
    public void setNilaiDiskon(int nilaiDiskon) { this.nilaiDiskon = nilaiDiskon; }

    public String getTanggalKadaluarsa() { return tanggalKadaluarsa; }
    public void setTanggalKadaluarsa(String tanggalKadaluarsa) { this.tanggalKadaluarsa = tanggalKadaluarsa; }

    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public boolean isValid() {
        return status;
    }

    public boolean gunakanVoucher() {
        if (isValid()) {
            this.status = false;
            return true;
        }
        return false;
    }

    public int hitungDiskon(int totalBelanja) {
        if (!isValid()) return 0;
        if (tipePromo.equalsIgnoreCase("persen")) {
            return (int)(totalBelanja * nilaiDiskon / 100.0);
        } else {
            return Math.min(nilaiDiskon, totalBelanja);
        }
    }

    @Override
    public String toString() {
        return "Voucher{id=" + idVoucher + ", kode='" + kodeVoucher + "', diskon=" + nilaiDiskon + "}";
    }
}