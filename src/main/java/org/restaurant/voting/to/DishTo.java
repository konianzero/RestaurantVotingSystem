package org.restaurant.voting.to;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.Objects;

public class DishTo extends BaseTo {
    protected String name;
    protected int price;
    protected LocalDate date;

    public DishTo() {
    }

    @ConstructorProperties({"id", "name", "price", "date"})
    public DishTo(Integer id, String name, int price, LocalDate date) {
        super(id);
        this.name = name;
        this.price = price;
        this.date = date;
    }

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
        DishTo mealTo = (DishTo) o;
        return price == mealTo.price &&
                Objects.equals(id, mealTo.id) &&
                Objects.equals(date, mealTo.date) &&
                Objects.equals(name, mealTo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, date);
    }

    @Override
    public String toString() {
        return "DishTo{" +
                "id=" + id +
                ", name='" + name +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
