package org.alpha_sync_java.application.service;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.strategy.*;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.Downloadable;
import org.alpha_sync_java.domain.upnp.UPNPObject;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class NodesById {

    UPNPObjectCachingService upnpObjectCachingService;
    UPNPTreeExplorer upnpTreeExplorer;
    UpnpGetChildren upnpGetChildren;

    public NodesById(UPNPObjectCachingService upnpObjectCachingService, UPNPTreeExplorer upnpTreeExplorer, UpnpGetChildren upnpGetChildren) {
        this.upnpObjectCachingService = upnpObjectCachingService;
        this.upnpTreeExplorer = upnpTreeExplorer;
this.upnpGetChildren = upnpGetChildren;
    }

    public List<UPNPObject> get(Set<String> ids, String date, UPNPObject root){
        return upnpObjectCachingService.coCacheComputeIfAbsent(ids, leftoverIds -> {
            try {
                return upnpTreeExplorer
                        .explore(new SpecificNodeExtraction(), root , new TreeExplorerState.Builder()
                                .ids(leftoverIds)
                                .date(date)
                                .build(), upnpGetChildren)
                     .get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
