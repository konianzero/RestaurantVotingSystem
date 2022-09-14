package org.restaurant.voting.web.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.restaurant.voting.HasId;
import org.restaurant.voting.model.User;
import org.restaurant.voting.service.UserService;
import org.restaurant.voting.to.UserTo;
import org.restaurant.voting.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.List;

import static org.restaurant.voting.util.validation.ValidationUtil.assureIdConsistent;
import static org.restaurant.voting.util.validation.ValidationUtil.checkNew;

@Slf4j
public abstract class AbstractUserController {

    @Autowired
    private UserService service;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private UniqueMailValidator emailValidator;

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public User create(UserTo userTo) {
        log.info("Create {}", userTo);
        checkNew(userTo);
        return service.create(mapper.toEntity(userTo));
    }

    public User create(User user) {
        log.info("Create {}", user);
        checkNew(user);
        return service.create(user);
    }

    public User get(int id) {
        log.info("Get user {}", id);
        return service.get(id);
    }

    public User getByEmail(String email) {
        log.info("Get user by Email {}", email);
        return service.getByEmail(email);
    }

    public List<User> getAll() {
        log.info("Get all users");
        return service.getAll();
    }

    public void update(UserTo userTo, int id) {
        log.info("Update {}", userTo);
        assureIdConsistent(userTo, id);
        service.update(userTo);
    }

    public void update(User user, int id) throws BindException {
        log.info("Update {}", user);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public void enable(int id, boolean enabled) {
        log.info(enabled ? "Enable {}" : "Disable {}", id);
        service.enable(id, enabled);
    }

    public void delete(int id) {
        log.info("Delete user {}", id);
        service.delete(id);
    }

    protected void validateBeforeUpdate(HasId user, int id) throws BindException {
        assureIdConsistent(user, id);
        DataBinder binder = new DataBinder(user);
        binder.addValidators(emailValidator, validator);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            throw new BindException(binder.getBindingResult());
        }
    }
}
