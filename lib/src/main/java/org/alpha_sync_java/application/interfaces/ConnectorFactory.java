package org.alpha_sync_java.application.interfaces;

import java.io.InputStream;

@FunctionalInterface
public interface ConnectorFactory {
    Connector create(String address, String port);
}
