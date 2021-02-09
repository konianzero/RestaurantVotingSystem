package org.restaurant.voting.util;

import java.util.List;
import java.util.stream.Collectors;

import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.to.RestaurantTo;
import org.restaurant.voting.to.RestaurantWithMenuTo;
import org.restaurant.voting.util.mapper.RestaurantMapper;

public class RestaurantUtil {

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return RestaurantMapper.INSTANCE.toEntityFromTo(restaurantTo);
    }

    public static RestaurantTo createTo(Restaurant restaurant) {
        return RestaurantMapper.INSTANCE.toToFromEntity(restaurant);
    }

    public static List<RestaurantTo> getTos(List<Restaurant> restaurants) {
        return restaurants.stream()
                          .map(RestaurantUtil::createTo)
                          .collect(Collectors.toList());
    }

    public static RestaurantWithMenuTo createWithMenuTo(Restaurant restaurant) {
        return RestaurantMapper.INSTANCE.toToWithFromEntity(restaurant);
    }

    public static List<RestaurantWithMenuTo> getTosWithMenu(List<Restaurant> restaurants) {
        return restaurants.stream()
                          .map(RestaurantUtil::createWithMenuTo)
                          .collect(Collectors.toList());
    }
}
