package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.domain.extracted.ExtractedListUPNP;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.infra.Loggable;

import java.util.Map;
import java.util.regex.Pattern;

public class DateExtraction implements ExtractionStrategy<ExtractedListUPNP>, Loggable {
    Pattern p = Pattern.compile("\\d\\d\\d\\d-\\d+-\\d+");
    UpnpGetChildren upnpGetChildren;

    public DateExtraction(UpnpGetChildren upnpGetChildren) {
        this.upnpGetChildren = upnpGetChildren;
    }

    @Override
    public <U extends UPNPObject> Boolean extract(U upnpObject, Map<String, ExtractedListUPNP> map, TreeExplorerState state) {
        if (upnpObject instanceof UPNPContainer container) {
            if (p.matcher(container.getDcTitle()).find()) {
                logger().info("Found date in: {}", container.getDcTitle());
                map.put(container.getDcTitle(), new ExtractedListUPNP(upnpGetChildren.getChildren(container)));
                return true;
            }
        }
        return false;
    }
}
