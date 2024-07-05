package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDTO;

import java.util.Optional;

public interface IUserService extends ICrudService<User> {

    Optional<User> getUserByUserName(String username);

    User updateWithDto(int id, UserDTO userUpdated);

    User saveWithDto(UserDTO dto);

}
