package org.restaurant.voting.web.controller.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.restaurant.voting.model.User;
import org.restaurant.voting.service.UserService;
import org.restaurant.voting.to.UserTo;

import static org.restaurant.voting.util.UserUtil.createNewFromTo;
import static org.restaurant.voting.util.ValidationUtil.assureIdConsistent;
import static org.restaurant.voting.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {

    protected Logger log = LoggerFactory.getLogger(AbstractUserController.class);

    @Autowired
    protected UserService service;

    public User create(UserTo userTo) {
        log.info("Create {}", userTo);
        checkNew(userTo);
        return service.create(createNewFromTo(userTo));
    }

    public User create(User user) {
        log.info("Create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public User get(int id) {
        log.info("Get user {}", id);
        return service.get(id);
    }

    public User getByEmail(String email) {
        log.info("Get user by Email {}", email);
        return service.getByEmail(email);
    }

    public List<User> getAll() {
        log.info("Get all users");
        return service.getAll();
    }

    public void update(UserTo userTo, int id) {
        log.info("Update {}", userTo);
        assureIdConsistent(userTo, id);
        service.update(userTo);
    }

    public void update(User user, int id) {
        log.info("Update {}", user);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public void delete(int id) {
        log.info("Delete user {}", id);
        service.delete(id);
    }
}
