package org.restaurant.voting;

import java.util.List;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.to.DishTo;
import org.restaurant.voting.to.DishWithRestaurantTo;

import static java.time.LocalDate.now;
import static org.restaurant.voting.RestaurantTestData.FIRST_RESTAURANT;
import static org.restaurant.voting.RestaurantTestData.SECOND_RESTAURANT;
import static org.restaurant.voting.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static final TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Dish.class, "restaurant");
    public static TestMatcher<DishTo> DISH_TO_MATCHER = TestMatcher.usingEqualsComparator(DishTo.class);
    public static TestMatcher<DishWithRestaurantTo> DISH_RESTA_TO_MATCHER = TestMatcher.usingEqualsComparator(DishWithRestaurantTo.class);

    public static final int NOT_FOUND = 100;
    public static final int DISH_1_ID = START_SEQ + 4;


    public static final Dish DISH_1 = new Dish(DISH_1_ID, "IceCream", FIRST_RESTAURANT, 20, now().minusDays(1));
    public static final Dish DISH_2 = new Dish(DISH_1_ID + 1, "Orange fresh", FIRST_RESTAURANT, 40, now().minusDays(1));
    public static final Dish DISH_3 = new Dish(DISH_1_ID + 2, "Soup", SECOND_RESTAURANT, 8, now().minusDays(1));
    public static final Dish DISH_4 = new Dish(DISH_1_ID + 3, "Cheeseburger", SECOND_RESTAURANT, 40, now().minusDays(1));
    public static final Dish DISH_5 = new Dish(DISH_1_ID + 4, "Salad", FIRST_RESTAURANT, 5, now());
    public static final Dish DISH_6 = new Dish(DISH_1_ID + 5, "Water", FIRST_RESTAURANT, 1, now());
    public static final Dish DISH_7 = new Dish(DISH_1_ID + 6, "Burger", SECOND_RESTAURANT, 40, now());
    public static final Dish DISH_8 = new Dish(DISH_1_ID + 7, "Potato", SECOND_RESTAURANT, 10, now());

    public static final List<Dish> ALL_DISHES = List.of(DISH_1, DISH_2, DISH_3, DISH_4, DISH_5, DISH_6, DISH_7, DISH_8);

    public static final List<Dish> FIRST_RESTAURANT_DISHES = List.of(DISH_5, DISH_6, DISH_1, DISH_2);

    public static final List<Dish> TODAY_MENU = List.of(DISH_7, DISH_8, DISH_5, DISH_6);
    public static final List<Dish> TODAY_REST1_MENU = List.of(DISH_5, DISH_6);
    public static final List<Dish> TODAY_REST2_MENU = List.of(DISH_7, DISH_8);

    public static Dish getNew() {
        return new Dish(null, "Sandwich", FIRST_RESTAURANT, 6, now());
    }

    public static Dish getUpdated() {
        return new Dish(DISH_1_ID, "Miso soup", FIRST_RESTAURANT, 7, now());
    }
}
