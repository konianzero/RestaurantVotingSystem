package org.restaurant.voting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "votes", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "voting_date"}, name = "votes_unique_idx"))
@NoArgsConstructor
@Getter
@Setter
public class Vote extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100000"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100000"))
    private Restaurant restaurant;

    @Column(name = "voting_date", nullable = false, columnDefinition = "DATE DEFAULT now()")
    @NotNull
    private LocalDate votingDate;

    public Vote(Integer id, User user, Restaurant restaurant, LocalDate votingDate) {
        super(id);
        this.user = user;
        this.restaurant = restaurant;
        this.votingDate = votingDate;
    }
}
