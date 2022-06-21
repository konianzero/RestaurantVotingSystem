package org.restaurant.voting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dishes", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "date", "name"}, name = "dishes_unique_idx"))
@NoArgsConstructor
@Getter
@Setter
public class Dish extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Nullable
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Restaurant restaurant;

    @Column(name = "price", nullable = false)
    @Range(min = 1)
    private int price;

    @Column(name = "date", nullable = false, columnDefinition = "DATE DEFAULT now()")
    @NotNull
    private LocalDate date;

    public Dish(Integer id, String name, Restaurant restaurant, int price, LocalDate date) {
        super(id, name);
        this.restaurant = restaurant;
        this.price = price;
        this.date = date;
    }
}
