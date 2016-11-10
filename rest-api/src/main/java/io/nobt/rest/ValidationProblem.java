package io.nobt.rest;

import io.nobt.rest.payloads.SimpleViolation;
import org.zalando.problem.Problem;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class ValidationProblem implements Problem {

    private final Set<ConstraintViolation<?>> violations;

    public ValidationProblem(Set<ConstraintViolation<?>> violations) {
        this.violations = violations;
    }

    @Override
    public String getTitle() {
        return "The sent request was not valid.";
    }

    @Override
    public Response.StatusType getStatus() {
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
