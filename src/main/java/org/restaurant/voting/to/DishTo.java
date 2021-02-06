package org.restaurant.voting.to;

import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.Objects;

import org.restaurant.voting.View;

import static org.hibernate.validator.constraints.SafeHtml.WhiteListType.NONE;

public class DishTo extends BaseTo {
    protected int restaurantId;

    @Size(min = 2, max = 100)
    @SafeHtml(groups = {View.Web.class}, whitelistType = NONE)
    protected String name;

    @Positive
    protected int price;

    protected LocalDate date;

    public DishTo() {
    }

    @ConstructorProperties({"id", "restaurantId", "name", "price", "date"})
    public DishTo(Integer id, int restaurantId, String name, int price, LocalDate date) {
        super(id);
        this.restaurantId = restaurantId;
        this.name = name;
        this.price = price;
        this.date = date;
    }

    public int getRestaurantId() { return restaurantId; }

    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishTo dishTo = (DishTo) o;
        return price == dishTo.price &&
                restaurantId == dishTo.restaurantId &&
                Objects.equals(id, dishTo.id) &&
                Objects.equals(date, dishTo.date) &&
                Objects.equals(name, dishTo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurantId, name, price, date);
    }

    @Override
    public String toString() {
        return "DishTo{" +
                "id=" + id +
                ", restaurantId='" + restaurantId +
                ", name='" + name +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
