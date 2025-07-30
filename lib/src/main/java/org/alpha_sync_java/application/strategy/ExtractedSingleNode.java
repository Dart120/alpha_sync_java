package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.domain.upnp.UPNPObject;

public record ExtractedSingleNode(UPNPObject value) implements Extracted {
}
