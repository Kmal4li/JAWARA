package app.model;

import app.model.interfaces.IUser;

public class User implements IUser {
    protected int id;
    protected String nama;
    protected String username;
    protected String password;
    protected String role;

    public User() {}

    public User(int id, String nama, String username, String password, String role) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Implementasi IUser
    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    public void logout() {
        System.out.println("User " + nama + " telah logout.");
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", nama='" + nama + "', username='" + username + "', role='" + role + "'}";
    }
}

