package app.controller;

import app.model.User;
import app.service.AuthService;
import app.view.auth.LoginView;
import javax.swing.SwingUtilities;

public class LoginController {
    private LoginView view;
    private AuthService authService;

    public LoginController(LoginView view, AuthService authService) {
        this.view = view;
        this.authService = authService;
        this.view.setController(this);
    }

    public void start() {
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);
        });
    }

    public void handleLogin(String username, String password) {
        try {
            User user = authService.login(username, password);
            view.showMessage("Login berhasil! Selamat datang " + user.getNama() + " (" + user.getRole() + ")");
            
            // Tutup window login
            view.dispose();
            
            // Buka Dashboard sesuai Role
            if ("Kasir".equalsIgnoreCase(user.getRole())) {
                app.service.ProdukService ps = new app.service.ProdukService();
                app.service.TransaksiService ts = new app.service.TransaksiService();
                app.view.kasir.KasirView kv = new app.view.kasir.KasirView();
                app.controller.KasirController kc = new app.controller.KasirController(kv, ps, ts, user);
                kc.start();
            } else {
                // Untuk admin nanti akan dibuatkan AdminView
                view.showMessage("Dashboard Admin belum tersedia pada fase ini.");
                System.exit(0);
            }
            
        } catch (Exception e) {
            view.showMessage("Login gagal: " + e.getMessage());
        }
    }
}
