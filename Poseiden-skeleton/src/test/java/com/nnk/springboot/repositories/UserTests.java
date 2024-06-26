package com.nnk.springboot.repositories;

import com.nnk.springboot.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {
    @Autowired
    private UserRepository repository;

    @Test
    public void bidListTest() {

        User user = new User("Joey", "myValidPassword@1", "Joe Doe", "USER");

        // Save
        user = repository.save(user);
        Assert.assertNotNull(user.getId());
        Assert.assertEquals("Joey", user.getUsername());

        // Update
        user.setUsername("Joseph");
        user = repository.save(user);
        assertEquals("Joseph", user.getUsername());

        // Find
        List<User> listResult = repository.findAll();
        Assert.assertFalse(listResult.isEmpty());

        // Find by Id
        Optional<User> optional = repository.findById(user.getId());
        Assert.assertTrue(optional.isPresent());
        Assert.assertEquals("USER", user.getRole());

        // Delete
        Integer id = user.getId();
        repository.delete(user);
        Optional<User> list = repository.findById(id);
        Assert.assertFalse(list.isPresent());
    }
}
