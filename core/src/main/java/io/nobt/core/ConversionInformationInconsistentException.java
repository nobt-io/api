package io.nobt.core;

import io.nobt.core.domain.Nobt;

public class ConversionInformationInconsistentException extends RuntimeException {

    private final Nobt nobt;

    public ConversionInformationInconsistentException(Nobt nobt) {
        this.nobt = nobt;
    }

    @Override
    public String getMessage() {
        return String.format("Conversion information is not consistent with currency stored in nobt. Either supply a currency different from the nobt currency !(%s) or a rate of 1.", nobt.getCurrencyKey().getKey());
    }
}
