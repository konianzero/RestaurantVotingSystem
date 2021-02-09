package org.restaurant.voting.util;

import java.util.List;
import java.util.stream.Collectors;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.to.DishWithRestaurantTo;
import org.restaurant.voting.to.DishTo;
import org.restaurant.voting.util.mapper.DishMapper;

public class DishUtil {

    public static DishTo createTo(Dish dish) {
        return DishMapper.INSTANCE.toToFromEntity(dish);
    }

    public static List<DishTo> getTos(List<Dish> dishes) {
        return dishes.stream()
                     .map(DishUtil::createTo)
                     .collect(Collectors.toList());
    }

    public static Dish createNewFromTo(DishTo dishTo) {
        return DishMapper.INSTANCE.toEntityFromTo(dishTo);
    }

    public static DishWithRestaurantTo createWithRestTo(Dish dish) {
        return DishMapper.INSTANCE.toToWithFromEntity(dish);
    }
}
