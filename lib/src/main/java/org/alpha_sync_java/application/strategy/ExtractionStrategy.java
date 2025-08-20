package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.domain.extracted.Extracted;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.UPNPObject;

import java.util.Map;

public interface ExtractionStrategy<T extends Extracted> {

    <U extends UPNPObject> Boolean extract(U upnpObject, Map<String, T> map, TreeExplorerState state);
}
