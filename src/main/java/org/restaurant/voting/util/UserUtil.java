package org.restaurant.voting.util;

import org.restaurant.voting.model.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class UserUtil {

    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private UserUtil() {
    }

    public static User prepareToSave(User user) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? PASSWORD_ENCODER.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
