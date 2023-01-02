package ru.practicum.ewm_service.config.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateDate.DateValidator.class)
@Documented
public @interface ValidateDate {

    String message() default "{ru.practicum.ewm_service.config.validator.ValidateDate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class DateValidator implements ConstraintValidator<ValidateDate, Timestamp> {
        private static final Timestamp MIN_TIME_START = Timestamp.valueOf(LocalDateTime.now().plusHours(2));

        @Override
        public boolean isValid(Timestamp value, ConstraintValidatorContext context) {
            return value.after(MIN_TIME_START);
        }
    }
}
