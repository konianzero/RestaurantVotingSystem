package org.restaurant.voting.service;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.repository.CrudDishRepository;
import org.restaurant.voting.repository.CrudRestaurantRepository;
import org.restaurant.voting.to.DishTo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static org.restaurant.voting.util.DishUtil.createNewFromTo;
import static org.restaurant.voting.util.validation.ValidationUtil.checkNotFoundWithId;

@Service
public class DishService {

    private final CrudDishRepository crudDishRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public DishService(CrudDishRepository crudDishRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudDishRepository = crudDishRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public Dish create(DishTo dishTo) {
        Assert.notNull(dishTo, "Dish must be not null");
        dishTo.setDate(LocalDate.now());
        Dish dish = createNewFromTo(dishTo);

        return save(dish, dishTo.getRestaurantId());
    }

    @Transactional
    protected Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.getId()) == null) {
            return null;
        }
        dish.setRestaurant(checkNotFoundWithId(crudRestaurantRepository.findById(restaurantId), restaurantId));
        return crudDishRepository.save(dish);
    }

    public Dish get(int id) {
        return checkNotFoundWithId(crudDishRepository.findById(id), id);
    }

    public Dish getWithRestaurant(int id) {
        return checkNotFoundWithId(crudDishRepository.getWithRestaurant(id), id);
    }

    public List<Dish> getAllByRestaurant(int restaurantId) {
        return crudDishRepository.getAllByRestaurant(restaurantId);
    }

    public List<Dish> getAllByRestaurantAndDate(int restaurantId, LocalDate date) {
        Assert.notNull(date, "Date must be not null");
        return crudDishRepository.getAllByRestaurantAndDate(restaurantId, date);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void update(DishTo dishTo) {
        Assert.notNull(dishTo, "Dish must be not null");
        Dish dish = createNewFromTo(dishTo);
        checkNotFoundWithId(save(dish, dishTo.getRestaurantId()), dish.id());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudDishRepository.delete(id) != 0, id);
    }
}
