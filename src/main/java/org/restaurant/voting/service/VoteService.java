package org.restaurant.voting.service;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.repository.CrudRestaurantRepository;
import org.restaurant.voting.repository.CrudUserRepository;
import org.restaurant.voting.repository.CrudVoteRepository;
import org.restaurant.voting.util.exception.DataConflictException;
import org.restaurant.voting.util.exception.VotingTimeOverException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.LocalDate.now;
import static org.restaurant.voting.util.validation.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {

    @Value("${app.vote_endtime}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime voteEndTime;

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
        Vote vote = new Vote(null, null, null, LocalDate.now());
        return save(vote, restaurantId, userId);
    }

    public Vote save(Vote vote, int restaurantId, int userId) {
        vote.setUser(checkNotFoundWithId(crudUserRepository.findById(userId), userId));
        vote.setRestaurant(checkNotFoundWithId(crudRestaurantRepository.findById(restaurantId), restaurantId));
        return crudVoteRepository.save(vote);
    }

    public List<Vote> getAllByUserId(int userId) {
        return crudVoteRepository.getByUserId(userId);
    }

    public Vote getLast(int userId) {
        return checkNotFoundWithId(crudVoteRepository.getByUserIdAndDate(now(), userId), userId);
    }

    @Transactional
    public void update(int restaurantId, int userId) {
        Assert.isTrue(restaurantId != 0, "Restaurant Id must be not null");
        checkTimeOver();

        Vote vote = crudVoteRepository.getByUserIdAndDate(now(), userId)
                                      .orElseThrow(() -> new DataConflictException("Have not voted today"));
        vote.setVotingDate(now());
        save(vote, restaurantId, userId);
    }

    public void checkTimeOver() {
        if (isVotingTimeOver()) {
            throw new VotingTimeOverException("It's " + LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + ". Time for voting is over!");
        }
    }

    public boolean isVotingTimeOver() {
        return LocalTime.now().isAfter(voteEndTime);
    }
}
