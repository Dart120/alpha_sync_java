package org.alpha_sync_java.application.service;

import org.alpha_sync_java.application.interfaces.Downloader;
import org.alpha_sync_java.domain.upnp.*;

import java.util.concurrent.CompletableFuture;

public class DownloadService {
    Downloader downloader;

    public DownloadService(Downloader downloader) {
        this.downloader = downloader;
    }

    public CompletableFuture<Void> downloadToFile(Downloadable node, String path, ResolutionSelector resolutionSelector) throws Exception {

            return downloader.downloadToFile(node.getRes().get(resolutionSelector), path, node.getDcTitle());
    }

    public CompletableFuture<byte[]> downloadToBytes(Downloadable node, ResolutionSelector resolutionSelector) throws Exception {
            return downloader.downloadToBytes(node.getRes().get(resolutionSelector));
    }
}
