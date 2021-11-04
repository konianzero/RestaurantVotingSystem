package org.restaurant.voting.web.controller;

import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.service.RestaurantService;
import org.restaurant.voting.to.RestaurantTo;
import org.restaurant.voting.to.RestaurantWithMenuTo;
import org.restaurant.voting.util.mapper.RestaurantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.restaurant.voting.util.validation.ValidationUtil.assureIdConsistent;
import static org.restaurant.voting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    public static final String REST_URL = "/rest/restaurants";
    private static final Logger log = LoggerFactory.getLogger(DishRestController.class);

    private final RestaurantService service;
    private final RestaurantMapper mapper;

    public RestaurantRestController(RestaurantService service) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createWithLocation(@Valid @RequestBody RestaurantTo restaurantTo) {
        checkNew(restaurantTo);
        log.info("Create {}", restaurantTo);
        Restaurant created = service.create(restaurantTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                                                          .path(REST_URL + "/{id}")
                                                          .buildAndExpand(created.getId())
                                                          .toUri();
        return ResponseEntity.created(uriOfNewResource)
                             .body(mapper.toTo(created));
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("Get restaurant {}", id);
        return mapper.toTo(service.get(id));
    }

    @GetMapping("/{id}/today")
    public RestaurantWithMenuTo getForToday(@PathVariable int id) {
        log.info("Get restaurant {} with menu", id);
        return mapper.toToWithMenu(service.getWithTodayMenu(id));
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("Get all restaurants");
        return mapper.getToList(service.getAll());
    }

    @GetMapping("/today")
    public List<RestaurantWithMenuTo> getAllForToday() {
        log.info("Get all restaurants");
        return mapper.getToWithMenuList(service.getAllWithTodayMenu());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody RestaurantTo restaurantTo, @PathVariable int id) {
        assureIdConsistent(restaurantTo, id);
        log.info("Update {}", restaurantTo);
        service.update(restaurantTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Delete restaurant {}", id);
        service.delete(id);
    }
}
