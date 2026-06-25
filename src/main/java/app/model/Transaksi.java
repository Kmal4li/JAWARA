package app.model;

import java.util.Date;
import java.util.List;

public class Transaksi {
    private int id;
    private int kasirId;
    private Date tanggal;
    private double totalHarga;
    private double diskon;
    private double totalBayar;
    private double uangDiterima;
    private double kembalian;
    private String paymentMethod;
    private String status;
    private List<ItemCart> items; 

    public Transaksi() {}

    public int getId() { 
        return id; 
    }
    public void setId(int id) {
         this.id = id; 
        }
    public int getKasirId() {
         return kasirId; 
        }
    public void setKasirId(int kasirId) { 
        this.kasirId = kasirId;
     }
    public Date getTanggal() { 
        return tanggal; 
    }
    public void setTanggal(Date tanggal) { 
        this.tanggal = tanggal; 
    }
    public double getTotalHarga() { 
        return totalHarga; 
    }
    public void setTotalHarga(double totalHarga) {
         this.totalHarga = totalHarga;
         }
    public double getDiskon() {
        return diskon;
     }
    public void setDiskon(double diskon) {
         this.diskon = diskon;
         }
    public double getTotalBayar() {
         return totalBayar; 
        }
    public void setTotalBayar(double totalBayar) { 
        this.totalBayar = totalBayar;
     }
    public double getUangDiterima() { 
        return uangDiterima; 
    }
    public void setUangDiterima(double uangDiterima) {
         this.uangDiterima = uangDiterima; 
        }
    public double getKembalian() {
         return kembalian; 
        }
    public void setKembalian(double kembalian) {
         this.kembalian = kembalian;
         }
    public String getPaymentMethod() { 
        return paymentMethod;
     }
    public void setPaymentMethod(String paymentMethod) { 
        this.paymentMethod = paymentMethod;
     }
    public String getStatus() { 
        return status;
     }
    public void setStatus(String status) { 
        this.status = status;
     }
    public List<ItemCart> getItems() {
         return items;
         }
    public void setItems(List<ItemCart> items) { 
        this.items = items; 
    }

    private String kasirNama;
    public String getKasirNama() { 
        return kasirNama; 
    }
    public void setKasirNama(String kasirNama) { 
        this.kasirNama = kasirNama;
     }
}
