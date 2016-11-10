package io.nobt.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {PositiveConstraintValidator.class})
@ReportAsSingleViolation
public @interface Positive {

    String message() default "Value must be positive.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
