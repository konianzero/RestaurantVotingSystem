package org.restaurant.voting.service;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.restaurant.voting.model.Dish;
import org.restaurant.voting.util.exception.NotFoundException;
import org.restaurant.voting.util.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.DishTestData.*;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_1_ID;

class DishServiceTest extends AbstractServiceTest {
    @Autowired
    private DishService service;
    @Autowired
    private DishMapper mapper;

//    @Order(1)
    @Test
    void get() {
        Dish actual = service.get(DISH_1_ID);
        DISH_MATCHER.assertMatch(actual, DISH_1);
    }

//    @Order(2)
    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

//    @Order(3)
    @Test
    void getAllByRestaurant() {
        DISH_MATCHER.assertMatch(service.getAllByRestaurant(RESTAURANT_1_ID), FIRST_RESTAURANT_DISHES);
    }

//    @Order(4)
    @Test
    void getAllByRestaurantWithDate() {
        DISH_MATCHER.assertMatch(service.getAllByRestaurantAndDate(RESTAURANT_1_ID, now()), TODAY_REST1_MENU);
    }

//    @Order(5)
    @Test
    void update() {
        Dish updated = getUpdated();
        service.update(mapper.toTo(updated));
        DISH_MATCHER.assertMatch(service.get(DISH_1_ID), getUpdated());
    }

//    @Order(6)
    @Test
    void create() {
        Dish newDish = getNew();
        Dish created = service.create(mapper.toTo(newDish));
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(service.get(newId), newDish);
    }

//    @Order(7)
    @Test
    void delete() {
        service.delete(DISH_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(DISH_1_ID));
    }

//    @Order(8)
    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }
}