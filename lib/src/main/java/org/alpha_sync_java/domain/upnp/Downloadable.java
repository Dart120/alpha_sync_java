package org.alpha_sync_java.domain.upnp;

import java.net.URL;
import java.util.Map;

public interface Downloadable {
    public Map<? extends ResolutionSelector, URL> getRes();
    public String getDcTitle();
    String getId();
}
