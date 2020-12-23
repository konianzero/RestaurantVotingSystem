package org.restaurant.voting.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.restaurant.voting.model.Vote;

@Repository
public class VoteRepository {
    private final CrudVoteRepository crudVoteRepository;
    private final CrudUserRepository crudUserRepository;

    public VoteRepository(CrudVoteRepository crudVoteRepository, CrudUserRepository crudUserRepository) {
        this.crudVoteRepository = crudVoteRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Transactional
    public Vote save(Vote vote, int userId) {
        if (!vote.isNew() && get(vote.getId(), userId) == null) {
            return null;
        }
        vote.setUser(crudUserRepository.getOne(userId));
        return crudVoteRepository.save(vote);
    }

    public Vote get(int voteId, int userId) {
        return crudVoteRepository.findById(voteId)
                                 .filter(vote -> vote.getUser().getId() == userId)
                                 .orElse(null);
    }

    public List<Vote> getAll(int userId) {
        return crudVoteRepository.getAllByUserId(userId);
    }

    public boolean delete(int voteId, int userId) {
        return crudVoteRepository.delete(voteId, userId) != 0;
    }

}
