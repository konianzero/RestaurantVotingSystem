package org.restaurant.voting.web.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.service.DishService;
import org.restaurant.voting.to.DishTo;
import org.restaurant.voting.util.JsonUtil;
import org.restaurant.voting.util.exception.NotFoundException;
import org.restaurant.voting.TestUtil;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.RestaurantTestData.FIRST_RESTAURANT;
import static org.restaurant.voting.TestUtil.userHttpBasic;
import static org.restaurant.voting.UserTestData.ADMIN;
import static org.restaurant.voting.UserTestData.USER;
import static org.restaurant.voting.util.exception.ErrorType.VALIDATION_ERROR;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.restaurant.voting.util.DishUtil.*;
import static org.restaurant.voting.DishTestData.*;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_1_ID;

class DishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = DishRestController.REST_URL + '/';

    @Autowired
    private DishService service;

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL + RESTAURANT_1_ID)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(newDish))
        ).andExpect(status().isCreated());

        DishTo created = TestUtil.readFromJson(action, DishTo.class);
        int newId = created.getId();
        newDish.setId(newId);

        DISH_TO_MATCHER.assertMatch(created, createTo(newDish));
        DISH_MATCHER.assertMatch(service.get(newId, RESTAURANT_1_ID), newDish);
    }

    @Test
    void createMenu() throws Exception {
        List<Dish> newMenu = getNewMenu();
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL + "menu/" + RESTAURANT_1_ID)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(newMenu))
        );

        List<DishTo> created = TestUtil.readFromJsonArray(action, DishTo.class);
        IntStream.range(0, newMenu.size())
                 .forEach(i -> newMenu.get(i).setId(created.get(i).getId()));

        DISH_TO_MATCHER.assertMatch(created, getTos(newMenu));
        DISH_MATCHER.assertMatch(service.getAllByRestaurantAndDate(RESTAURANT_1_ID, of(2020, 12, 21)), newMenu);
        DISH_MATCHER.assertMatch(service.getAllByRestaurant(RESTAURANT_1_ID), DISH_5, DISH_6, DISH_1, DISH_2, newMenu.get(0), newMenu.get(1));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID))
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(createTo(DISH_1)));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID))
                                      .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getWith() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "with/" + DISH_1_ID)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID))
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
    void getMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "menu/" + RESTAURANT_1_ID)
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(getTos(FIRST_RESTAURANT_MENU)));
    }

    @Test
    void getMenuByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "menu/" + RESTAURANT_1_ID)
                                      .queryParam("date", now().toString())
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(getTos(List.of(DISH_5, DISH_6))));
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID))
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(service.get(DISH_1_ID, RESTAURANT_1_ID), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_1_ID)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID))
                                      .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(DISH_1_ID, RESTAURANT_1_ID));
    }

    @Test
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, "Sandwich", FIRST_RESTAURANT, 0, null);
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT_1_ID)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH_1_ID, "Miso soup", FIRST_RESTAURANT, 0, null);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID))
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        Dish invalid = new Dish(null, "<script>alert(123)</script>", FIRST_RESTAURANT, 6, LocalDate.now());
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID))
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }
}