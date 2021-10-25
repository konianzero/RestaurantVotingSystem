package org.restaurant.voting.util;

import org.restaurant.voting.model.User;
import org.restaurant.voting.to.UserTo;
import org.restaurant.voting.util.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class UserUtil {

    private UserUtil() {
    }

    public static User createNewFromTo(UserTo userTo) {
        return UserMapper.INSTANCE.toEntityFromTo(userTo);
    }

    public static UserTo createTo(User user) {
        return UserMapper.INSTANCE.toToFromEntity(user);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        UserMapper.INSTANCE.updateFromTo(user, userTo);
        return user;
    }

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
