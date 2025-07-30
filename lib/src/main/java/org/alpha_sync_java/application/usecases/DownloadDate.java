package org.alpha_sync_java.application.usecases;

import org.alpha_sync_java.application.interfaces.Downloader;
import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.application.strategy.*;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.Downloadable;
import org.alpha_sync_java.domain.upnp.PHOTO_RES;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.infra.NioDownloader;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DownloadDate {
    public void downloadDate(String date, UPNPContainer c) throws ExecutionException, InterruptedException {
        Downloader downloader = new NioDownloader();
        DownloadService downloadService = new DownloadService(downloader);
        NodeByDateExtraction nodeByDateExtraction = new NodeByDateExtraction();
        UPNPTreeExplorer ute = new UPNPTreeExplorer();
        CompletableFuture<Map<String, ExtractedDownloadable>> d = Objects.requireNonNull(ute.explore(nodeByDateExtraction, c, new TreeExplorerState
                .Builder()
                .date(date)
                .build()));

        List<Downloadable> ed =  d.get().values().stream().map(ExtractedDownloadable::value).toList();
        ed.forEach((down) -> {
            try {
                System.out.println(down.getDcTitle());
                downloadService.downloadToFile(down,"./" + date, PHOTO_RES.LRG).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
