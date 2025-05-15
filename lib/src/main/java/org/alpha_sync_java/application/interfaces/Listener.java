package org.alpha_sync_java.application.interfaces;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Listener {
    public void listener(Function<String, String> callback) throws Exception;
}
