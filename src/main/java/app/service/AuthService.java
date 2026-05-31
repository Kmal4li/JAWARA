package app.service;

import app.model.User;
import app.repository.UserRepository;

public class AuthService {
    private UserRepository userRepository;
    
    public AuthService() {
        this.userRepository = new UserRepository();
    }
    
    public User login(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("Password cannot be empty");
        }
        
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }
        
        if (!user.getPassword().equals(password)) {
            throw new Exception("Incorrect password");
        }
        
        return user;
    }
}
