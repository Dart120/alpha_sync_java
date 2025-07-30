package org.alpha_sync_java.application.service;

import org.alpha_sync_java.application.strategy.Extracted;
import org.alpha_sync_java.application.strategy.ExtractionStrategy;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;


public class UPNPTreeExplorer {
// TODO refactor state into a class
    public <T extends Extracted> CompletableFuture<Map<String,T>>  explore(ExtractionStrategy<T> strategy, UPNPObject node, TreeExplorerState state) {
        Map<String,T> map = new HashMap<>();
        return exploreHelper(map, strategy, node, state).thenApply((a) -> map);

    }

    public <T extends Extracted> CompletableFuture<Void> exploreHelper(Map<String, T> map, ExtractionStrategy<T> strategy, UPNPObject node, TreeExplorerState state){
        if (state.haveFoundItem()){
            return CompletableFuture.completedFuture(null);
        }
        boolean path_exhausted = strategy.extract(node, map, state);
        if (node instanceof UPNPContainer container && !path_exhausted) {
            return container
                    .getChildren()
                    .get()
                    .thenCompose(children ->
                            CompletableFuture.allOf(
                                    children
                                            .stream()
                                            .map(child -> exploreHelper(map, strategy, child, state))
                                            .toList()
                                            .toArray(new CompletableFuture[0])));

        } else {
            return CompletableFuture.completedFuture(null);
        }


        }

    }
