package com.nnk.springboot.services;


import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDTO;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User createUser(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFullname(dto.getFullname());
        user.setRole(dto.getRole());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(dto.getPassword()));
        return repository.save(user);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return repository.findById(id);
    }

    @Override
    public User updateUser(int id, UserDTO userUpdated) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return repository.findById(id).map(user -> {
                    user.setUsername(userUpdated.getUsername());
                    user.setFullname(userUpdated.getFullname());
                    user.setRole(userUpdated.getRole());
                    user.setPassword(encoder.encode(userUpdated.getPassword()));
                    return repository.save(user);
                })
                .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    }

    @Override
    public void deleteUser(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Entity not found");
        }
    }

    @Override
    public Optional<User> getUserByUserName(String username) {
        return repository.findByUsername(username);
    }
}
