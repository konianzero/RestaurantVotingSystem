package org.restaurant.voting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.service.DishService;
import org.restaurant.voting.to.DishTo;
import org.restaurant.voting.to.DishWithRestaurantTo;
import org.restaurant.voting.View;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.restaurant.voting.util.ValidationUtil.checkNew;
import static org.restaurant.voting.util.DishUtil.*;
import static org.restaurant.voting.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = DishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController {
    public static final String REST_URL = "/rest/dishes";
    private static final Logger log = LoggerFactory.getLogger(DishRestController.class);

    private final DishService service;

    public DishRestController(DishService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishTo> createWithLocation(@Validated(View.Web.class) @RequestBody DishTo dishTo) {
        checkNew(dishTo);
        log.info("Create {}", dishTo);
        Dish created = service.create(dishTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path(REST_URL + "/{id}")
                                                          .buildAndExpand(created.getId())
                                                          .toUri();
        return ResponseEntity.created(uriOfNewResource)
                             .body(createTo(created));
    }

    @GetMapping("/{id}")
    public DishTo get(@PathVariable int id) {
        log.info("Get {}", id);
        return createTo(service.get(id));
    }

    @GetMapping("/{id}/with")
    public DishWithRestaurantTo getWith(@PathVariable int id) {
        log.info("Get dish {} with restaurant", id);
        return createWithRestTo(service.getWithRestaurant(id));
    }

    @GetMapping
    private List<DishTo> getAllBy(@RequestParam int restaurantId,
                                  @RequestParam @DateTimeFormat(iso = DATE) Optional<LocalDate> date) {
        log.info("Get all dishes from restaurant {}{}", restaurantId, date.isPresent() ? " for " + date : "");
        return date.map(d -> getTos(service.getAllByRestaurantAndDate(restaurantId, d)))
                   .orElse(getTos(service.getAllByRestaurant(restaurantId)));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody DishTo dishTo, @PathVariable int id) {
        assureIdConsistent(dishTo, id);
        log.info("Update {}", dishTo);
        service.update(dishTo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Delete {}", id);
        service.delete(id);
    }
}
