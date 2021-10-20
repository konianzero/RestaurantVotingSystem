package org.restaurant.voting.service;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.repository.CrudRestaurantRepository;
import org.restaurant.voting.repository.CrudUserRepository;
import org.restaurant.voting.repository.CrudVoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static java.time.LocalDate.now;
import static org.restaurant.voting.util.VoteUtil.createNew;
import static org.restaurant.voting.util.validation.ValidationUtil.checkNotFoundWithId;
import static org.restaurant.voting.util.validation.ValidationUtil.checkTimeOver;

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

    @Transactional
    public Vote create(int restaurantId, int userId) {
        Assert.isTrue(restaurantId != 0, "Restaurant Id must be not null");
        Vote vote = createNew();
        return save(vote, restaurantId, userId);
    }

    public Vote save(Vote vote, int restaurantId, int userId) {
        if (!vote.isNew() && get(userId) == null) {
            return null;
        }
        vote.setUser(checkNotFoundWithId(crudUserRepository.findById(userId), userId));
        vote.setRestaurant(checkNotFoundWithId(crudRestaurantRepository.findById(restaurantId), restaurantId));
        return crudVoteRepository.save(vote);
    }

    public List<Vote> get(int userId) {
        return crudVoteRepository.getByUserId(userId);
    }

    public Vote getLast(int userId) {
        return crudVoteRepository.getByUserIdAndDate(now(), userId);
    }

    @Transactional
    public void update(int restaurantId, int userId) {
        Assert.isTrue(restaurantId != 0, "Restaurant Id must be not null");
        checkTimeOver();
        Vote vote = getLast(userId);
        vote.setVotingDate(now());
        save(vote, restaurantId, userId);
    }
}
