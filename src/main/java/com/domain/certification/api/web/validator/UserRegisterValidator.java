package com.domain.certification.api.web.validator;

import com.domain.certification.api.util.CoreService;
import com.domain.certification.api.util.dto.user.UserRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserRegisterValidator implements Validator {

    private final CoreService coreService;

    public UserRegisterValidator(CoreService coreService) {
        this.coreService = coreService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRequestDTO requestDTO = (UserRequestDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "email",
                "emptyField.userRegister.email",
                "email is empty or invalid. Please enter valid email."
        );
        if (errors.hasErrors()) return;

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "password",
                "emptyField.userRegister.password",
                "password is empty or invalid. Please enter valid password."
        );
        if (errors.hasErrors()) return;

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "name",
                "emptyField.userRegister.name",
                "name is empty or invalid. Please enter valid name."
        );
        if (errors.hasErrors()) return;

        if (!coreService.validateEmail(requestDTO.getEmail())) {
            errors.rejectValue(
                    "email",
                    "constraintViolation.userRegister.email",
                    "email invalid. Please enter valid email."
            );
        }

        if (requestDTO.getPassword().length() < 6) {
            errors.rejectValue(
                    "password",
                    "constraintViolation.userRegister.password",
                    "password length should be at least 6 characters."
            );
        }
    }
}
