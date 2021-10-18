package org.restaurant.voting.to;


import org.restaurant.voting.util.validation.SafeHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.util.Objects;

public class RestaurantTo extends BaseTo {
    @NotBlank
    @Size(min = 2, max = 100)
    @SafeHtml
    protected String name;

    public RestaurantTo() {
    }

    @ConstructorProperties({"id", "name"})
    public RestaurantTo(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTo mealTo = (RestaurantTo) o;
        return Objects.equals(id, mealTo.id) &&
                Objects.equals(name, mealTo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", name='" + name +
                '}';
    }
}
