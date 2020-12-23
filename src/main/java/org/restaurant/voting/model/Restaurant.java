package org.restaurant.voting.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "restaurants", uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "restaurants_unique_idx"))
public class Restaurant extends AbstractNamedEntity {
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
    @OrderBy("date desc")
    private List<Dish> menu;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, List<Dish> menu) {
        super(id, name);
        this.menu = menu;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menu=" + menu +
                '}';
    }
}
