package org.restaurant.voting.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import org.restaurant.voting.model.Dish;
import org.restaurant.voting.to.DishTo;
import org.restaurant.voting.to.DishWithRestaurantTo;

@Mapper
public interface DishMapper {
    DishMapper  INSTANCE = Mappers.getMapper(DishMapper.class);

    @Mapping(target = "restaurantId", source = "dish.restaurant.id")
    DishTo toToFromEntity(Dish dish);

    Dish toEntityFromTo(DishTo dishTo);

    @Mappings({
            @Mapping(target = "restaurantId", source = "dish.restaurant.id"),
            @Mapping(target = "restaurantName", source = "dish.restaurant.name")
    })
    DishWithRestaurantTo toToWithFromEntity(Dish dish);
}
