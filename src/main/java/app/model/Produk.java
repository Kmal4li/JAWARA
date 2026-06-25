package app.model;

public class Produk {
    private int id;
    private String namaProduk;
    private double hargaBeli;
    private double hargaJual;
    private int stok;
    private String barcode;
    private int categoryId;

    public Produk() {}

    public Produk(int id, String namaProduk, double hargaBeli, double hargaJual, int stok, String barcode, int categoryId) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
        this.stok = stok;
        this.barcode = barcode;
        this.categoryId = categoryId;
    }

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }
    public String getNamaProduk() { 
        return namaProduk; 
    }
    public void setNamaProduk(String namaProduk) { 
        this.namaProduk = namaProduk; }
    public double getHargaBeli() { 
        return hargaBeli; 
    }
    public void setHargaBeli(double hargaBeli) {
         this.hargaBeli = hargaBeli; 
        }
    public double getHargaJual() { 
        return hargaJual;
     }
    public void setHargaJual(double hargaJual) {
         this.hargaJual = hargaJual; 
        }
    public int getStok() { 
        return stok; 
    }
    public void setStok(int stok) { 
        this.stok = stok; 
    }
    public String getBarcode() { 
        return barcode; 
    }
    public void setBarcode(String barcode) {
         this.barcode = barcode;
         }
    public int getCategoryId() { 
        return categoryId; 
    }
    public void setCategoryId(int categoryId) {
         this.categoryId = categoryId; 
        }

    @Override
    public String toString() {
        return "Produk{id=" + id + ", nama='" + namaProduk + "', stok=" + stok + "}";
    }
}
