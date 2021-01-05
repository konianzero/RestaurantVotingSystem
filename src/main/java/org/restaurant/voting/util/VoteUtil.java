package org.restaurant.voting.util;

import java.util.List;
import java.util.stream.Collectors;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.to.VoteTo;

public class VoteUtil {

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getVotingDate(), vote.getUser().getId(), vote.getRestaurant().getId());
    }

    public static List<VoteTo> getTos(List<Vote> votes) {
        return votes.stream()
                    .map(VoteUtil::createTo)
                    .collect(Collectors.toList());
    }

    public static Vote createNewFromTo(VoteTo voteTo) {
        return new Vote(voteTo.getId(), null, null, voteTo.getVotingDate());
    }
}
