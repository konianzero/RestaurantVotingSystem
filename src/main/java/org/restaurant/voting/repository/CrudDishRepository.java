package org.restaurant.voting.repository;

import org.restaurant.voting.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import org.restaurant.voting.model.Dish;

@Repository
@Transactional(readOnly = true)
public interface CrudDishRepository extends JpaRepository<Dish, Integer> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId ORDER BY d.date DESC")
    List<Dish> getAllByRestaurant(@Param("restaurantId") int restaurantId);

    @Query("SELECT d from Dish d WHERE d.restaurant.id=:restaurantId AND d.date=:date")
    List<Dish> getAllByRestaurantAndDate(@Param("restaurantId") int restaurantId, @Param("date") LocalDate date);

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.id=:dishId")
    int delete(@Param("dishId") int dishId);
}
