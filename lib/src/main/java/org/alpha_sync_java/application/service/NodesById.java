package org.alpha_sync_java.application.service;

import org.alpha_sync_java.application.strategy.*;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.Downloadable;
import org.alpha_sync_java.domain.upnp.UPNPObject;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static java.util.Map.entry;

public class NodesById {

    UPNPObjectCachingService upnpObjectCachingService;
    UPNPTreeExplorer upnpTreeExplorer;

    public NodesById(UPNPObjectCachingService upnpObjectCachingService, UPNPTreeExplorer upnpTreeExplorer) {
        this.upnpObjectCachingService = upnpObjectCachingService;
        this.upnpTreeExplorer = upnpTreeExplorer;

    }

    public List<UPNPObject> get(Set<String> ids, String date, UPNPObject root){
        return upnpObjectCachingService.coCacheComputeIfAbsent(ids, leftoverIds -> {
            try {
                return upnpTreeExplorer
                        .explore(new SpecificNodeExtraction(), root , new TreeExplorerState.Builder().ids(leftoverIds).date(date).build())
                     .get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
