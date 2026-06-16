package app.controller;

import app.model.ItemCart;
import app.model.Produk;
import app.model.Transaksi;
import app.model.User;
import app.service.ProdukService;
import app.service.TransaksiService;
import app.view.kasir.KasirView;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class KasirController {
    private KasirView view;
    private ProdukService produkService;
    private TransaksiService transaksiService;
    private User kasir;
    
    private List<ItemCart> keranjang = new ArrayList<>();

    public KasirController(KasirView view, ProdukService produkService, TransaksiService transaksiService, User kasir) {
        this.view = view;
        this.produkService = produkService;
        this.transaksiService = transaksiService;
        this.kasir = kasir;
        this.view.setController(this);
        loadProduk();
    }

    public void start() {
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });
    }

    private void loadProduk() {
        List<Produk> list = produkService.getAllProduk();
        view.showProduk(list);
    }

    public void tambahKeKeranjang(Produk produk, int qty) {
        if (produk.getStok() < qty) {
            view.showError("Stok tidak mencukupi!");
            return;
        }

        boolean found = false;
        for (ItemCart item : keranjang) {
            if (item.getProduk().getId() == produk.getId()) {
                if (item.getQty() + qty > produk.getStok()) {
                    view.showError("Total kuantitas di keranjang melebihi stok yang tersedia!");
                    return;
                }
                item.setQty(item.getQty() + qty);
                found = true;
                break;
            }
        }

        if (!found) {
            keranjang.add(new ItemCart(produk, qty));
        }

        view.updateKeranjang(keranjang);
        hitungTotal();
    }

    public void hitungTotal() {
        double total = 0;
        for (ItemCart item : keranjang) {
            total += item.getSubtotal();
        }
        view.setTotalBelanja(total);
    }

    public void prosesPembayaran(double uangDiterima, String paymentMethod) {
        if (keranjang.isEmpty()) {
            view.showError("Keranjang masih kosong!");
            return;
        }

        double totalBelanja = 0;
        for (ItemCart item : keranjang) {
            totalBelanja += item.getSubtotal();
        }

        Transaksi transaksi = new Transaksi();
        transaksi.setKasirId(kasir.getId());
        transaksi.setTotalHarga(totalBelanja);
        transaksi.setTotalBayar(totalBelanja);
        transaksi.setUangDiterima(uangDiterima);
        transaksi.setPaymentMethod(paymentMethod);
        transaksi.setItems(new ArrayList<>(keranjang));

        try {
            transaksiService.prosesPembayaran(transaksi);
            view.showMessage("Pembayaran sukses!\nKembalian: Rp" + transaksi.getKembalian());
            keranjang.clear();
            view.updateKeranjang(keranjang);
            view.resetFormBayar();
            hitungTotal();
            loadProduk(); // Refresh stok
        } catch (Exception e) {
            view.showError("Gagal memproses pembayaran: " + e.getMessage());
        }
    }
}
