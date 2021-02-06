package org.restaurant.voting.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.repository.DishRepository;
import org.restaurant.voting.to.DishTo;

import static org.restaurant.voting.util.DishUtil.createNewFromTo;
import static org.restaurant.voting.util.ValidationUtil.*;

@Service
public class DishService {

    private final DishRepository repository;

    public DishService(DishRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public Dish create(DishTo dishTo) {
        Assert.notNull(dishTo, "Dish must be not null");
        dishTo.setDate(LocalDate.now());
        Dish dish = createNewFromTo(dishTo);
        return repository.save(dish, dishTo.getRestaurantId());
    }

    public Dish get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public Dish getWithRestaurant(int id) {
        return checkNotFoundWithId(repository.getWithRestaurant(id), id);
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
    public void update(DishTo dishTo) {
        Assert.notNull(dishTo, "Dish must be not null");
        Dish dish = createNewFromTo(dishTo);
        checkNotFoundWithId(repository.save(dish, dishTo.getRestaurantId()), dish.id());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }
}
