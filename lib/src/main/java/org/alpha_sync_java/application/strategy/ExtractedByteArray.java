package org.alpha_sync_java.application.strategy;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public record ExtractedByteArray(Supplier<CompletableFuture<byte[]>> value) implements Extracted {
}
