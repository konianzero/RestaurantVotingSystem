package org.restaurant.voting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.restaurant.voting.util.validation.SafeHtml;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "restaurants", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "address"}, name = "restaurants_unique_idx"))
public class Restaurant extends AbstractNamedEntity {

    @Column(name = "address")
    @Size(max = 1024)
    @SafeHtml
    @Nullable
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("date DESC")
    @JsonManagedReference
    private List<Dish> menu;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name, @Nullable String address) {
        super(id, name);
        this.address = address;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
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
