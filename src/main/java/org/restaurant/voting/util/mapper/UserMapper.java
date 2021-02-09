package org.restaurant.voting.util.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import org.restaurant.voting.model.Role;
import org.restaurant.voting.model.User;
import org.restaurant.voting.to.UserTo;

import java.util.EnumSet;

@Mapper(imports = {EnumSet.class, Role.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target = "email", expression = "java(userTo.getEmail().toLowerCase())"),
            @Mapping(target = "enabled", constant = "true"),
            @Mapping(target = "roles", expression = "java(EnumSet.of(Role.USER))")
    })
    User toEntityFromTo(UserTo userTo);

    @Mapping(target = "email", expression = "java(user.getEmail().toLowerCase())")
    UserTo toToFromEntity(User user);

    @Mapping(target = "email", expression = "java(userTo.getEmail().toLowerCase())")
    void updateFromTo(@MappingTarget User user, UserTo userTo);
}
