package org.restaurant.voting.util;

import org.restaurant.voting.model.Role;
import org.restaurant.voting.model.User;
import org.restaurant.voting.to.UserTo;

public class UserUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), true, Role.USER);
    }

    public static UserTo createTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail().toLowerCase(), user.getPassword());
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail());
        user.setPassword(userTo.getPassword());
        return user;
    }
}
