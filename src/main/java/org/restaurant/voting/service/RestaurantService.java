package org.restaurant.voting.service;

import org.restaurant.voting.repository.CrudDishRepository;
import org.restaurant.voting.repository.CrudRestaurantRepository;
import org.restaurant.voting.to.RestaurantTo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.restaurant.voting.model.Restaurant;

import static org.restaurant.voting.util.RestaurantUtil.createNewFromTo;
import static org.restaurant.voting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    private static final Sort SORT_BY_NAME = Sort.by(Sort.Direction.DESC, "name");

    private final CrudDishRepository crudDishRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public RestaurantService(CrudDishRepository crudDishRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudDishRepository = crudDishRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    public Restaurant create(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "Restaurant must be not null");
        return save(restaurantTo);
    }

    @Transactional
    protected Restaurant save(RestaurantTo restaurantTo) {
        return crudRestaurantRepository.save(createNewFromTo(restaurantTo));
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.findById(id, Restaurant.class), id);
    }

    public Restaurant getWithDishes(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.getWithDishes(id), id);
    }

    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll(SORT_BY_NAME);
    }

    public List<Restaurant> getAllWithTodayMenu() {
        return crudRestaurantRepository.findAll(SORT_BY_NAME)
                                       .stream()
                                       .peek(r ->
                                               r.setMenu(
                                                       crudDishRepository.getAllByRestaurantAndDate(r.getId(), LocalDate.now())
                                               )
                                       )
                                       .collect(Collectors.toList());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void update(RestaurantTo restaurantTo) {
        Assert.notNull(restaurantTo, "Restaurant must be not null");
        checkNotFoundWithId(save(restaurantTo), restaurantTo.getId());
    }

    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudRestaurantRepository.delete(id) != 0, id);
    }
}
