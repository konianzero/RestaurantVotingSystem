package org.restaurant.voting.web.controller.user;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.restaurant.voting.model.User;
import org.restaurant.voting.service.UserService;
import org.restaurant.voting.to.UserTo;
import org.restaurant.voting.util.JsonUtil;
import org.restaurant.voting.web.controller.AbstractControllerTest;
import org.restaurant.voting.TestUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.restaurant.voting.UserTestData.*;
import static org.restaurant.voting.util.UserUtil.*;

class ProfileRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileRestController.REST_URL + '/';

    @Autowired
    private UserService service;

    @Test
    void register() throws Exception {
        User newUser = getNew();
        UserTo newTo = createTo(newUser);
        ResultActions action = perform(
                MockMvcRequestBuilders.post(REST_URL + "/register")
                                                             .contentType(MediaType.APPLICATION_JSON)
                                                             .content(JsonUtil.writeValue(newTo))
        ).andExpect(status().isCreated());

        User created = TestUtil.readFromJson(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);

        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(ADMIN));
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(JsonUtil.writeValue(createTo(updated))))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(service.get(ADMIN_ID), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(service.getAll(), USER);
    }
}