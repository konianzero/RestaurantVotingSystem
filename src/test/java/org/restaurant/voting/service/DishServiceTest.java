package org.restaurant.voting.service;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.util.exception.NotFoundException;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.DishTestData.*;
import static org.restaurant.voting.DishTestData.NOT_FOUND;
import static org.restaurant.voting.DishTestData.getUpdated;
import static org.restaurant.voting.DishTestData.getNew;
import static org.restaurant.voting.RestaurantTestData.*;
import static org.restaurant.voting.util.DishUtil.createTo;

public class DishServiceTest extends AbstractServiceTest {
    @Autowired
    private DishService service;

    @Test
    void create() {
        Dish newDish = getNew();
        Dish created = service.create(createTo(newDish));
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(service.get(newId), newDish);
    }

    @Test
    void get() {
        Dish actual = service.get(DISH_1_ID);
        DISH_MATCHER.assertMatch(actual, DISH_1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void getWithRestaurant() {
        Dish actual = service.getWithRestaurant(DISH_1_ID);
        RESTAURANT_MATCHER.assertMatch(actual.getRestaurant(), FIRST_RESTAURANT);
    }

    @Test
    void getAll() {
        DISH_MATCHER.assertMatch(service.getAll(), ALL_DISHES);
    }

    @Test
    void getAllByRestaurant() {
        DISH_MATCHER.assertMatch(service.getAllByRestaurant(RESTAURANT_1_ID), FIRST_RESTAURANT_DISHES);
    }

    @Test
    void getAllByRestaurantWithDate() {
        DISH_MATCHER.assertMatch(service.getAllByRestaurantAndDate(RESTAURANT_1_ID, now()), TODAY_REST1_MENU);
    }

    @Test
    void update() {
        Dish updated = getUpdated();
        service.update(createTo(updated));
        DISH_MATCHER.assertMatch(service.get(DISH_1_ID), getUpdated());
    }

    @Test
    void delete() {
        service.delete(DISH_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(DISH_1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }
}