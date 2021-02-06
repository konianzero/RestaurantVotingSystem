package org.restaurant.voting.service;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.util.exception.NotFoundException;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.DishTestData.*;
import static org.restaurant.voting.DishTestData.NOT_FOUND;
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
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(service.get(newId, RESTAURANT_1_ID), newDish);
    }

    @Test
    void createMenu() {
        List<Dish> newMenu = getNewMenu();
        List<Dish> created = service.createAllForRestaurant(newMenu, RESTAURANT_1_ID);

        newMenu.get(0).setId(created.get(0).getId());
        newMenu.get(1).setId(created.get(1).getId());

        DISH_MATCHER.assertMatch(service.getAllByRestaurant(RESTAURANT_1_ID), DISH_5, DISH_6, DISH_1, DISH_2, newMenu.get(0), newMenu.get(1));
    }

    @Test
    void getAll() {
        DISH_MATCHER.assertMatch(service.getAll(), DISH_1, DISH_2, DISH_3, DISH_4, DISH_5, DISH_6, DISH_7, DISH_8);
    }

    @Test
    void getAllByRestaurant() {
        DISH_MATCHER.assertMatch(service.getAllByRestaurant(RESTAURANT_1_ID), DISH_5, DISH_6, DISH_1, DISH_2);
    }

    @Test
    void getAllByRestaurantWithDate() {
        DISH_MATCHER.assertMatch(service.getAllByRestaurantAndDate(RESTAURANT_1_ID, now()), DISH_5, DISH_6);
    }

    @Test
    void get() {
        Dish actual = service.get(DISH_1_ID, RESTAURANT_1_ID);
        DISH_MATCHER.assertMatch(actual, DISH_1);
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(DISH_1_ID, RESTAURANT_2_ID));
    }

    @Test
    void getWithRestaurant() {
        Dish actual = service.getWithRestaurant(DISH_1_ID, RESTAURANT_1_ID);
        RESTAURANT_MATCHER.assertMatch(actual.getRestaurant(), FIRST_RESTAURANT);
    }
    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, RESTAURANT_1_ID));
    }

    @Test
    void update() {
        Dish updated = getUpdated();
        service.update(updated, RESTAURANT_1_ID);
        DISH_MATCHER.assertMatch(service.get(DISH_1_ID, RESTAURANT_1_ID), getUpdated());
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