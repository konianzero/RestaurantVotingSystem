package org.restaurant.voting.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.restaurant.voting.model.Vote;
import org.restaurant.voting.to.VoteTo;

@Mapper
public interface VoteMapper {
    VoteMapper INSTANCE = Mappers.getMapper(VoteMapper.class);

    @Mappings({
            @Mapping(target = "userId", source = "vote.user.id"),
            @Mapping(target = "restaurantId", source = "vote.restaurant.id")
    })
    VoteTo toToFromEntity(Vote vote);

    Vote toEntityFromTo(VoteTo voteTo);
}
