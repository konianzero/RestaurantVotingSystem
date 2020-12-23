package org.restaurant.voting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.restaurant.voting.model.Restaurant;

@Repository
public class RestaurantRepository {
    private static final Sort SORT_BY_NAME = Sort.by(Sort.Direction.DESC, "name");

    private final CrudRestaurantRepository repository;

    public RestaurantRepository(CrudRestaurantRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public Restaurant get(int id) {
        return repository.findById(id)
                         .orElse(null);
    }

    public Restaurant getWithDishes(int id) {
        return repository.getWithDishes(id);
    }

    public List<Restaurant> getAll() {
        return repository.findAll(SORT_BY_NAME);
    }

    public boolean delete(int id) {
        return repository.delete(id) != 0;
    }
}
