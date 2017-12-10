package io.nobt.test.domain.provider;

import java.util.Random;

public final class IDProvider {

    private static final Random ID_PROVIDER = new Random(42L);

    private IDProvider() {
    }

    public static long nextId() {
        return ID_PROVIDER.nextLong();
    }
}
