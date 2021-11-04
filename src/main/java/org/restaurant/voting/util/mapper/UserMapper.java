package org.restaurant.voting.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.restaurant.voting.model.Role;
import org.restaurant.voting.model.User;
import org.restaurant.voting.to.UserTo;

import java.util.EnumSet;

@Mapper(componentModel="spring", imports={EnumSet.class, Role.class})
public interface UserMapper {

    @Mapping(target = "email", expression = "java(userTo.getEmail().toLowerCase())")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "roles", expression = "java(EnumSet.of(Role.USER))")
    User toEntity(UserTo userTo);

    @Mapping(target = "email", expression = "java(user.getEmail().toLowerCase())")
    UserTo toTo(User user);

    @Mapping(target = "email", expression = "java(userTo.getEmail().toLowerCase())")
    void updateEntity(@MappingTarget User user, UserTo userTo);
}
