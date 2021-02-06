package org.restaurant.voting.util;

import java.util.List;
import java.util.stream.Collectors;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.to.DishWithRestaurantTo;
import org.restaurant.voting.to.DishTo;

public class DishUtil {

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getRestaurant().getId(), dish.getName(), dish.getPrice(), dish.getDate());
    }

    public static List<DishTo> getTos(List<Dish> dishes) {
        return dishes.stream()
                     .map(DishUtil::createTo)
                     .collect(Collectors.toList());
    }

    public static Dish createNewFromTo(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getName(), null, dishTo.getPrice(), dishTo.getDate());
    }

    public static DishWithRestaurantTo createWithRestTo(Dish dish) {
        return new DishWithRestaurantTo(
                dish.getId(),
                dish.getName(),
                dish.getRestaurant().getId(),
                dish.getRestaurant().getName(),
                dish.getPrice(),
                dish.getDate()
        );
    }
}
