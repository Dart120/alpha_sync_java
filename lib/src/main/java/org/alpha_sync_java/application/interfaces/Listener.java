package org.alpha_sync_java.application.interfaces;

import java.util.function.Function;

public interface Listener {
    public void listen(String port, Function<String, Boolean> callback) throws Exception;
}
