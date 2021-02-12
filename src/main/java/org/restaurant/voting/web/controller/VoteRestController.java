package org.restaurant.voting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.to.VoteTo;
import org.restaurant.voting.service.VoteService;
import org.restaurant.voting.util.SecurityUtil;

import static org.restaurant.voting.util.VoteUtil.createTo;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    public static final String REST_URL = "/rest/votes";
    private static final Logger log = LoggerFactory.getLogger(VoteRestController.class);

    private final VoteService service;

    public VoteRestController(VoteService service) {
        this.service = service;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createWithLocation(@RequestParam int restaurantId) {
        int userId = SecurityUtil.authUserId();
        log.info("Vote of user {} for restaurant {}", userId, restaurantId);
        Vote created = service.create(restaurantId, userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path(REST_URL + "/{id}")
                                                          .buildAndExpand(created.getId())
                                                          .toUri();
        return ResponseEntity.created(uriOfNewResource)
                             .body(createTo(created));
    }

    @GetMapping("/{id}")
    public VoteTo get(@PathVariable int id) {
        int userId = SecurityUtil.authUserId();
        log.info("Get vote {} of user {}", id, userId);
        return createTo(service.get(id, userId));
    }

    @GetMapping("/last")
    public VoteTo getLastVote() {
        int userId = SecurityUtil.authUserId();
        log.info("Get last vote");
        return createTo(service.getLast(userId));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestParam int restaurantId) {
        int userId = SecurityUtil.authUserId();
        log.info("Update vote of user {} for restaurant {}", userId, restaurantId);
        service.update(restaurantId, userId);
    }
}
