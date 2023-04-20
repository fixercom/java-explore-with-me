package ru.practicum.ewm.validation.validator;


import ru.practicum.ewm.util.DateUtils;
import ru.practicum.ewm.validation.annotation.AtLeastTwoHoursAfterCurrentTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class AtLeastTwoHoursAfterCurrentTimeValidator implements
        ConstraintValidator<AtLeastTwoHoursAfterCurrentTime, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return value.isAfter(DateUtils.now().plusHours(2));
    }

}
