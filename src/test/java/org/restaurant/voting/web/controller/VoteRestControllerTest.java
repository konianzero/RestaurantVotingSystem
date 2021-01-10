package org.restaurant.voting.web.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.service.VoteService;
import org.restaurant.voting.to.VoteTo;
import org.restaurant.voting.util.JsonUtil;
import org.restaurant.voting.util.exception.NotFoundException;
import org.restaurant.voting.TestUtil;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.restaurant.voting.TestUtil.userHttpBasic;
import static org.restaurant.voting.UserTestData.ADMIN;
import static org.restaurant.voting.UserTestData.ADMIN_ID;
import static org.restaurant.voting.UserTestData.USER;
import static org.restaurant.voting.UserTestData.USER_ID;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_MATCHER;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_2_ID;
import static org.restaurant.voting.util.exception.ErrorType.VALIDATION_ERROR;
import static org.restaurant.voting.VoteTestData.*;
import static org.restaurant.voting.VoteTestData.NOT_FOUND;
import static org.restaurant.voting.util.VoteUtil.getTos;
import static org.restaurant.voting.util.VoteUtil.createTo;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + "/";

    @Autowired
    private VoteService service;

    @Test
    void createWithLocation() throws Exception {
        Vote newVote = getNew();
        VoteTo newVoteTo = createTo(newVote);
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(newVoteTo))
        ).andExpect(status().isCreated());

        VoteTo created = TestUtil.readFromJson(action, VoteTo.class);
        int newId = created.getId();
        newVoteTo.setId(newId);
        newVote.setId(newId);

        VOTE_TO_MATCHER.assertMatch(created, newVoteTo);
        VOTE_MATCHER.assertMatch(service.get(newId, ADMIN_ID), newVote);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE_2_ID)
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTo(VOTE_2)));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE_2_ID))
                .andDo(print())
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
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(getTos(ALL_VOTES)));
    }

    @Test
    void getAllByUserId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by")
                                      .queryParam("userId", String.valueOf(ADMIN_ID))
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(getTos(List.of(VOTE_1, VOTE_3))));
    }

    @Test
    void getAllByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by")
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_2_ID))
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(getTos(List.of(VOTE_1, VOTE_2, VOTE_3))));
    }

//    @Test
//    void update() throws Exception {
//        Vote updated = getUpdated();
//        perform(MockMvcRequestBuilders.put(REST_URL + VOTE_1_ID)
//                                      .with(userHttpBasic(ADMIN))
//                                      .contentType(MediaType.APPLICATION_JSON)
//                                      .content(JsonUtil.writeValue(createTo(updated))))
//                .andExpect(status().isNoContent());
//
//        Vote actual = service.get(VOTE_1_ID, ADMIN_ID);
//        VOTE_MATCHER.assertMatch(actual, updated);
//        RESTAURANT_MATCHER.assertMatch(actual.getRestaurant(), updated.getRestaurant());
//    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE_2_ID)
                                      .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(VOTE_2_ID, USER_ID));
    }

    @Test
    void createInvalid() throws Exception {
        VoteTo newVoteTo = new VoteTo(null, LocalDate.now(), ADMIN_ID, 1000);
        perform(MockMvcRequestBuilders.post(REST_URL)
                        .with(userHttpBasic(ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(newVoteTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateInvalid() throws Exception {
        VoteTo updateTo = new VoteTo(VOTE_1_ID, LocalDate.now(), ADMIN_ID, 1000);
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE_1_ID)
                                      .with(userHttpBasic(ADMIN))
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(updateTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }
}