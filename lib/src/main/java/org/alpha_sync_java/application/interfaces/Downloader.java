package org.alpha_sync_java.application.interfaces;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public interface Downloader {
    CompletableFuture<Void> downloadToFile(URL url,String path, String filename) throws Exception;
    CompletableFuture<byte[]> downloadToBytes(URL url) throws Exception;
}
