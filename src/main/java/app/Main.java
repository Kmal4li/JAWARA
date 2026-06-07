package app;

import app.controller.LoginController;
import app.service.AuthService;
import app.view.auth.LoginView;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        AuthService authService = new AuthService();
        LoginView loginView = new LoginView();
        LoginController loginController = new LoginController(loginView, authService);
        
        loginController.start();
    }
}
