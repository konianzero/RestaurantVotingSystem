package org.restaurant.voting.cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.restaurant.voting.model.Dish;
import org.restaurant.voting.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDate.now;
import static org.restaurant.voting.DishTestData.DISH_MATCHER;
import static org.restaurant.voting.DishTestData.TODAY_REST1_MENU;
import static org.restaurant.voting.RestaurantTestData.RESTAURANT_1_ID;


@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class CacheTest {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private DishService dishService;

    @AfterAll
    static void afterAll() {

    }

    @Order(1)
    @Test
    void ehCacheExists() {
        Cache cache = cacheManager.getCache("dishes");
        Assertions.assertEquals("dishes", cache.getName());
        Assertions.assertEquals("Eh107Cache", cache.getNativeCache().getClass().getSimpleName());
    }

    @Order(2)
    @Test
    void cacheable() {
        Cache cache = cacheManager.getCache("dishes");
        dishService.getAllByRestaurantAndDate(RESTAURANT_1_ID, now());
        DISH_MATCHER.assertMatch((Iterable<Dish>) cache.get("all").get(), TODAY_REST1_MENU);
    }

    @Order(3)
    @Test
    void cacheEvict() {
        Cache cache = cacheManager.getCache("dishes");
        dishService.delete(100008);
        Assertions.assertNull(cache.get("all"));
    }
}
