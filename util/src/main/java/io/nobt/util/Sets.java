package io.nobt.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Sets {

	public static <T> Set<T> newHashSet(T... elements) {
		return new HashSet<>(Arrays.asList(elements));
	}
}
