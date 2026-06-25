package app.model.interfaces;


public interface IUser {
    boolean login(String username, String password);
    void logout();
    String getNama();
    void setNama(String nama);
    String getRole();
}
