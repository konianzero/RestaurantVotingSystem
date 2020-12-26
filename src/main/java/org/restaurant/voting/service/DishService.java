package org.restaurant.voting.service;

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

    public Dish create(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Dish must be not null");
        return repository.save(dish, restaurantId);
    }

    public List<Dish> create(List<Dish> dishes) {
        Assert.notNull(dishes, "Dishes must be not null");
        return repository.save(dishes);
    }

    public void update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Meal must be not null");
        checkNotFoundWithId(repository.save(dish, restaurantId), dish.id());
    }

    public List<Dish> getAll(int restaurantId) {
        return repository.getAll(restaurantId);
    }

    public Dish get(int id, int restaurantId) {
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    public void delete(int id, int restaurantId) {
        checkNotFoundWithId(repository.delete(id, restaurantId), id);
    }

    public Dish getWithRestaurant(int id) {
        return checkNotFoundWithId(repository.getWithRestaurant(id), id);
    }

    public List<Dish> getAllByDate(int restaurantId, LocalDate date) {
        Assert.notNull(date, "Date must be not null");
        return repository.getAllByDate(restaurantId, date);
    }
}
