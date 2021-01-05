package org.restaurant.voting.util;

import java.util.List;
import java.util.stream.Collectors;

import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.to.RestaurantTo;
import org.restaurant.voting.to.RestaurantWithMenuTo;

public class RestaurantUtil {

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName());
    }

    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName());
    }

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                          .map(RestaurantUtil::createTo)
                          .collect(Collectors.toList());
    }

    public static RestaurantWithMenuTo createWithMenuTo(Restaurant restaurant) {
        return new RestaurantWithMenuTo(restaurant.getId(), restaurant.getName(), restaurant.getMenu());
    }
}
