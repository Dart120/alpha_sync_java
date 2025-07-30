package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.domain.upnp.Downloadable;
import org.alpha_sync_java.domain.upnp.UPNPObject;

public record ExtractedDownloadable(Downloadable value) implements Extracted {
}
