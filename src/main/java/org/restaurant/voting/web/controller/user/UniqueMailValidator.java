package org.restaurant.voting.web.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import org.restaurant.voting.HasIdAndEmail;
import org.restaurant.voting.model.User;
import org.restaurant.voting.repository.CrudUserRepository;

@Component
@RequiredArgsConstructor
public class UniqueMailValidator implements org.springframework.validation.Validator {

    private final CrudUserRepository crudUserRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            User dbUser = crudUserRepository.getByEmail(user.getEmail().toLowerCase());
            if (dbUser != null && !dbUser.getId().equals(user.getId())) {
                errors.rejectValue("email", "User with this email already exists");
            }
        }
    }
}
