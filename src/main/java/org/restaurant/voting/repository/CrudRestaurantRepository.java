package org.restaurant.voting.repository;

import org.restaurant.voting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CrudRestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT DISTINCT r from Restaurant r JOIN FETCH r.menu m WHERE r.id=:id AND  m.date=:date")
    Restaurant getWithDishes(@Param("id") int id, @Param("date") LocalDate date);

    @Query("SELECT DISTINCT r from Restaurant r JOIN FETCH r.menu m WHERE m.date=?1")
    List<Restaurant> getAllWithDishes(LocalDate date);
}
