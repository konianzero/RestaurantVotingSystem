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
        if (preSaveCheck(dish)) {
            return null;
        }
        dish.setRestaurant(crudRestaurantRepository.getOne(restaurantId));
        return crudDishRepository.save(dish);
    }

    private boolean preSaveCheck(Dish dish) {
        return !dish.isNew() && get(dish.getId()) == null;
    }

    public Dish get(int dishId) {
        return crudDishRepository.findById(dishId)
                                 .orElse(null);
    }

    public Dish getWithRestaurant(int id) {
        return crudDishRepository.getWithRestaurant(id);
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

    public boolean delete(int dishId) {
        return crudDishRepository.delete(dishId) != 0;
    }
}
