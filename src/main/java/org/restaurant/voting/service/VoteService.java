package org.restaurant.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.repository.VoteRepository;
import org.restaurant.voting.to.VoteTo;

import static org.restaurant.voting.util.ValidationUtil.*;
import static org.restaurant.voting.util.VoteUtil.createNewFromTo;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote create(VoteTo voteTo, int userId) {
        Assert.notNull(voteTo, "Vote must be not null");
        voteTo.setVotingDate(LocalDate.now());
        Vote vote = createNewFromTo(voteTo);
        return voteRepository.save(vote, voteTo.getRestaurantId(), userId);
    }

    public Vote get(int id, int userId) {
        return checkNotFoundWithId(voteRepository.get(id, userId), id);
    }

    public List<Vote> getAll() {
        return voteRepository.getAll();
    }

    public List<Vote> getAllByUser(int userId) {
        return voteRepository.getAllByUser(userId);
    }

    public List<Vote> getAllByRestaurant(int restaurantId) {
        return voteRepository.getAllByRestaurant(restaurantId);
    }

    public void update(VoteTo voteTo, int userId) {
        Assert.notNull(voteTo, "Vote must be not null");
        voteTo.setVotingDate(LocalDate.now());
        checkTimeOver();
        Vote vote = get(voteTo.getId(), userId);
        vote.setVotingDate(voteTo.getVotingDate());
        checkNotFoundWithId(voteRepository.save(vote, voteTo.getRestaurantId(), userId), voteTo.getId());
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(voteRepository.delete(id, userId), id);
    }
}
