package org.restaurant.voting.util.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.to.RestaurantTo;
import org.restaurant.voting.to.RestaurantWithMenuTo;

import java.util.List;

@Mapper(componentModel="spring")
public interface RestaurantMapper {

    @Named(value = "to")
    RestaurantTo toTo(Restaurant restaurant);

    Restaurant toEntity(RestaurantTo restaurantTo);

    @Named(value = "toWith")
    RestaurantWithMenuTo toToWithMenu(Restaurant restaurant);

    @IterableMapping(qualifiedByName = "to")
    List<RestaurantTo> getToList(List<Restaurant> restaurants);

    @IterableMapping(qualifiedByName = "toWith")
    List<RestaurantWithMenuTo> getToWithMenuList(List<Restaurant> restaurants);
}
