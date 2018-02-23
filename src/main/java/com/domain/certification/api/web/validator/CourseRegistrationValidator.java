package com.domain.certification.api.web.validator;

import com.domain.certification.api.util.dto.Course.CourseRegistrationDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CourseRegistrationValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CourseRegistrationDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "courseId",
                "emptyField.courseRegistration.courseId",
                "courseId is empty or invalid. Please enter valid courseId."
        );
        if (errors.hasErrors()) return;

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "userId",
                "emptyField.courseRegistration.userId",
                "userId is empty or invalid. Please enter valid userId."
        );
        if (errors.hasErrors()) return;
    }
}
