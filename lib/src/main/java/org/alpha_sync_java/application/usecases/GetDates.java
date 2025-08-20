package org.alpha_sync_java.application.usecases;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.application.strategy.*;
import org.alpha_sync_java.domain.extracted.ExtractedListUPNP;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.UPNPContainer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GetDates {
    public List<String> execute(UPNPContainer c, DateExtraction dateExtraction, UPNPTreeExplorer ute, UpnpGetChildren upnpGetChildren) throws ExecutionException, InterruptedException {
        CompletableFuture<Map<String, ExtractedListUPNP>> m = Objects.requireNonNull(ute.explore(dateExtraction, c, new TreeExplorerState.Builder().build(), upnpGetChildren));
        return m.get().keySet().stream().toList();
    }
}
