package app.service;

import app.model.User;
import app.repository.UserRepository;
import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public void addUser(User user) throws Exception {
        validateUser(user);
        
        // Cek username unik
        User existing = userRepository.findByUsername(user.getUsername());
        if (existing != null) {
            throw new Exception("Username already exists");
        }
        
        userRepository.insert(user);
    }

    public void updateUser(User user) throws Exception {
        validateUser(user);
        
        // Cek username unik jika diubah
        User existing = userRepository.findByUsername(user.getUsername());
        if (existing != null && existing.getId() != user.getId()) {
            throw new Exception("Username already exists");
        }
        
        userRepository.update(user);
    }

    public void deleteUser(int id) throws Exception {
        userRepository.delete(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void validateUser(User user) throws Exception {
        if (user.getNama() == null || user.getNama().trim().isEmpty()) {
            throw new Exception("Name is required");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new Exception("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new Exception("Password is required");
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            throw new Exception("Role is required");
        }
    }
}
