package org.restaurant.voting.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import org.restaurant.voting.model.Dish;

@Repository
public class DishRepository {

    private final CrudDishRepository crudDishRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public DishRepository(CrudDishRepository crudDishRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudDishRepository = crudDishRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Transactional
    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.getId(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurant(crudRestaurantRepository.getOne(restaurantId));
        return crudDishRepository.save(dish);
    }

    @Transactional
    public List<Dish> save(List<Dish> dishes) {
        return crudDishRepository.saveAll(dishes);
    }

    public Dish get(int dishId, int restaurantId) {
        return crudDishRepository.findById(dishId)
                                 .filter(dish -> dish.getRestaurant().getId() == restaurantId)
                                 .orElse(null);
    }

    public Dish getWithRestaurant(int id) {
        return crudDishRepository.getWithRestaurant(id);
    }

    public List<Dish> getAll(int restaurantId) {
        return crudDishRepository.getAll(restaurantId);
    }

    public List<Dish> getAllByDate(int restaurantId, LocalDate date) {
        return crudDishRepository.getAllByDate(restaurantId, date);
    }

    public boolean delete(int dishId, int restaurantId) {
        return crudDishRepository.delete(dishId, restaurantId) != 0;
    }
}
