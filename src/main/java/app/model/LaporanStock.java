package app.model;

import app.model.interfaces.ILaporan;


public class LaporanStock implements ILaporan {
    private int stokMasuk;
    private int stokKeluar;

    public LaporanStock() {}

    public LaporanStock(int stokMasuk, int stokKeluar) {
        this.stokMasuk = stokMasuk;
        this.stokKeluar = stokKeluar;
    }

    public int getStokMasuk() { 
        return stokMasuk; 
    }
    public void setStokMasuk(int stokMasuk) { 
        this.stokMasuk = stokMasuk; 
    }

    public int getStokKeluar() { 
        return stokKeluar; 
    }
    public void setStokKeluar(int stokKeluar) {
         this.stokKeluar = stokKeluar;
         }

    public void generateLaporan() {
        System.out.println("=== LAPORAN STOK ===");
        System.out.println("Stok Masuk  : " + stokMasuk);
        System.out.println("Stok Keluar : " + stokKeluar);
        System.out.println("Selisih     : " + (stokMasuk - stokKeluar));
        System.out.println("====================");
    }

    @Override
    public String toString() {
        return "LaporanStock{masuk=" + stokMasuk + ", keluar=" + stokKeluar + "}";
    }
}
