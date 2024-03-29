package org.restaurant.voting.service;

import lombok.RequiredArgsConstructor;
import org.restaurant.voting.model.Dish;
import org.restaurant.voting.repository.CrudDishRepository;
import org.restaurant.voting.repository.CrudRestaurantRepository;
import org.restaurant.voting.to.DishTo;
import org.restaurant.voting.util.mapper.DishMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static org.restaurant.voting.util.validation.ValidationUtil.checkNotFoundWithId;

@Service
@RequiredArgsConstructor
public class DishService {

    private final CrudDishRepository crudDishRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;
    private final DishMapper mapper;

    @CacheEvict(value = "dishes", allEntries = true)
    @Transactional
    public Dish create(DishTo dishTo) {
        Assert.notNull(dishTo, "Dish must be not null");
        Dish dish = mapper.toEntity(dishTo);
        return save(dish, dishTo.getRestaurantId());
    }

    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.getId()) == null) {
            return null;
        }
        dish.setDate(LocalDate.now());
        dish.setRestaurant(checkNotFoundWithId(crudRestaurantRepository.findById(restaurantId), restaurantId));
        return crudDishRepository.save(dish);
    }

    public Dish get(int id) {
        return checkNotFoundWithId(crudDishRepository.findById(id), id);
    }

    public List<Dish> getAllByRestaurant(int restaurantId) {
        return crudDishRepository.getAllByRestaurant(restaurantId);
    }

    @Cacheable(value = "dishes", key = "T(java.lang.String).valueOf(\"all\".toCharArray())")
    public List<Dish> getAllByRestaurantAndDate(int restaurantId, LocalDate date) {
        Assert.notNull(date, "Date must be not null");
        return crudDishRepository.getAllByRestaurantAndDate(restaurantId, date);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Transactional
    public void update(DishTo dishTo) {
        Assert.notNull(dishTo, "Dish must be not null");
        Dish dish = mapper.toEntity(dishTo);
        checkNotFoundWithId(save(dish, dishTo.getRestaurantId()), dish.id());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(int id) {
        crudDishRepository.deleteExisted(id);
    }
}
