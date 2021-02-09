package org.restaurant.voting.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.to.RestaurantTo;
import org.restaurant.voting.to.RestaurantWithMenuTo;

@Mapper
public interface RestaurantMapper {
    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    RestaurantTo toToFromEntity(Restaurant restaurant);

    Restaurant toEntityFromTo(RestaurantTo restaurantTo);

    RestaurantWithMenuTo toToWithFromEntity(Restaurant restaurant);
}
