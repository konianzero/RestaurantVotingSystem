package org.restaurant.voting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.restaurant.voting.util.ValidationUtil;

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

    @PostMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishTo> createWithLocation(@RequestBody Dish dish, @PathVariable int restaurantId) {
        checkNew(dish);
        log.info("Create {} for restaurant {}", dish, restaurantId);
        Dish created = service.create(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path(REST_URL + "/{id}")
                                                          .query("restaurantId={restaurantId}")
                                                          .buildAndExpand(created.getId(),
                                                                          created.getRestaurant().getId())
                                                          .toUri();
        return ResponseEntity.created(uriOfNewResource)
                             .body(createTo(created));
    }

    @PostMapping(value = "/menu/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<DishTo> createMenu(@RequestBody List<Dish> dishes, @PathVariable int restaurantId) {
        dishes.forEach(ValidationUtil::checkNew);
        log.info("Create menu {} for restaurant {}", dishes, restaurantId);
        List<Dish> created = service.createAllForRestaurant(dishes, restaurantId);
        return getTos(created);
    }

    @GetMapping("/{id}")
    public DishTo get(@PathVariable int id, @RequestParam int restaurantId) {
        log.info("Get {} from restaurant {}", id, restaurantId);
        return createTo(service.get(id, restaurantId));
    }

    @GetMapping("/with/{id}")
    public DishWithRestaurantTo getWith(@PathVariable int id, @RequestParam int restaurantId) {
        log.info("Get dish {} with restaurant {} info", id, restaurantId);
        return createWithRestTo(service.getWithRestaurant(id, restaurantId));
    }

    @GetMapping
    public List<DishTo> getAll() {
        log.info("Get all dishes");
        return getTos(service.getAll());
    }

    @GetMapping("/menu/{restaurantId}")
    public List<DishTo> getMenu(@PathVariable int restaurantId,
                                @RequestParam @DateTimeFormat(iso = DATE) Optional<LocalDate> date) {
        log.info("Get menu from restaurant {}{}", restaurantId, date.isPresent() ? " for " + date : "");
        List<Dish> dishes = date.isPresent()
                ? service.getAllByRestaurantAndDate(restaurantId, date.get())
                : service.getAllByRestaurant(restaurantId);
        return getTos(dishes);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Dish dish, @PathVariable int id, @RequestParam int restaurantId) {
        assureIdConsistent(dish, id);
        log.info("Update {} for restaurant {}", dish, restaurantId);
        service.update(dish, restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @RequestParam int restaurantId) {
        log.info("Delete meal {} of restaurant {}", id, restaurantId);
        service.delete(id, restaurantId);
    }
}
