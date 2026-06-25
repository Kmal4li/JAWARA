package app.model;

/**
 * Class Struk sesuai class diagram.
 * Constructor: Struk(tanggal: String, kembalian: int)
 * Method: cetakStruk()
 */
public class Struk {
    private String tanggal;
    private int kembalian;

    public Struk() {}

    public Struk(String tanggal, int kembalian) {
        this.tanggal = tanggal;
        this.kembalian = kembalian;
    }

    public String getTanggal() { 
        return tanggal; 
    }
    public void setTanggal(String tanggal) { 
        this.tanggal = tanggal; 
    }

    public int getKembalian() { 
        return kembalian; 
    }
    public void setKembalian(int kembalian) { 
        this.kembalian = kembalian; 
    }

    
    public void cetakStruk() {
        System.out.println("================================");
        System.out.println("         JAWARA POS");
        System.out.println("================================");
        System.out.println("Tanggal   : " + tanggal);
        System.out.println("Kembalian : Rp" + kembalian);
        System.out.println("================================");
        System.out.println("    Terima Kasih!");
        System.out.println("================================");
    }

    @Override
    public String toString() {
        return "Struk{tanggal='" + tanggal + "', kembalian=" + kembalian + "}";
    }
}
