package org.restaurant.voting.to;

import org.restaurant.voting.model.Dish;
import org.springframework.lang.Nullable;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Objects;

public class RestaurantWithMenuTo extends RestaurantTo {
    private List<Dish> menu;

    public RestaurantWithMenuTo() {
    }

    @ConstructorProperties({"id", "name", "address", "menu"})
    public RestaurantWithMenuTo(Integer id, String name, @Nullable String address, List<Dish> menu) {
        super(id, name, address);
        this.menu = menu;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public void setMenu(List<Dish> menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantWithMenuTo restTo = (RestaurantWithMenuTo) o;
        return Objects.equals(id, restTo.id) &&
                Objects.equals(name, restTo.name) &&
                Objects.equals(menu, restTo.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, menu);
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", name='" + name +
                ", menu='" + menu +
                '}';
    }
}
