package org.alpha_sync_java.domain;

import java.util.concurrent.CompletableFuture;

public class ServerContext {
    public CompletableFuture<String> location = new CompletableFuture<>();
}
