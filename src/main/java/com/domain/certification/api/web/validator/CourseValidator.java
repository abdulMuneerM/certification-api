package com.domain.certification.api.web.validator;

import com.domain.certification.api.util.dto.Course.CourseDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
public class CourseValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CourseDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseDTO courseDTO = (CourseDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "name",
                "emptyField.course.name",
                "name is empty or invalid. Please enter valid name."
        );
        if (errors.hasErrors()) return;

        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors,
                "cost",
                "emptyField.course.cost",
                "cost is empty or invalid. Please enter valid cost."
        );
        if (errors.hasErrors()) return;

        if (courseDTO.getCost().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue(
                    "cost",
                    "constraintViolation.course.cost",
                    "Cost should not leass than zero."
            );
        }
    }
}
