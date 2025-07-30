package org.alpha_sync_java.application.usecases;

import org.alpha_sync_java.application.interfaces.Downloader;
import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.application.strategy.*;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.infra.NioDownloader;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GetDates {
    public List<String> execute(UPNPContainer c) throws ExecutionException, InterruptedException {
        Downloader downloader = new NioDownloader();
        DateExtraction de = new DateExtraction();
        UPNPTreeExplorer ute = new UPNPTreeExplorer();
        CompletableFuture<Map<String, ExtractedListUPNP>> m = Objects.requireNonNull(ute.explore(de, c, new TreeExplorerState.Builder().build()));
        return m.get().keySet().stream().toList();
    }
}
