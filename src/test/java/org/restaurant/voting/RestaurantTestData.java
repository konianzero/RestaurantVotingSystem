package org.restaurant.voting;

import java.util.ArrayList;
import java.util.List;

import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.to.RestaurantTo;
import org.restaurant.voting.to.RestaurantWithMenuTo;

import static org.restaurant.voting.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "menu");
    public static final TestMatcher<RestaurantTo> RESTAURANT_TO_MATCHER = TestMatcher.usingEqualsComparator(RestaurantTo.class);
    public static final TestMatcher<RestaurantWithMenuTo> RESTAURANT_WITH_MENU_MATCHER = TestMatcher.usingEqualsComparator(RestaurantWithMenuTo.class);

    public static final int NOT_FOUND = 1000;
    public static final int RESTAURANT_1_ID = START_SEQ + 2;
    public static final int RESTAURANT_2_ID = START_SEQ + 3;

    public static final Restaurant FIRST_RESTAURANT = new Restaurant(RESTAURANT_1_ID, "Restaurant1");
    public static final Restaurant SECOND_RESTAURANT = new Restaurant(RESTAURANT_2_ID, "Restaurant2");

    public static final List<Restaurant> ALL_RESTAURANTS = List.of(SECOND_RESTAURANT, FIRST_RESTAURANT);

    public static void initMenu() {
        FIRST_RESTAURANT.setMenu(new ArrayList<>());
        SECOND_RESTAURANT.setMenu(new ArrayList<>());
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "Restaurant3");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_1_ID, "RestOne");
    }
}
