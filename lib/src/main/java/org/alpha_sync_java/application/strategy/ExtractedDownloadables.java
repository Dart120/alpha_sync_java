package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.domain.upnp.Downloadable;

import java.util.List;

public record ExtractedDownloadables(List<Downloadable> value) implements Extracted {
}
