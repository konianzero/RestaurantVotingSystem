package org.restaurant.voting.service;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.restaurant.voting.model.User;
import org.restaurant.voting.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.UserTestData.*;

class UserServiceTest extends AbstractServiceTest {
    @Autowired
    private UserService service;

    @Order(1)
    @Test
    void get() {
        User user = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
    }

    @Order(2)
    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Order(3)
    @Test
    void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, ADMIN);
    }

    @Order(4)
    @Test
    void getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, ADMIN, USER);
    }

    @Order(5)
    @Test
    void update() {
        User updated = getUpdated();
        service.update(updated);
        USER_MATCHER.assertMatch(service.get(ADMIN_ID), getUpdated());
    }

    @Order(6)
    @Test
    void create() {
        User newUser = getNew();
        User created = service.create(newUser);
        int newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Order(7)
    @Test
    void delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Order(8)
    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }
}