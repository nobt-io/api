package io.nobt.rest.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = CheckNoDuplicateDebtorsValidator.class)
@Documented
public @interface CheckNoDuplicateDebtors {

    String message() default "Debtors names must be unique for a single expense";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
