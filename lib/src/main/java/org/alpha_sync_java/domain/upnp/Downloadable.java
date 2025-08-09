package org.alpha_sync_java.domain.upnp;

import java.net.URL;
import java.util.Map;

public interface Downloadable {
    Map<? extends ResolutionSelector, URL> getRes();
    String getDcTitle();
    String getId();
}
