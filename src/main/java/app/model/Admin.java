package app.model;

public class Admin extends User {
    public Admin() {
        super();
        this.role = "Admin";
    }

    public Admin(int id, String nama, String username, String password) {
        super(id, nama, username, password, "Admin");
    }
}
