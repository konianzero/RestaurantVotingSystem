package org.restaurant.voting.util;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.restaurant.voting.model.Vote;
import org.restaurant.voting.to.VoteTo;
import org.restaurant.voting.util.mapper.VoteMapper;

public class VoteUtil {

    public static VoteTo createTo(Vote vote) {
        return VoteMapper.INSTANCE.toToFromEntity(vote);
    }

    public static List<VoteTo> getTos(List<Vote> votes) {
        return votes.stream()
                    .map(VoteUtil::createTo)
                    .collect(Collectors.toList());
    }

    public static Vote createNew() {
        return new Vote(null, null, null, LocalDate.now());
    }
}
