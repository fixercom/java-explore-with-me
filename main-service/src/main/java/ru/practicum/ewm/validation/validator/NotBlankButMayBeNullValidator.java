package ru.practicum.ewm.validation.validator;


import ru.practicum.ewm.validation.annotation.NotBlankButMayBeNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankButMayBeNullValidator implements ConstraintValidator<NotBlankButMayBeNull, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !value.isBlank();
    }

}
