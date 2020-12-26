package org.restaurant.voting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.util.exception.NotFoundException;

import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.DishTestData.*;
import static org.restaurant.voting.DishTestData.getUpdated;
import static org.restaurant.voting.DishTestData.getNew;
import static org.restaurant.voting.RestaurantTestData.*;

public class DishServiceTest extends AbstractServiceTest {
    @Autowired
    private DishService service;

    @Test
    void create() {
        Dish newDish = getNew();
        Dish created = service.create(newDish, RESTAURANT_1_ID);
        int newId = created.id();
        newDish.setId(newId);
//        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(service.get(newId, RESTAURANT_1_ID), newDish);
    }

    @Test
    void createMenu() {
        Dish first = new Dish(null, "Salad", FIRST_RESTAURANT, 4, of(2020, 12, 21));
        Dish second = new Dish(null, "Tea", FIRST_RESTAURANT, 2, of(2020, 12, 21));
        List<Dish> created = service.create(Arrays.asList(first, second));

        first.setId(created.get(0).getId());
        second.setId(created.get(1).getId());

        DISH_MATCHER.assertMatch(service.getAll(RESTAURANT_1_ID), first, second, DISH_1, DISH_2, DISH_5, DISH_6);
    }

    @Test
    void update() {
        Dish updated = getUpdated();
        service.update(updated, RESTAURANT_1_ID);
        DISH_MATCHER.assertMatch(service.get(DISH_1_ID, RESTAURANT_1_ID), getUpdated());
    }

    @Test
    void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), RESTAURANT_2_ID));
    }

    @Test
    void getAll() {
        DISH_MATCHER.assertMatch(service.getAll(RESTAURANT_1_ID), DISH_1, DISH_2, DISH_5, DISH_6);
    }

    @Test
    void getAllByDate() {
        DISH_MATCHER.assertMatch(service.getAllByDate(RESTAURANT_1_ID, of(2020, 12, 19)), DISH_5, DISH_6);
    }

    @Test
    void get() {
        Dish actual = service.get(DISH_1_ID, RESTAURANT_1_ID);
        DISH_MATCHER.assertMatch(actual, DISH_1);
    }

    @Test
    void getWithRestaurant() {
        Dish actual = service.getWithRestaurant(DISH_1_ID);
        RESTAURANT_MATCHER.assertMatch(actual.getRestaurant(), FIRST_RESTAURANT);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, RESTAURANT_1_ID));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(DISH_1_ID, RESTAURANT_2_ID));
    }

    @Test
    void delete() {
        service.delete(DISH_1_ID, RESTAURANT_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(DISH_1_ID, RESTAURANT_1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, RESTAURANT_1_ID));
    }

    @Test
    void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(DISH_1_ID, RESTAURANT_2_ID));
    }
}