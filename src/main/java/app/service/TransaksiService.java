package app.service;

import app.model.ItemCart;
import app.model.Transaksi;
import app.repository.TransaksiRepository;
import java.util.Date;

public class TransaksiService {
    private TransaksiRepository transaksiRepository;

    public TransaksiService() {
        this.transaksiRepository = new TransaksiRepository();
    }

    public void prosesPembayaran(Transaksi transaksi) throws Exception {
        if (transaksi.getItems() == null || transaksi.getItems().isEmpty()) {
            throw new Exception("Keranjang kosong!");
        }
        if (transaksi.getUangDiterima() < transaksi.getTotalBayar()) {
            throw new Exception("Uang yang diterima kurang dari total bayar!");
        }
        
        // Kembalian otomatis dihitung ulang untuk keamanan
        transaksi.setKembalian(transaksi.getUangDiterima() - transaksi.getTotalBayar());
        transaksi.setTanggal(new Date());

        // Simpan ke database (Repository akan menghandle pengecekan stok otomatis melalui query)
        transaksiRepository.simpanTransaksi(transaksi);
    }

    public java.util.List<Transaksi> getAllTransaksi() {
        return transaksiRepository.findAllWithKasirName();
    }
}
