package org.restaurant.voting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.restaurant.voting.model.User;

@Repository
public class UserRepository {
    private static final Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "registered");

    private final CrudUserRepository crudRepository;

    public UserRepository(CrudUserRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Transactional
    public User save(User user) {
        return crudRepository.save(user);
    }

    public User get(int id) {
        return crudRepository.findById(id)
                             .orElse(null);
    }

    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    public List<User> getAll() {
        return crudRepository.findAll(SORT_BY_DATE);
    }

    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }
}