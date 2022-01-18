package org.restaurant.voting.service;

import org.junit.jupiter.api.Test;
import org.restaurant.voting.model.Dish;
import org.restaurant.voting.util.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.DishTestData.*;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_1_ID;

class DishServiceTest extends AbstractServiceTest {
    @Autowired
    private DishService service;
    @Autowired
    private DishMapper mapper;

    @Test
    void create() {
        Dish newDish = getNew();
        Dish created = service.create(mapper.toTo(newDish));
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
        assertThrows(EntityNotFoundException.class, () -> service.get(NOT_FOUND));
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
        service.update(mapper.toTo(updated));
        DISH_MATCHER.assertMatch(service.get(DISH_1_ID), getUpdated());
    }

    @Test
    void delete() {
        service.delete(DISH_1_ID);
        assertThrows(EntityNotFoundException.class, () -> service.get(DISH_1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(NOT_FOUND));
    }
}