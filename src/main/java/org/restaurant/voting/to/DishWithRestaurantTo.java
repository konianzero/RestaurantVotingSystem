package org.restaurant.voting.to;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.Objects;

public class DishWithRestaurantTo extends DishTo {
    private int restaurantId;
    private String restaurantName;

    public DishWithRestaurantTo() {
    }

    @ConstructorProperties({"id", "name", "restaurantId", "restaurantName", "price", "date"})
    public DishWithRestaurantTo(Integer id, String name, int restaurantId, String restaurantName, int price, LocalDate date) {
        super(id, name, price, date);
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishWithRestaurantTo dishWithRestaurantTo = (DishWithRestaurantTo) o;
        return price == dishWithRestaurantTo.price &&
                restaurantId == dishWithRestaurantTo.restaurantId &&
                Objects.equals(id, dishWithRestaurantTo.id) &&
                Objects.equals(date, dishWithRestaurantTo.date) &&
                Objects.equals(name, dishWithRestaurantTo.name) &&
                Objects.equals(restaurantName, dishWithRestaurantTo.restaurantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, restaurantId, restaurantName, price, date);
    }

    @Override
    public String toString() {
        return "DishWithRestaurantTo{" +
                "id=" + id +
                ", name='" + name +
                ", restaurantId='" + restaurantId +
                ", restaurantName='" + restaurantName +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
