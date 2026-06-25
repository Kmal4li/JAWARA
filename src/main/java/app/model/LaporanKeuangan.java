package app.model;

import java.util.List;

import app.model.interfaces.ILaporan;

/**
 * Class LaporanKeuangan sesuai class diagram.
 * Atribut: totalPendapatan, totalTransaksi, tanggalCetak
 */
public class LaporanKeuangan implements ILaporan {
    private int totalPendapatan;
    private int totalTransaksi;
    private String tanggalCetak;

    public LaporanKeuangan() {}

    public LaporanKeuangan(int totalPendapatan, int totalTransaksi, String tanggalCetak) {
        this.totalPendapatan = totalPendapatan;
        this.totalTransaksi = totalTransaksi;
        this.tanggalCetak = tanggalCetak;
    }

    public int getTotalPendapatan() { 
        return totalPendapatan;
     }
    public void setTotalPendapatan(int totalPendapatan) {
         this.totalPendapatan = totalPendapatan; 
        }

    public int getTotalTransaksi() { 
        return totalTransaksi;
     }
    public void setTotalTransaksi(int totalTransaksi) {
         this.totalTransaksi = totalTransaksi; 
        }

    public String getTanggalCetak() {
         return tanggalCetak;
         }
    public void setTanggalCetak(String tanggalCetak) { 
        this.tanggalCetak = tanggalCetak; 
    }

    public int hitungTotalPendapatan(List<Transaksi> transaksi) {
        int total = 0;
        for (Transaksi t : transaksi) {
            total += (int) t.getTotalBayar();
        }
        this.totalPendapatan = total;
        return total;
    }

    public int hitungTotalTransaksi(List<Transaksi> transaksi) {
        this.totalTransaksi = transaksi.size();
        return this.totalTransaksi;
    }

    public void generateLaporan() {
        System.out.println("=== LAPORAN KEUANGAN ===");
        System.out.println("Tanggal Cetak    : " + tanggalCetak);
        System.out.println("Total Pendapatan : Rp" + totalPendapatan);
        System.out.println("Total Transaksi  : " + totalTransaksi);
        System.out.println("========================");
    }

    @Override
    public String toString() {
        return "LaporanKeuangan{pendapatan=" + totalPendapatan + ", transaksi=" + totalTransaksi + "}";
    }
}
