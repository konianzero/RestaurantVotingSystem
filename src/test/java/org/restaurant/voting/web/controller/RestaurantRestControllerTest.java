package org.restaurant.voting.web.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.restaurant.voting.TestUtil;
import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.service.RestaurantService;
import org.restaurant.voting.to.RestaurantTo;
import org.restaurant.voting.util.JsonUtil;
import org.restaurant.voting.util.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.restaurant.voting.DishTestData.*;
import static org.restaurant.voting.RestaurantTestData.NOT_FOUND;
import static org.restaurant.voting.RestaurantTestData.getNew;
import static org.restaurant.voting.RestaurantTestData.getUpdated;
import static org.restaurant.voting.RestaurantTestData.*;
import static org.restaurant.voting.UserTestData.ADMIN_EMAIL;
import static org.restaurant.voting.UserTestData.USER_EMAIL;
import static org.restaurant.voting.util.RestaurantUtil.*;
import static org.restaurant.voting.util.exception.ErrorType.VALIDATION_ERROR;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantRestController.REST_URL + '/';

    @Autowired
    private RestaurantService service;

    @Order(1)
    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(newRestaurant))
        ).andExpect(status().isCreated());

        RestaurantTo created = TestUtil.readFromJson(action, RestaurantTo.class);
        int newId = created.getId();
        newRestaurant.setId(newId);

        RESTAURANT_TO_MATCHER.assertMatch(created, createTo(newRestaurant));
        RESTAURANT_MATCHER.assertMatch(service.get(newId), newRestaurant);
    }

    @Order(2)
    @Test
    @WithUserDetails(value = USER_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_2_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(createTo(SECOND_RESTAURANT)));
    }

    @Order(3)
    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_2_ID))
                .andExpect(status().isUnauthorized());
    }

    @Order(4)
    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Order(5)
    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getWith() throws Exception {
        initMenu();
        FIRST_RESTAURANT.getMenu().addAll(TODAY_REST1_MENU);

        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_1_ID + "/today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_WITH_MENU_MATCHER.contentJson(createWithMenuTo(FIRST_RESTAURANT)));
    }

    @Order(6)
    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(getTos(ALL_RESTAURANTS)));
    }

    @Order(7)
    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getAllForToday() throws Exception {
        initMenu();
        FIRST_RESTAURANT.getMenu().addAll(TODAY_REST1_MENU);
        SECOND_RESTAURANT.getMenu().addAll(TODAY_REST2_MENU);

        perform(MockMvcRequestBuilders.get(REST_URL + "/today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_WITH_MENU_MATCHER.contentJson(getTosWithMenu(ALL_RESTAURANTS)));
    }

    @Order(8)
    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(service.get(RESTAURANT_1_ID), updated);
    }

    @Order(9)
    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT_1_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(RESTAURANT_1_ID));
    }

    @Order(10)
    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void createInvalid() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, "", "");
        perform(MockMvcRequestBuilders.post(REST_URL)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Order(11)
    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void updateInvalid() throws Exception {
        Restaurant updated = new Restaurant(RESTAURANT_1_ID, "1", "ул. Бумажная, д.1");
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Order(12)
    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void updateHtmlUnsafe() throws Exception {
        Restaurant newRestaurant = new Restaurant(null, "<script>alert(123)</script>", "ул. Бумажная, д.1");
        perform(MockMvcRequestBuilders.post(REST_URL)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(newRestaurant)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }
}