package org.restaurant.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import org.restaurant.voting.model.User;
import org.restaurant.voting.repository.UserRepository;
import org.restaurant.voting.to.UserTo;
import org.restaurant.voting.util.UserUtil;

import static org.restaurant.voting.util.ValidationUtil.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        Assert.notNull(user, "User must be not null");
        return userRepository.save(user);
    }

    public void delete(int id) {
        checkNotFoundWithId(userRepository.delete(id), id);
    }

    public User get(int id) {
        return checkNotFoundWithId(userRepository.get(id), id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "Email must be not null");
        return checkNotFound(userRepository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public void update(User user) {
        Assert.notNull(user, "User must be not null");
        userRepository.save(user);
    }

    @Transactional
    public void update(UserTo userTo) {
        Assert.notNull(userTo, "UserTo must be not null");
        User user = get(userTo.getId());
        User updated = UserUtil.updateFromTo(user, userTo);
        userRepository.save(updated);
    }
}
