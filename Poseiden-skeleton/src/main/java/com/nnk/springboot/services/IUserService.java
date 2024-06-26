package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getAllUsers();

    User createUser(UserDTO userdto);

    Optional<User> getUserById(int id);

    User updateUser(int id, UserDTO user);

    void deleteUser(int id);

    Optional<User> getUserByUserName(String username);
}
