package org.restaurant.voting.service;

import org.restaurant.voting.AuthorizedUser;
import org.restaurant.voting.model.User;
import org.restaurant.voting.repository.CrudUserRepository;
import org.restaurant.voting.to.UserTo;
import org.restaurant.voting.util.UserUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static org.restaurant.voting.util.UserUtil.prepareToSave;
import static org.restaurant.voting.util.validation.ValidationUtil.checkNotFound;
import static org.restaurant.voting.util.validation.ValidationUtil.checkNotFoundWithId;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {
    private static final Sort SORT_BY_DATE = Sort.by(Sort.Direction.DESC, "registered");

    private final CrudUserRepository crudUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(CrudUserRepository crudUserRepository, PasswordEncoder passwordEncoder) {
        this.crudUserRepository = crudUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        Assert.notNull(user, "User must be not null");
        return prepareAndSave(user);
    }

    public User get(int id) {
        return checkNotFoundWithId(crudUserRepository.findById(id), id);
    }

    @Cacheable("users")
    public User getByEmail(String email) {
        Assert.notNull(email, "Email must be not null");
        return checkNotFound(crudUserRepository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        return crudUserRepository.findAll(SORT_BY_DATE);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(int id, boolean enabled) {
        User user = get(id);
        user.setEnabled(enabled);
        save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void update(User user) {
        Assert.notNull(user, "User must be not null");
        prepareAndSave(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(UserTo userTo) {
        Assert.notNull(userTo, "UserTo must be not null");
        User user = get(userTo.getId());
        User updated = UserUtil.updateFromTo(user, userTo);
        prepareAndSave(updated);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        crudUserRepository.deleteExisted(id);
    }

    private User prepareAndSave(User user) {
        return save(prepareToSave(user, passwordEncoder));
    }

    @Transactional
    public User save(User user) {
        return crudUserRepository.save(user);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = crudUserRepository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
