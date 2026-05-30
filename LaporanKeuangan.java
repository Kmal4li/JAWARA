import java.util.List;

public class LaporanKeuangan {

    // ==================== ATRIBUT ====================

    private int totalPendapatan;    // Total pendapatan dari seluruh transaksi
    private int totalTransaksi;     // Jumlah transaksi dalam periode tertentu
    private String tanggalDibuat;   // Tanggal laporan dibuat

    // ==================== CONSTRUCTOR ====================

    public LaporanKeuangan(String tanggalDibuat) {
        this.tanggalDibuat = tanggalDibuat;
        this.totalPendapatan = 0;
        this.totalTransaksi = 0;
    }

    // ==================== METODE UTAMA ====================

    public int hitungTotalPendapatan(List<Transaksi> transaksi) {
        int total = 0;
        if (transaksi != null) {
            for (Transaksi t : transaksi) {
                // Hanya hitung transaksi yang statusnya "selesai"
                if ("selesai".equalsIgnoreCase(t.getStatus())) {
                    total += t.getTotalBayar();
                }
            }
        }
        this.totalPendapatan = total;
        return this.totalPendapatan;
    }

    public int hitungTotalTransaksi(List<Transaksi> transaksi) {
        int jumlah = 0;
        if (transaksi != null) {
            for (Transaksi t : transaksi) {
                if ("selesai".equalsIgnoreCase(t.getStatus())) {
                    jumlah++;
                }
            }
        }
        this.totalTransaksi = jumlah;
        return this.totalTransaksi;
    }

    public void generateLaporan() {
        System.out.println("============================================");
        System.out.println("         LAPORAN KEUANGAN - JAWARA          ");
        System.out.println("============================================");
        System.out.println("Tanggal Dibuat    : " + tanggalDibuat);
        System.out.println("--------------------------------------------");
        System.out.println("Total Transaksi   : " + totalTransaksi + " transaksi");
        System.out.println("Total Pendapatan  : Rp" + totalPendapatan);

        if (totalTransaksi > 0) {
            int rataRata = totalPendapatan / totalTransaksi;
            System.out.println("Rata-rata/Transaksi: Rp" + rataRata);
        } else {
            System.out.println("Rata-rata/Transaksi: Rp0");
        }

        System.out.println("============================================");
        System.out.println("         -- Akhir Laporan Keuangan --       ");
        System.out.println("============================================");
    }

    public void generateLaporan(List<Transaksi> transaksi) {
        hitungTotalPendapatan(transaksi);
        hitungTotalTransaksi(transaksi);
        generateLaporan();
    }

    // ==================== GETTER & SETTER ====================

    public int getTotalPendapatan() {
        return totalPendapatan;
    }

    public int getTotalTransaksi() {
        return totalTransaksi;
    }

    public String getTanggalDibuat() {
        return tanggalDibuat;
    }

    public void setTanggalDibuat(String tanggalDibuat) {
        this.tanggalDibuat = tanggalDibuat;
    }
}