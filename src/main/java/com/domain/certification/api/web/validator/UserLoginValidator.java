package com.domain.certification.api.web.validator;

import com.domain.certification.api.util.dto.user.UserLoginRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserLoginValidator  implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserLoginRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "email",
                "emptyField.userLogin.email",
                "email is empty or invalid. Please enter valid email."
        );
        if (errors.hasErrors()) return;

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "password",
                "emptyField.userLogin.password",
                "password is empty or invalid. Please enter valid password."
        );
        if (errors.hasErrors()) return;
    }
}
