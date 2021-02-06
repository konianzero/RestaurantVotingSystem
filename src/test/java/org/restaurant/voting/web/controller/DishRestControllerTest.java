package org.restaurant.voting.web.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.service.DishService;
import org.restaurant.voting.to.DishTo;
import org.restaurant.voting.util.JsonUtil;
import org.restaurant.voting.util.exception.NotFoundException;
import org.restaurant.voting.TestUtil;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.restaurant.voting.RestaurantTestData.FIRST_RESTAURANT;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_1_ID;
import static org.restaurant.voting.DishTestData.*;
import static org.restaurant.voting.UserTestData.ADMIN;
import static org.restaurant.voting.UserTestData.USER;
import static org.restaurant.voting.TestUtil.userHttpBasic;
import static org.restaurant.voting.util.DishUtil.createTo;
import static org.restaurant.voting.util.exception.ErrorType.VALIDATION_ERROR;
import static org.restaurant.voting.util.DishUtil.*;

class DishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = DishRestController.REST_URL + '/';

    @Autowired
    private DishService service;

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        DishTo newDishTo = createTo(newDish);
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(newDishTo))
        ).andExpect(status().isCreated());

        DishTo created = TestUtil.readFromJson(action, DishTo.class);
        int newId = created.getId();
        newDishTo.setId(newId);
        newDish.setId(newId);

        DISH_TO_MATCHER.assertMatch(created, newDishTo);
        DISH_MATCHER.assertMatch(service.get(newId), newDish);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID)
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(createTo(DISH_1)));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                                      .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getWith() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID + "/with")
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_RESTA_TO_MATCHER.contentJson(createWithRestTo(DISH_1)));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(getTos(ALL_DISHES)));
    }

    @Test
    void getAllByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID))
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(getTos(FIRST_RESTAURANT_DISHES)));
    }

    @Test
    void getAllByRestaurantAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID))
                                      .queryParam("date", now().toString())
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(getTos(TODAY_REST1_MENU)));
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(createTo(updated))))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(service.get(DISH_1_ID), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_1_ID)
                                      .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(DISH_1_ID));
    }

    @Test
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, "Sandwich", FIRST_RESTAURANT, 0, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(createTo(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH_1_ID, "Miso soup", FIRST_RESTAURANT, 0, null);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(createTo(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        Dish invalid = new Dish(DISH_1_ID, "<script>alert(123)</script>", FIRST_RESTAURANT, 6, LocalDate.now());
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(createTo(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }
}