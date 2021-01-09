package org.restaurant.voting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import org.restaurant.voting.model.Dish;

@Repository
public class DishRepository {
    private static final Sort SORT_BY_NAME = Sort.by(Sort.Direction.ASC, "date");

    private final CrudDishRepository crudDishRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public DishRepository(CrudDishRepository crudDishRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudDishRepository = crudDishRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Transactional
    public Dish save(Dish dish, int restaurantId) {
        if (preSaveCheck(dish, restaurantId)) {
            return null;
        }
        dish.setRestaurant(crudRestaurantRepository.getOne(restaurantId));
        return crudDishRepository.save(dish);
    }

    @Transactional
    public List<Dish> save(List<Dish> dishes, int restaurantId) {
        if (dishes.stream().anyMatch(dish -> preSaveCheck(dish, restaurantId))) {
            return null;
        }
        dishes.forEach(dish -> dish.setRestaurant(crudRestaurantRepository.getOne(restaurantId)));
        return crudDishRepository.saveAll(dishes);
    }

    private boolean preSaveCheck(Dish dish, int restaurantId) {
        return !dish.isNew() && get(dish.getId(), restaurantId) == null;
    }

    public Dish get(int dishId, int restaurantId) {
        return crudDishRepository.findById(dishId)
                                 .filter(dish -> dish.getRestaurant().getId() == restaurantId)
                                 .orElse(null);
    }

    public Dish getWithRestaurant(int id, int restaurantId) {
        return crudDishRepository.getWithRestaurant(id, restaurantId);
    }

    public List<Dish> getAll() {
        return crudDishRepository.findAll(SORT_BY_NAME);
    }

    public List<Dish> getAllByRestaurant(int restaurantId) {
        return crudDishRepository.getAllByRestaurant(restaurantId);
    }

    public List<Dish> getAllByRestaurantAndDate(int restaurantId, LocalDate date) {
        return crudDishRepository.getAllByRestaurantAndDate(restaurantId, date);
    }

    public boolean delete(int dishId, int restaurantId) {
        return crudDishRepository.delete(dishId, restaurantId) != 0;
    }
}
