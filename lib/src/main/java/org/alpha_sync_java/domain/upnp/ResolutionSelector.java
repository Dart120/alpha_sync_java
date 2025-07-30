package org.alpha_sync_java.domain.upnp;

public sealed interface ResolutionSelector<T extends Enum<T>> permits PHOTO_RES, VIDEO_RES {
    T default_value();

}