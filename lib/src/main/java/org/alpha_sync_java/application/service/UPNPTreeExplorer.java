package org.alpha_sync_java.application.service;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.domain.extracted.Extracted;
import org.alpha_sync_java.application.strategy.ExtractionStrategy;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class UPNPTreeExplorer {
    public <T extends Extracted> CompletableFuture<Map<String,T>>  explore(ExtractionStrategy<T> strategy, UPNPObject node, TreeExplorerState state, UpnpGetChildren upnpGetChildren) {
        Map<String,T> map = new HashMap<>();
        return exploreHelper(map, strategy, node, state, upnpGetChildren).thenApply((a) -> map);

    }

    public <T extends Extracted> CompletableFuture<Void> exploreHelper(Map<String, T> map, ExtractionStrategy<T> strategy, UPNPObject node, TreeExplorerState state, UpnpGetChildren upnpGetChildren){
        if (state.haveFoundItem()){
            return CompletableFuture.completedFuture(null);
        }
        boolean path_exhausted = strategy.extract(node, map, state);
        if (node instanceof UPNPContainer container && !path_exhausted) {
            return upnpGetChildren.getChildren(container)
                    .get()
                    .thenCompose(children ->
                            CompletableFuture.allOf(
                                    children
                                            .stream()
                                            .map(child -> exploreHelper(map, strategy, child, state, upnpGetChildren))
                                            .toList()
                                            .toArray(new CompletableFuture[0])));

        } else {
            return CompletableFuture.completedFuture(null);
        }


        }

    }
