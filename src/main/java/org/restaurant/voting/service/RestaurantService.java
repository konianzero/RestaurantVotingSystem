package org.restaurant.voting.service;

import org.restaurant.voting.to.RestaurantTo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.repository.RestaurantRepository;

import static org.restaurant.voting.util.RestaurantUtil.createNewFromTo;
import static org.restaurant.voting.util.ValidationUtil.*;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public Restaurant create(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "Restaurant must be not null");
        return repository.save(createNewFromTo(restaurantTo));
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public Restaurant getWithMenu(int id) {
        return checkNotFoundWithId(repository.getWithDishes(id), id);
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void update(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "Restaurant must be not null");
        checkNotFoundWithId(repository.save(createNewFromTo(restaurantTo)), restaurantTo.getId());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }
}
