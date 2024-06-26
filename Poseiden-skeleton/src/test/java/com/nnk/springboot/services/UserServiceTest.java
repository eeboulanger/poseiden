package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDTO;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService service;
    private UserDTO dto;
    private User user;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    public void setUp() {
        user = new User("Joey", "validPass@11", "Joe Doe", "User");
        user.setId(1);
        encoder = new BCryptPasswordEncoder();
    }

    @Test
    public void getAllUsersTest() {
        when(repository.findAll()).thenReturn(List.of(user));

        List<User> result = service.getAllUsers();

        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();

    }

    @Test
    public void createUser() {
        dto= new UserDTO("Joey", "validPass@11", "Joe Doe", "User");
        when(repository.save(any(User.class))).thenReturn(user);

        User result = service.createUser(dto);

        verify(repository, times(1)).save(any(User.class));
        assertNotNull(result);
    }

    @Test
    public void getUserByIdTest() {
        when(repository.findById(1)).thenReturn(Optional.ofNullable(user));

        Optional<User> result = service.getUserById(1);

        verify(repository, times(1)).findById(1);
        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void updateUserTest() {
        UserDTO dto = new UserDTO("updated username", "newPassword", "updated fullname", "new role");
        when(repository.findById(1)).thenReturn(Optional.ofNullable(user));
        when(repository.save(user)).thenReturn(user);

        User result = service.updateUser(1, dto);

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(user);
        assertEquals(dto.getUsername(), result.getUsername());
        assertEquals(dto.getFullname(), result.getFullname());
        assertEquals(dto.getRole(), result.getRole());
        assertTrue(encoder.matches(dto.getPassword(), result.getPassword()));
    }

    @Test
    @DisplayName("When no user found with id, then update should fail and throw exception")
    public void updateUserFailsTest() {
        UserDTO dto = new UserDTO("updated username", "newPassword", "updated fullname", "new role");
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateUser(1, dto));

        verify(repository, times(1)).findById(1);
        verify(repository, never()).save(any(User.class));
    }

    @Test
    public void deleteUserTest() {
        when(repository.existsById(1)).thenReturn(true);

        service.deleteUser(1);

        verify(repository, times(1)).existsById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("When no user found with id, then delete should fail and throw exception")
    public void deleteUserFailsTest() {
        when(repository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.deleteUser(1));

        verify(repository, times(1)).existsById(1);
        verify(repository, never()).deleteById(anyInt());
    }
}
