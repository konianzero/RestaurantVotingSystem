package org.restaurant.voting;

import static java.time.LocalDate.of;

import org.restaurant.voting.model.Dish;

import static org.restaurant.voting.RestaurantTestData.FIRST_RESTAURANT;
import static org.restaurant.voting.RestaurantTestData.SECOND_RESTAURANT;
import static org.restaurant.voting.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static final TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Dish.class, "restaurant");

    public static final int NOT_FOUND = 100;
    public static final int DISH_1_ID = START_SEQ + 4;

    public static final Dish DISH_1 = new Dish(DISH_1_ID, "Salad", FIRST_RESTAURANT, 5, of(2020, 12, 20));
    public static final Dish DISH_2 = new Dish(DISH_1_ID + 1, "Water", FIRST_RESTAURANT, 1, of(2020, 12, 20));
    public static final Dish DISH_3 = new Dish(DISH_1_ID + 2, "Burger", SECOND_RESTAURANT, 40, of(2020, 12, 20));
    public static final Dish DISH_4 = new Dish(DISH_1_ID + 3, "Potato", SECOND_RESTAURANT, 10, of(2020, 12, 20));
    public static final Dish DISH_5 = new Dish(DISH_1_ID + 4, "IceCream", FIRST_RESTAURANT, 20, of(2020, 12, 19));
    public static final Dish DISH_6 = new Dish(DISH_1_ID + 5, "Orange fresh", FIRST_RESTAURANT, 40, of(2020, 12, 19));
    public static final Dish DISH_7 = new Dish(DISH_1_ID + 6, "Soup", SECOND_RESTAURANT, 8, of(2020, 12, 19));
    public static final Dish DISH_8 = new Dish(DISH_1_ID + 7, "Cheeseburger", SECOND_RESTAURANT, 40, of(2020, 12, 19));

    public static Dish getNew() {
        return new Dish(null, "Sandwich", FIRST_RESTAURANT, 6, of(2020, 12, 21));
    }

    public static Dish getUpdated() {
        return new Dish(DISH_1_ID, "Miso soup", FIRST_RESTAURANT, 7, of(2020, 12, 20));
    }
}
