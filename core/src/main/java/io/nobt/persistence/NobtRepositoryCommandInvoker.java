package io.nobt.persistence;

import java.io.Closeable;

public interface NobtRepositoryCommandInvoker extends Closeable {

    <T> T invoke(NobtRepositoryCommand<T> command);

}
