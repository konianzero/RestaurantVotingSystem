package org.restaurant.voting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.restaurant.voting.model.Restaurant;

@Repository
public class RestaurantRepository {
    private static final Sort SORT_BY_NAME = Sort.by(Sort.Direction.DESC, "name");

    private final CrudDishRepository crudDishRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public RestaurantRepository(CrudDishRepository crudDishRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudDishRepository = crudDishRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        return crudRestaurantRepository.save(restaurant);
    }

    public Restaurant get(int id) {
        return crudRestaurantRepository.findById(id)
                         .orElse(null);
    }

    public Restaurant getWithDishes(int id) {
        return crudRestaurantRepository.getWithDishes(id);
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

    public boolean delete(int id) {
        return crudRestaurantRepository.delete(id) != 0;
    }
}
