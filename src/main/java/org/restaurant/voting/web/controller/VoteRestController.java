package org.restaurant.voting.web.controller;

import org.restaurant.voting.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.to.VoteTo;
import org.restaurant.voting.service.VoteService;
import org.restaurant.voting.View;

import static org.restaurant.voting.util.ValidationUtil.assureIdConsistent;
import static org.restaurant.voting.util.ValidationUtil.checkNew;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createWithLocation(@Validated(View.Web.class) @RequestBody VoteTo voteTo) {
        checkNew(voteTo);
        int userId = SecurityUtil.authUserId();
        log.info("Create {} of user {}", voteTo, userId);
        Vote created = service.create(voteTo, userId);
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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody VoteTo voteTo, @PathVariable int id) {
        assureIdConsistent(voteTo, id);
        int userId = SecurityUtil.authUserId();
        log.info("Update {} of user {}", voteTo, userId);
        service.update(voteTo, userId);
    }
}
