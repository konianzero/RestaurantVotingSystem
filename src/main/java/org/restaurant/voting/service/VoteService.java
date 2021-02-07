package org.restaurant.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.model.Restaurant;
import org.restaurant.voting.model.User;
import org.restaurant.voting.repository.CrudRestaurantRepository;
import org.restaurant.voting.repository.CrudUserRepository;
import org.restaurant.voting.repository.CrudVoteRepository;
import org.restaurant.voting.to.VoteTo;

import static java.time.LocalDate.now;
import static org.restaurant.voting.util.ValidationUtil.*;
import static org.restaurant.voting.util.VoteUtil.createNewFromTo;

@Service
public class VoteService {

    private final CrudVoteRepository crudVoteRepository;
    private final CrudUserRepository crudUserRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public VoteService(CrudVoteRepository crudVoteRepository, CrudUserRepository crudUserRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudVoteRepository = crudVoteRepository;

        this.crudUserRepository = crudUserRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    public Vote create(VoteTo voteTo, int userId) {
        Assert.notNull(voteTo, "Vote must be not null");
        voteTo.setVotingDate(now());
        Vote vote = createNewFromTo(voteTo);
        return save(vote, voteTo.getRestaurantId(), userId);
    }

    @Transactional
    protected Vote save(Vote vote, int restaurantId, int userId) {
        if (!vote.isNew() && get(vote.getId(), userId) == null) {
            return null;
        }
        vote.setUser(crudUserRepository.findById(userId, User.class));
        vote.setRestaurant(crudRestaurantRepository.findById(restaurantId, Restaurant.class));
        return crudVoteRepository.save(vote);
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(crudVoteRepository.findById(id)
                                                     .filter(vote -> vote.getUser().getId() == userId)
                                                     .orElse(null),
                                   id);
    }

    public Vote getLast(int userId) {
        return crudVoteRepository.getByDate(now(), userId);
    }

    public void update(VoteTo voteTo, int userId) {
        Assert.notNull(voteTo, "Vote must be not null");
        voteTo.setVotingDate(now());
        checkTimeOver();
        Vote vote = get(voteTo.getId(), userId);
        vote.setVotingDate(voteTo.getVotingDate());
        checkNotFoundWithId(save(vote, voteTo.getRestaurantId(), userId), voteTo.getId());
    }
}
