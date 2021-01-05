package org.restaurant.voting.repository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.restaurant.voting.model.Vote;

@Repository
public class VoteRepository {
    private static final Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "votingDate");

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

    public List<Vote> getAll() { return crudVoteRepository.findAll(SORT_BY_DATE); }

    public List<Vote> getAllByUser(int userId) {
        return crudVoteRepository.getAllByUserId(userId);
    }

    public List<Vote> getAllByRestaurant(int restaurantId) {
        return crudVoteRepository.getAllByRestaurantId(restaurantId);
    }

    public boolean delete(int voteId, int userId) {
        return crudVoteRepository.delete(voteId, userId) != 0;
    }

}
