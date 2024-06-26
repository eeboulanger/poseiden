package com.nnk.springboot.security;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@SpringBootTest
@AutoConfigureMockMvc
public class LogInTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository repository;
    private User user;

    @BeforeEach
    public void setUp() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user = repository.save(new User("Joe", encoder.encode("validPassword@11"), "Joe Doe", "USER"));
    }

    @AfterEach
    public void tearDown() {
        repository.deleteById(user.getId());
    }

    @Test
    public void userLoginTest() throws Exception {
        mockMvc.perform(formLogin("/app/login")
                        .user("Joe")
                        .password("validPassword@11"))
                .andExpect(authenticated().withUsername("Joe").withRoles("USER"));
    }

    @Test
    public void userLoginFailed() throws Exception {
        mockMvc.perform(formLogin("/app/login").user("Joe").password("wron@11gpassword"))
                .andExpect(unauthenticated());
    }
}
