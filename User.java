


public class User {

    
    protected int    id;
    protected String nama;
    protected String password;
    protected String role;

    
    public User() {}

    public User(int id, String nama, String password, String role) {
        this.id       = id;
        this.nama     = nama;
        this.password = password;
        this.role     = role;
    }

    public boolean login(String inputPassword) {
        if (this.password != null && this.password.equals(inputPassword)) {
            System.out.println("[LOGIN]  Berhasil — " + nama + " (" + role + ") masuk ke sistem.");
            return true;
        }
        System.out.println("[LOGIN]  Gagal — password salah untuk pengguna: " + nama);
        return false;
    }
    public void logout() {
        System.out.println("[LOGOUT] " + nama + " (" + role + ") keluar dari sistem.");
    }
    public void kelolaVoucher() {
        System.out.println("[VOUCHER] " + nama + " mengakses pengelolaan voucher.");
    }
    public int    getId()       { return id; }
    public void   setId(int id) { this.id = id; }

    public String getNama()           { return nama; }
    public void   setNama(String nama){ this.nama = nama; }

    public String getPassword()               { return password; }
    public void   setPassword(String password){ this.password = password; }

    public String getRole()            { return role; }
    public void   setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{id=" + id + ", nama='" + nama + "', role='" + role + "'}";
    }
}
