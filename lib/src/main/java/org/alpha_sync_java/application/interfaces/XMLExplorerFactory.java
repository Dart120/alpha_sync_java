package org.alpha_sync_java.application.interfaces;

import java.io.InputStream;
@FunctionalInterface
public interface XMLExplorerFactory {
    public XMLExplorer create(String xml) throws RuntimeException;
}
