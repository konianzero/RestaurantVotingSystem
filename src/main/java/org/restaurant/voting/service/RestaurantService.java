package org.restaurant.voting.service;

import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.repository.CrudRestaurantRepository;
import org.restaurant.voting.to.RestaurantTo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static org.restaurant.voting.util.RestaurantUtil.createNewFromTo;
import static org.restaurant.voting.util.validation.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {

    private final CrudRestaurantRepository crudRestaurantRepository;
    private final LocalDate TODAY = LocalDate.now();

    public RestaurantService(CrudRestaurantRepository crudRestaurantRepository) {
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Transactional
    public Restaurant create(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "Restaurant must be not null");
        return save(restaurantTo);
    }

    public Restaurant save(RestaurantTo restaurantTo) {
        return crudRestaurantRepository.save(createNewFromTo(restaurantTo));
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.findById(id), id);
    }

    public Restaurant getWithTodayMenu(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.getWithDishes(id, TODAY), id);
    }

    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll();
    }

    @Cacheable("dishes")
    public List<Restaurant> getAllWithTodayMenu() {
        return crudRestaurantRepository.getAllWithDishes(TODAY);
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Transactional
    public void update(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "Restaurant must be not null");
        checkNotFoundWithId(save(restaurantTo), restaurantTo.getId());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudRestaurantRepository.delete(id) != 0, id);
    }
}
