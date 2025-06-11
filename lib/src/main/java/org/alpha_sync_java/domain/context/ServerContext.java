package org.alpha_sync_java.domain.context;

import org.alpha_sync_java.domain.communication.LocatorURL;

import java.util.concurrent.CompletableFuture;

public class ServerContext {
    public CompletableFuture<LocatorURL> location = new CompletableFuture<>();
}
