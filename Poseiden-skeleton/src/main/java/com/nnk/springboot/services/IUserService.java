package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getAllUsers();

    User createUser(User user);

    Optional<User> getUserById(int id);

    User updateUser(int id, User user);

    void deleteUser(int id);
}
