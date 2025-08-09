package org.alpha_sync_java.application.interfaces;

import org.alpha_sync_java.domain.communication.LocatorURL;

import java.io.InputStream;
@FunctionalInterface
public interface RequestsXML {
    String getXML(LocatorURL url);
}
