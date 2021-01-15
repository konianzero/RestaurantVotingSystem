package org.restaurant.voting.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.repository.DishRepository;

import static org.restaurant.voting.util.ValidationUtil.*;

@Service
public class DishService {

    private final DishRepository repository;

    public DishService(DishRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Dish must be not null");
        return repository.save(dish, restaurantId);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public List<Dish> createAllForRestaurant(List<Dish> dishes, int restaurantId) {
        Assert.notNull(dishes, "Dishes must be not null");
        return repository.save(dishes, restaurantId);
    }

    public Dish get(int id, int restaurantId) {
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    public Dish getWithRestaurant(int id, int restaurantId) {
        return checkNotFoundWithId(repository.getWithRestaurant(id, restaurantId), id);
    }

    @Cacheable("dishes")
    public List<Dish> getAll() {
        return repository.getAll();
    }

    @Cacheable("dishes")
    public List<Dish> getAllByRestaurant(int restaurantId) {
        return repository.getAllByRestaurant(restaurantId);
    }

    @Cacheable("dishes")
    public List<Dish> getAllByRestaurantAndDate(int restaurantId, LocalDate date) {
        Assert.notNull(date, "Date must be not null");
        return repository.getAllByRestaurantAndDate(restaurantId, date);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Dish must be not null");
        checkNotFoundWithId(repository.save(dish, restaurantId), dish.id());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(repository.delete(id, restaurantId), id);
    }
}
