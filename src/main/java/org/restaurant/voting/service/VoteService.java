package org.restaurant.voting.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.repository.VoteRepository;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote get(int id, int userId) {
        return voteRepository.get(id, userId);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }

    public void update(Vote vote, int userId) {
        Assert.notNull(vote, "Vote must be not null");
        voteRepository.save(vote, userId);
    }

    public Vote create(Vote vote, int userId) {
        vote.setVotingDate(LocalDate.now());
        Assert.notNull(vote, "Vote must be not null");
        return voteRepository.save(vote, userId);
    }

    public void delete(int id, int userId) {
        voteRepository.delete(id, userId);
    }
}
