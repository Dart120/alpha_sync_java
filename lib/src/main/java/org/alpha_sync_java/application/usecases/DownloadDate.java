package org.alpha_sync_java.application.usecases;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.application.strategy.*;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DownloadDate {
    public void downloadDate(String date, UPNPContainer c, DownloadService downloadService, UpnpGetChildren upnpGetChildren) throws ExecutionException, InterruptedException {
        NodeByDateExtraction nodeByDateExtraction = new NodeByDateExtraction();
        UPNPTreeExplorer ute = new UPNPTreeExplorer();
        CompletableFuture<Map<String, ExtractedDownloadable>> d = Objects.requireNonNull(ute.explore(nodeByDateExtraction, c, new TreeExplorerState
                .Builder()
                .date(date)
                .build(), upnpGetChildren));

        List<Downloadable> ed =  d.get().values().stream().map(ExtractedDownloadable::value).toList();
        ed.forEach((down) -> {
            try {
                System.out.println(down.getDcTitle());
                switch (down){
                    case UPNPImage image -> downloadService.downloadToFile(image,"./" + date, PHOTO_RES.ORG).get();
                    case UPNPVideo video -> downloadService.downloadToFile(video,"./" + date, VIDEO_RES.ORG).get();
                    default -> throw new IllegalStateException("Unexpected downloadable in download service: " + down);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
