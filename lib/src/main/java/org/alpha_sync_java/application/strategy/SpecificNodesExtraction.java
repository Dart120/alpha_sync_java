package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.domain.extracted.ExtractedDownloadable;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.Downloadable;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.infra.Loggable;

import java.util.Map;
import java.util.regex.Pattern;

public class SpecificNodesExtraction implements ExtractionStrategy<ExtractedDownloadable>, Loggable {
    Pattern p = Pattern.compile("\\d\\d\\d\\d-\\d+-\\d+");
    @Override
    public <U extends UPNPObject> Boolean extract(U upnpObject, Map<String, ExtractedDownloadable> map, TreeExplorerState state) {
        switch (upnpObject){
            case UPNPContainer container -> {
                if (state.getDate() != null){
                    logger().info("Searching for {} in {}", state.getDate(), container.getDcTitle());
                    return p.matcher(container.getDcTitle()).find() && !state.getDate().equals(container.getDcTitle());
                }
                return false;

            }
            case Downloadable d -> {
                if (state.getIds().contains(d.getId())){
                    map.put(d.getId(),new ExtractedDownloadable(d));
                    if (state.getIds().stream().filter((a) -> !map.containsKey(a)).count() == 0){
                        state.foundItem();
                    }
                    return true;
                } else {
                    return false;
                }
            }
            default -> throw new IllegalStateException("Unexpected value in Specific Node Extraction: " + upnpObject);
        }
    }
}
