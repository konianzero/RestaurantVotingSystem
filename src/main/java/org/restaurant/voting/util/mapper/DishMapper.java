package org.restaurant.voting.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.restaurant.voting.model.Dish;
import org.restaurant.voting.to.DishTo;

import java.util.List;

@Mapper(componentModel="spring")
public interface DishMapper {

    @Mapping(target = "restaurantId", source = "dish.restaurant.id")
    DishTo toTo(Dish dish);

    Dish toEntity(DishTo dishTo);

    List<DishTo> getEntityList(List<Dish> dish);
}
