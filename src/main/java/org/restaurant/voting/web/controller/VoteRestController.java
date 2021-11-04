package org.restaurant.voting.web.controller;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.service.VoteService;
import org.restaurant.voting.to.VoteTo;
import org.restaurant.voting.util.SecurityUtil;
import org.restaurant.voting.util.mapper.VoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    public static final String REST_URL = "/rest/votes";
    private static final Logger log = LoggerFactory.getLogger(VoteRestController.class);

    private final VoteService service;
    private final VoteMapper mapper;

    public VoteRestController(VoteService service, VoteMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> voteToday(@RequestParam int restaurantId) {
        int userId = SecurityUtil.authUserId();
        log.info("Vote of user {} for restaurant {}", userId, restaurantId);
        Vote created = service.create(restaurantId, userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path(REST_URL + "/{id}")
                                                          .buildAndExpand(created.getId())
                                                          .toUri();
        return ResponseEntity.created(uriOfNewResource)
                             .body(mapper.toTo(created));
    }

    @GetMapping()
    public List<VoteTo> getOwnVotes() {
        int userId = SecurityUtil.authUserId();
        log.info("Get vote of user {}", userId);
        return mapper.getToList(service.getAllByUserId(userId));
    }

    @GetMapping("/last")
    public VoteTo getLastVote() {
        int userId = SecurityUtil.authUserId();
        log.info("Get last vote");
        return mapper.toTo(service.getLast(userId));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revoteToday(@RequestParam int restaurantId) {
        int userId = SecurityUtil.authUserId();
        log.info("Update vote of user {} for restaurant {}", userId, restaurantId);
        service.update(restaurantId, userId);
    }
}
