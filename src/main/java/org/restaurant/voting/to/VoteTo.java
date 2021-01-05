package org.restaurant.voting.to;

import java.time.LocalDate;
import java.util.Objects;

public class VoteTo extends BaseTo {
    private LocalDate votingDate;
    private int userId;
    private int restaurantId;

    public VoteTo() {
    }

    public VoteTo(Integer id, LocalDate votingDate, int userId, int restaurantId) {
        super(id);
        this.votingDate = votingDate;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public LocalDate getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(LocalDate votingDate) {
        this.votingDate = votingDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        VoteTo voteTo = (VoteTo) o;
        return userId == voteTo.userId &&
                restaurantId == voteTo.restaurantId &&
                Objects.equals(id, voteTo.id) &&
                Objects.equals(votingDate, voteTo.votingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, votingDate, userId, restaurantId);
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "id=" + id +
                ", votingDate='" + votingDate +
                ", userId='" + userId +
                ", restaurantId='" + restaurantId +
                '}';
    }
}
