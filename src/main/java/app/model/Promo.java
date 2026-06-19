package app.model;

/**
 * Class Promo sesuai class diagram.
 * Atribut: namaPromo, tipePromo, nilaiDiskon, tanggalMulai, tanggalAkhir, status
 */
public class Promo {
    private String namaPromo;
    private String tipePromo;
    private int nilaiDiskon;
    private String tanggalMulai;
    private String tanggalAkhir;
    private boolean status;

    public Promo() {}

    public Promo(String namaPromo, String tipePromo, int nilaiDiskon,
                 String tanggalMulai, String tanggalAkhir, boolean status) {
        this.namaPromo = namaPromo;
        this.tipePromo = tipePromo;
        this.nilaiDiskon = nilaiDiskon;
        this.tanggalMulai = tanggalMulai;
        this.tanggalAkhir = tanggalAkhir;
        this.status = status;
    }

    // Getters & Setters
    public String getNamaPromo() { return namaPromo; }
    public void setNamaPromo(String namaPromo) { this.namaPromo = namaPromo; }

    public String getTipePromo() { return tipePromo; }
    public void setTipePromo(String tipePromo) { this.tipePromo = tipePromo; }

    public int getNilaiDiskon() { return nilaiDiskon; }
    public void setNilaiDiskon(int nilaiDiskon) { this.nilaiDiskon = nilaiDiskon; }

    public String getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(String tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public String getTanggalAkhir() { return tanggalAkhir; }
    public void setTanggalAkhir(String tanggalAkhir) { this.tanggalAkhir = tanggalAkhir; }

    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    // Method sesuai class diagram
    public boolean isValid() {
        return status;
    }

    public boolean setStatusBerlangsung(String tanggal) {
        // Cek apakah tanggal berada dalam rentang promo
        if (tanggal.compareTo(tanggalMulai) >= 0 && tanggal.compareTo(tanggalAkhir) <= 0) {
            this.status = true;
            return true;
        }
        this.status = false;
        return false;
    }

    public int hitungJumlahDiskon(int totalBelanja) {
        if (!isValid()) return 0;
        if (tipePromo.equalsIgnoreCase("persen")) {
            return (int) (totalBelanja * nilaiDiskon / 100.0);
        } else {
            return Math.min(nilaiDiskon, totalBelanja);
        }
    }

    public int applyPromoToDiskon(int totalBelanja) {
        int diskon = hitungJumlahDiskon(totalBelanja);
        return totalBelanja - diskon;
    }

    @Override
    public String toString() {
        return "Promo{nama='" + namaPromo + "', tipe='" + tipePromo + "', diskon=" + nilaiDiskon + "}";
    }
}
