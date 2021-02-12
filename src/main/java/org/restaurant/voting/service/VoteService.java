package org.restaurant.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.repository.CrudRestaurantRepository;
import org.restaurant.voting.repository.CrudUserRepository;
import org.restaurant.voting.repository.CrudVoteRepository;

import static java.time.LocalDate.now;
import static org.restaurant.voting.util.ValidationUtil.*;
import static org.restaurant.voting.util.VoteUtil.createNew;

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

    public Vote create(int restaurantId, int userId) {
        Assert.notNull(restaurantId, "Restaurant Id must be not null");
        Vote vote = createNew();
        return save(vote, restaurantId, userId);
    }

    @Transactional
    protected Vote save(Vote vote, int restaurantId, int userId) {
        if (!vote.isNew() && get(vote.getId(), userId) == null) {
            return null;
        }
        vote.setUser(checkNotFoundWithId(crudUserRepository.findById(userId), userId));
        vote.setRestaurant(checkNotFoundWithId(crudRestaurantRepository.findById(restaurantId), restaurantId));
        return crudVoteRepository.save(vote);
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(crudVoteRepository.findById(id)
                                                     .filter(vote -> vote.getUser().getId() == userId),
                                   id);
    }

    public Vote getLast(int userId) {
        return crudVoteRepository.getByDate(now(), userId);
    }

    public void update(int restaurantId, int userId) {
        Assert.notNull(restaurantId, "Restaurant Id must be not null");
        checkTimeOver();
        Vote vote = getLast(userId);
        vote.setVotingDate(now());
        save(vote, restaurantId, userId);
    }
}
