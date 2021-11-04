package org.restaurant.voting.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.restaurant.voting.model.Vote;
import org.restaurant.voting.to.VoteTo;

import java.util.List;

@Mapper(componentModel="spring")
public interface VoteMapper {

    @Mapping(target = "userId", source = "vote.user.id")
    @Mapping(target = "restaurantId", source = "vote.restaurant.id")
    VoteTo toTo(Vote vote);

    List<VoteTo> getToList(List<Vote> votes);
}
