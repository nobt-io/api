package io.nobt.profiles;

import java.util.function.Predicate;

/**
 * Utility class for {@link Profile}s.
 */
public class Profiles {

    public static final Profile CURRENT = Profile.getCurrentProfile();

    public static void ifProfile(Predicate<Profile> predicate, Runnable task) {
        if (predicate.test(CURRENT)) {
            task.run();
        }
    }

    public static boolean notCloud(Profile candidate) {
        return candidate != Profile.CLOUD;
    }
}
