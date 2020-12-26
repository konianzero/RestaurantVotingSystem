package org.restaurant.voting;

import org.restaurant.voting.model.Restaurant;

import static org.restaurant.voting.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Restaurant.class, "menu");

    public static final int RESTAURANT_1_ID = START_SEQ + 2;
    public static final int RESTAURANT_2_ID = START_SEQ + 3;

    public static final Restaurant FIRST_RESTAURANT = new Restaurant(RESTAURANT_1_ID, "Restaurant1");
    public static final Restaurant SECOND_RESTAURANT = new Restaurant(RESTAURANT_2_ID, "Restaurant2");

    public static Restaurant getNew() {
        return new Restaurant(null, "Restaurant3");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_1_ID, "RestOne");
    }
}
