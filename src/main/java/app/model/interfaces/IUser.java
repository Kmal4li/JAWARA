package app.model.interfaces;

/**
 * Interface IUser sesuai class diagram.
 * Mendefinisikan kontrak untuk semua tipe user (Admin, Kasir).
 * Method: login(), logout(), getNama(), setNama(), getRole()
 */
public interface IUser {
    boolean login(String username, String password);
    void logout();
    String getNama();
    void setNama(String nama);
    String getRole();
}
