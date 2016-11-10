package io.nobt.core.validation;

import io.nobt.core.domain.Amount;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PositiveConstraintValidator implements ConstraintValidator<Positive, Amount> {

    @Override
    public void initialize(Positive constraintAnnotation) {

    }

    @Override
    public boolean isValid(Amount value, ConstraintValidatorContext context) {
        return value != null && value.isPositive();
    }
}
