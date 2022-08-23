package org.restaurant.voting.service;

import lombok.RequiredArgsConstructor;
import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.repository.CrudRestaurantRepository;
import org.restaurant.voting.to.RestaurantTo;
import org.restaurant.voting.util.mapper.RestaurantMapper;
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
public class RestaurantService {

    private final CrudRestaurantRepository crudRestaurantRepository;
    private final RestaurantMapper mapper;

    @Transactional
    public Restaurant create(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "Restaurant must be not null");
        return save(restaurantTo);
    }

    public Restaurant save(RestaurantTo restaurantTo) {
        return crudRestaurantRepository.save(mapper.toEntity(restaurantTo));
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.findById(id), id);
    }

    public Restaurant getWithTodayMenu(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.getWithDishes(id, today()), id);
    }

    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll();
    }

    @Cacheable(value = "dishes", key = "T(java.lang.String).valueOf(\"all\".toCharArray())")
    public List<Restaurant> getAllWithTodayMenu() {
        return crudRestaurantRepository.getAllWithDishes(today());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    @Transactional
    public void update(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "Restaurant must be not null");
        checkNotFoundWithId(save(restaurantTo), restaurantTo.getId());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(int id) {
        crudRestaurantRepository.deleteExisted(id);
    }

    private LocalDate today() {
        return LocalDate.now();
    }
}
