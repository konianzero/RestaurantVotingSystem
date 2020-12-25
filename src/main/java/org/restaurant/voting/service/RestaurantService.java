package org.restaurant.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.repository.RestaurantRepository;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must be not null");
        return repository.save(restaurant);
    }

    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must be not null");
        repository.save(restaurant);
    }

    public Restaurant get(int id) {
        return repository.get(id);
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    public void delete(int id) {
        repository.delete(id);
    }
}
