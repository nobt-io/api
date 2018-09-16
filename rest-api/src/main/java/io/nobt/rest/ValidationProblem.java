package io.nobt.rest;

import io.nobt.rest.payloads.SimpleViolation;
import org.zalando.problem.Problem;
import org.zalando.problem.StatusType;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.zalando.problem.Status.BAD_REQUEST;

public class ValidationProblem implements Problem {

    private final Set<ConstraintViolation<?>> violations;

    public ValidationProblem(Set<ConstraintViolation<?>> violations) {
        this.violations = violations;
    }

    @Override
    public String getTitle() {
        return "The sent request was not valid.";
    }

    @Nullable
    @Override
    public String getDetail() {
        return "At least one element in the sent request is invalid. Please refer to the list of errors below.";
    }

    @Override
    public StatusType getStatus() {
        return BAD_REQUEST;
    }

    @Override
    public Map<String, Object> getParameters() {

        final List<Object> errors = violations
                .stream()
                .map(cv -> new SimpleViolation(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(toList());

        return new HashMap<String, Object>() {{
            put("errors", errors);
        }};
    }
}
