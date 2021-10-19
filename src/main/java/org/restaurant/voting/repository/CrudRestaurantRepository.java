package org.restaurant.voting.repository;

import org.restaurant.voting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Query("SELECT DISTINCT r from Restaurant r JOIN FETCH r.menu m WHERE r.id=:id AND  m.date=:date")
    Restaurant getWithDishes(@Param("id") int id, @Param("date") LocalDate date);

    @Query("SELECT DISTINCT r from Restaurant r JOIN FETCH r.menu m WHERE m.date=?1")
    List<Restaurant> getAllWithDishes(LocalDate date);

    @Modifying
    @Transactional
    @Query("DELETE FROM Restaurant r WHERE r.id=?1")
    int delete(int id);
}
