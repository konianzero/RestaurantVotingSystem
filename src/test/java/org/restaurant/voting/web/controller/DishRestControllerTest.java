package org.restaurant.voting.web.controller;

import org.junit.jupiter.api.Test;
import org.restaurant.voting.TestUtil;
import org.restaurant.voting.model.Dish;
import org.restaurant.voting.service.DishService;
import org.restaurant.voting.to.DishTo;
import org.restaurant.voting.util.JsonUtil;
import org.restaurant.voting.util.exception.NotFoundException;
import org.restaurant.voting.util.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.DishTestData.*;
import static org.restaurant.voting.RestaurantTestData.FIRST_RESTAURANT;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_1_ID;
import static org.restaurant.voting.UserTestData.ADMIN_EMAIL;
import static org.restaurant.voting.UserTestData.USER_EMAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = DishRestController.REST_URL + '/';

    @Autowired
    private DishService service;
    @Autowired
    private DishMapper mapper;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        DishTo newDishTo = mapper.toTo(newDish);
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL)
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
    @WithUserDetails(value = USER_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(mapper.toTo(DISH_1)));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getAllByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(mapper.getEntityList(FIRST_RESTAURANT_DISHES)));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getAllByRestaurantAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_1_ID))
                                      .queryParam("date", now().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(mapper.getEntityList(TODAY_REST1_MENU)));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(mapper.toTo(updated))))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(service.get(DISH_1_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_1_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(DISH_1_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, "Sandwich", FIRST_RESTAURANT, 0, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(mapper.toTo(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH_1_ID, "Miso soup", FIRST_RESTAURANT, 0, null);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(mapper.toTo(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void updateHtmlUnsafe() throws Exception {
        Dish invalid = new Dish(DISH_1_ID, "<script>alert(123)</script>", FIRST_RESTAURANT, 6, LocalDate.now());
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(mapper.toTo(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}