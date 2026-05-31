package app.model;

public class Kasir extends User {
    public Kasir() {
        super();
        this.role = "Kasir";
    }

    public Kasir(int id, String nama, String username, String password) {
        super(id, nama, username, password, "Kasir");
    }
}
