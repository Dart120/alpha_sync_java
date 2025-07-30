package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.infra.Loggable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class DateExtraction implements ExtractionStrategy<ExtractedListUPNP>, Loggable {
    Pattern p = Pattern.compile("\\d\\d\\d\\d-\\d+-\\d+");

    @Override
    public <U extends UPNPObject> Boolean extract(U upnpObject, Map<String, ExtractedListUPNP> map, TreeExplorerState state) {
        if (upnpObject instanceof UPNPContainer container) {
            if (p.matcher(container.getDcTitle()).find()) {
                logger().info("Found date in: {}", container.getDcTitle());
                map.put(container.getDcTitle(), new ExtractedListUPNP(container.getChildren()));
                return true;
            }
        }
        return false;
    }
}
