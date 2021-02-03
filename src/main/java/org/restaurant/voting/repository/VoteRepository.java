package org.restaurant.voting.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import org.restaurant.voting.model.Vote;

@Repository
public class VoteRepository {

    private final CrudVoteRepository crudVoteRepository;
    private final CrudUserRepository crudUserRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public VoteRepository(CrudVoteRepository crudVoteRepository, CrudUserRepository crudUserRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudVoteRepository = crudVoteRepository;
        this.crudUserRepository = crudUserRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Transactional
    public Vote save(Vote vote, int restaurantId, int userId) {
        if (!vote.isNew() && get(vote.getId(), userId) == null) {
            return null;
        }
        vote.setUser(crudUserRepository.findById(userId).orElseThrow());
        vote.setRestaurant(crudRestaurantRepository.findById(restaurantId).orElseThrow());
        return crudVoteRepository.save(vote);
    }

    public Vote get(int voteId, int userId) {
        return crudVoteRepository.findById(voteId)
                                 .filter(vote -> vote.getUser().getId() == userId)
                                 .orElse(null);
    }

    public Vote getLast(LocalDate now, int userId) {
        return crudVoteRepository.getByDate(now, userId);
    }
}
