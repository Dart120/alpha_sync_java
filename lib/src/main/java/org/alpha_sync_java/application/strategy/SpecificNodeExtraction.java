package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.domain.extracted.ExtractedDownloadable;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.*;
import org.alpha_sync_java.infra.Loggable;

import java.util.Map;
import java.util.regex.Pattern;

public class SpecificNodeExtraction implements ExtractionStrategy<ExtractedDownloadable>, Loggable {
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
                if (d.getId().equals(state.getId())){
                    map.put(d.getId(),new ExtractedDownloadable(d));
                    state.foundItem();
                    return true;
                } else {
                    return false;
                }
            }
            default -> throw new IllegalStateException("Unexpected value in Specific Node Extraction: " + upnpObject);
        }
    }
}
