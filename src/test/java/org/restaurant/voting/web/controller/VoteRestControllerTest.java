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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_MATCHER;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_2_ID;
import static org.restaurant.voting.UserTestData.ADMIN_ID;
import static org.restaurant.voting.VoteTestData.*;
import static org.restaurant.voting.util.VoteUtil.getTos;
import static org.restaurant.voting.util.VoteUtil.createTo;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + "/";

    @Autowired
    private VoteService service;

    @Test
    void createWithLocation() throws Exception {
        Vote newVote = getNew();
        VoteTo newVoteTo = createTo(getNew());
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL)
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
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + VOTE_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(createTo(VOTE_1)));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(getTos(ALL_VOTES)));
    }

    @Test
    void getAllByUserId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by")
                                      .queryParam("userId", String.valueOf(ADMIN_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(getTos(List.of(VOTE_1, VOTE_3))));
    }

    @Test
    void getAllByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by")
                                      .queryParam("restaurantId", String.valueOf(RESTAURANT_2_ID)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(getTos(List.of(VOTE_1, VOTE_2, VOTE_3))));
    }

    @Test
    void update() throws Exception {
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + VOTE_1_ID)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(createTo(updated))))
                .andExpect(status().isNoContent());

        Vote actual = service.get(VOTE_1_ID, ADMIN_ID);
        VOTE_MATCHER.assertMatch(actual, updated);
        RESTAURANT_MATCHER.assertMatch(actual.getRestaurant(), updated.getRestaurant());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VOTE_1_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(VOTE_1_ID, ADMIN_ID));
    }
}