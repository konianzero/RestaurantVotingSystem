package org.restaurant.voting.service;

import org.junit.jupiter.api.Test;
import org.restaurant.voting.model.Dish;
import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.DishTestData.*;
import static org.restaurant.voting.RestaurantTestData.getNew;
import static org.restaurant.voting.RestaurantTestData.getUpdated;
import static org.restaurant.voting.RestaurantTestData.*;
import static org.restaurant.voting.util.RestaurantUtil.createTo;

public class RestaurantServiceTest extends AbstractServiceTest  {

    @Autowired
    private RestaurantService service;

    @Test
    void create() {
        Restaurant newRestaurant = getNew();
        Restaurant created = service.create(createTo(newRestaurant));
        int newId = created.getId();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Test
    void get() {
        Restaurant actual = service.get(RESTAURANT_1_ID);
        RESTAURANT_MATCHER.assertMatch(actual, FIRST_RESTAURANT);
    }

    @Test
    void getWithDishes() {
        Restaurant actual = service.getWithTodayMenu(RESTAURANT_1_ID);
        DISH_MATCHER.assertMatch(actual.getMenu(), TODAY_REST1_MENU);
    }

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(service.getAll(), FIRST_RESTAURANT, SECOND_RESTAURANT);
    }

    @Test
    void getAllWithTodayMenu() {
        List<Dish> today = service.getAllWithTodayMenu()
                                  .stream()
                                  .flatMap(r -> r.getMenu().stream())
                                  .collect(Collectors.toList());
        DISH_MATCHER.assertMatch(today, TODAY_MENU);
    }

    @Test
    void update() {
        Restaurant updated = getUpdated();
        service.update(createTo(updated));
        RESTAURANT_MATCHER.assertMatch(service.get(RESTAURANT_1_ID), updated);
    }

    @Test
    void delete() {
        service.delete(RESTAURANT_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(RESTAURANT_1_ID));
    }
}