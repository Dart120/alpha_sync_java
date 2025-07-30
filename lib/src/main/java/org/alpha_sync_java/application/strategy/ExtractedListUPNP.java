package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.domain.upnp.UPNPObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public record ExtractedListUPNP(Supplier<CompletableFuture<List<UPNPObject>>> value) implements Extracted {
}
