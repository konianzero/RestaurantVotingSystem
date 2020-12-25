package org.restaurant.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.repository.DishRepository;

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
        repository.save(dish, restaurantId);
    }

    public List<Dish> getAll(int restaurantId) {
        return repository.getAll(restaurantId);
    }

    public Dish get(int id, int restaurantId) {
        return repository.get(id, restaurantId);
    }

    public void delete(int id, int restaurantId) {
        repository.delete(id, restaurantId);
    }

    public Dish getWithRestaurant(int id) {
        return repository.getWithRestaurant(id);
    }

    public List<Dish> getAllByDate(int restaurantId, LocalDate date) {
        Assert.notNull(date, "Date must be not null");
        return repository.getAllByDate(restaurantId, date);
    }
}
