package app.service;

import app.model.Produk;
import app.repository.ProdukRepository;
import java.util.List;

public class ProdukService {
    private ProdukRepository produkRepository;

    public ProdukService() {
        this.produkRepository = new ProdukRepository();
    }

    public void addProduk(Produk produk) throws Exception {
        if (produk.getNamaProduk() == null || produk.getNamaProduk().trim().isEmpty()) {
            throw new Exception("Product name is required");
        }
        if (produk.getHargaJual() < 0 || produk.getHargaBeli() < 0) {
            throw new Exception("Price cannot be negative");
        }
        if (produk.getStok() < 0) {
            throw new Exception("Stock cannot be negative");
        }
        
        produkRepository.insert(produk);
    }
    
    public List<Produk> getAllProduk() {
        return produkRepository.findAll();
    }
}
