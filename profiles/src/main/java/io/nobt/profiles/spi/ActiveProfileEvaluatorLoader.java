package io.nobt.profiles.spi;

import io.nobt.profiles.ActiveProfileEvaluator;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ActiveProfileEvaluatorLoader {

    public static ActiveProfileEvaluator load() {

        final List<ActiveProfileEvaluator> evaluators = StreamSupport
                .stream(ServiceLoader.load(ActiveProfileEvaluator.class).spliterator(), false)
                .collect(Collectors.toList());

        if (evaluators.size() > 1) {
            throw new ActiveProfileEvaluatorLoaderException("There must not be more than one ActiveProfileEvaluator available.");
        }

        if (evaluators.size() == 0) {
            throw new ActiveProfileEvaluatorLoaderException("No ActiveProfileEvaluator was found.");
        }

        return evaluators.get(0);
    }
}
