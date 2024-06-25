package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(controllers = UserController.class)
@WithMockUser(roles = "USER")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IUserService userService;
    @InjectMocks
    private UserController controller;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Joey", "password", "Joe Doe", "USER");
    }

    @Test
    public void homeTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user));
        mockMvc.perform(get("/user/list")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(1)))
                .andExpect(model().attribute("users", contains(
                        allOf(
                                hasProperty("username", is(user.getUsername())),
                                hasProperty("fullname", is(user.getFullname())),
                                hasProperty("password", is(user.getPassword())),
                                hasProperty("role", is(user.getRole()))
                        )
                )));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void addUserTest() throws Exception {
        mockMvc.perform(get("/user/add")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/add"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void validateTest() throws Exception {
        when((userService.createUser(ArgumentMatchers.any(User.class)))).thenReturn(new User());
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "Jane")
                        .param("password", "myPassword")
                        .param("fullname", "Jane Doe")
                        .param("role", "USER")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).createUser(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Given the user has field errors, when submitting form, then don't save and return list")
    public void whenInvalidUser_thenDisplayFieldErrors() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "")
                        .param("password", "")
                        .param("fullname", "")
                        .param("role", "")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/add"))
                .andExpect(model().errorCount(4))//Not blank
                .andExpect(model().attributeHasFieldErrors("user", "username", "password", "fullname", "role"));

        verify(userService, never()).createUser(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Given there's a user with the id, then add to model")
    public void updateUserTest() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", hasProperty("username", is(user.getUsername()))))
                .andExpect(model().attribute("user", hasProperty("password", is(user.getPassword()))))
                .andExpect(model().attribute("user", hasProperty("fullname", is(user.getFullname()))))
                .andExpect(model().attribute("user", hasProperty("role", is(user.getRole()))));


        verify(userService, times(1)).getUserById(1);
    }

    @Test
    @DisplayName("Given there's no user with the id, then don't add  to model")
    public void givenNoUserWithId_whenUpdate_thenDontAddToModel() throws Exception {
        when(userService.getUserById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/update/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

        verify(userService, times(1)).getUserById(1);
    }

    @Test
    @DisplayName("Given fields are valid, update user in database")
    public void updateUserSuccessTest() throws Exception {
        mockMvc.perform(post("/user/update/{id}", 1)
                        .with(csrf())
                        .param("username", "Jane")
                        .param("password", "myPassword")
                        .param("fullname", "Jane Doe")
                        .param("role", "USER")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
        verify(userService, times(1)).updateUser(eq(1), ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("Given fields have errors, don't update and display errors")
    public void updateUserFailsTest() throws Exception {
        mockMvc.perform(post("/user/update/{id}", 1)
                        .param("username", "")
                        .param("password", "")
                        .param("fullname", "")
                        .param("role", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().errorCount(4)) //Not blank
                .andExpect(model().attributeHasFieldErrors("user", "username", "password", "fullname", "role"))
                .andExpect(view().name("/user/update"));

        verify(userService, never()).updateUser(eq(1), ArgumentMatchers.any(User.class));
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(get("/user/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @DisplayName("Given there's no user with the id, then redirect to list")
    public void deleteUserFailsTest() throws Exception {
        doThrow(new EntityNotFoundException()).when(userService).deleteUser(1);

        mockMvc.perform(get("/user/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).deleteUser(1);
    }
}
