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
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User saveWithDto(UserDTO dto) {
        Optional<User> optional = getUserByUserName(dto.getUsername());
        if (optional.isEmpty()) {
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setFullname(dto.getFullname());
            user.setRole(dto.getRole());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(dto.getPassword()));
            return save(user);
        } else {
            throw new IllegalArgumentException("Username already in use");
        }
    }

    @Override
    public Optional<User> getById(int id) {
        return repository.findById(id);
    }

    @Override
    public User update(int id, User user) {
        return repository.save(user);
    }

    @Override
    public User updateWithDto(int id, UserDTO userUpdated) {
        Optional<User> optional = getUserByUserName(userUpdated.getUsername());
        if (optional.isEmpty()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            return repository.findById(id).map(user -> {
                        user.setUsername(userUpdated.getUsername());
                        user.setFullname(userUpdated.getFullname());
                        user.setRole(userUpdated.getRole());
                        user.setPassword(encoder.encode(userUpdated.getPassword()));
                        return update(id, user);
                    })
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        } else {
            throw new IllegalArgumentException("Username already in use");
        }
    }

    @Override
    public void delete(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Entity not found");
        }
    }

    public Optional<User> getUserByUserName(String username) {
        return repository.findByUsername(username);
    }
}
