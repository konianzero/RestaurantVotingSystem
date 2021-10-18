package org.restaurant.voting;

import org.restaurant.voting.model.Role;
import org.restaurant.voting.model.User;
import org.restaurant.voting.util.JsonUtil;

import static org.restaurant.voting.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int NOT_FOUND = 100;
    public static final int ADMIN_ID = START_SEQ;
    public static final int USER_ID = START_SEQ + 1;

    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String USER_EMAIL = "user@gmail.com";

    public static final User ADMIN = new User(ADMIN_ID, "Mike", "admin@gmail.com", "admin", true, Role.ADMIN, Role.USER);
    public static final User USER = new User(USER_ID, "Nick", "user@gmail.com", "userpass", true, Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "password", true, Role.USER);
    }

    public static User getUpdated() {
        return new User(ADMIN_ID, "Mike", "newadmin@gmail.com", "newPass", true, Role.ADMIN, Role.USER);
    }

    public static String jsonWithPassword(User user, String pass) {
        return JsonUtil.writeAdditionProps(user, "password", pass);
    }
}
