package org.restaurant.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import org.restaurant.voting.model.User;
import org.restaurant.voting.repository.UserRepository;

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
        userRepository.delete(id);
    }

    public User get(int id) {
        return userRepository.get(id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "Email must be not null");
        return userRepository.getByEmail(email);
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

    public void update(User user) {
        Assert.notNull(user, "User must be not null");
        userRepository.save(user);
    }
}
