package org.alpha_sync_java.application.interfaces;

import java.io.InputStream;
@FunctionalInterface
public interface XMLExplorerFactory {
    XMLExplorer create(String xml) throws RuntimeException;
}
