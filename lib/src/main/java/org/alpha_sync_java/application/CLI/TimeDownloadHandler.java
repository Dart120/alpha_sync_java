package org.alpha_sync_java.application.CLI;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.application.strategy.NodeByDateExtraction;
import org.alpha_sync_java.application.usecases.DownloadDate;
import org.alpha_sync_java.domain.CameraConnection;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class TimeDownloadHandler {
    DownloadDate downloadDate;
    CameraConnection cameraConnection;
    DownloadService downloadService;
    UpnpGetChildren upnpGetChildren;
    NodeByDateExtraction nodeByDateExtraction;
    UPNPTreeExplorer upnpTreeExplorer;
    String pattern;


    private TimeDownloadHandler(Builder builder) {
        downloadDate = builder.downloadDate;
        cameraConnection = builder.cameraConnection;
        downloadService = builder.downloadService;
        upnpGetChildren = builder.upnpGetChildren;
        nodeByDateExtraction = builder.nodeByDateExtraction;
        upnpTreeExplorer = builder.upnpTreeExplorer;
        pattern = builder.pattern;
    }

    public List<String> handle(List<String> dates, List<String> choices){
        Map<Boolean, List<String>> partition =  choices
                .stream()
                .collect(Collectors
                        .partitioningBy(date -> date
                                .matches(pattern)));
        download(dates,partition.get(true));
        return partition.get(false);
    }

    private void download(List<String> dates, List<String> choices){
        choices.stream()
                .map(String::trim)
                .flatMap(a -> dates.stream().filter(b -> b.startsWith(a)))
                .forEach(date -> {
                    try {
                        downloadDate.downloadDate(date, cameraConnection.getRoot(), downloadService, upnpGetChildren,nodeByDateExtraction,upnpTreeExplorer);
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static final class Builder {
        private DownloadDate downloadDate;
        private CameraConnection cameraConnection;
        private DownloadService downloadService;
        private UpnpGetChildren upnpGetChildren;
        private NodeByDateExtraction nodeByDateExtraction;
        private UPNPTreeExplorer upnpTreeExplorer;
        private String pattern;

        public Builder() {
        }

        public Builder downloadDate(DownloadDate val) {
            downloadDate = val;
            return this;
        }

        public Builder cameraConnection(CameraConnection val) {
            cameraConnection = val;
            return this;
        }

        public Builder downloadService(DownloadService val) {
            downloadService = val;
            return this;
        }

        public Builder upnpGetChildren(UpnpGetChildren val) {
            upnpGetChildren = val;
            return this;
        }

        public Builder nodeByDateExtraction(NodeByDateExtraction val) {
            nodeByDateExtraction = val;
            return this;
        }

        public Builder upnpTreeExplorer(UPNPTreeExplorer val) {
            upnpTreeExplorer = val;
            return this;
        }

        public Builder pattern(String val) {
            pattern = val;
            return this;
        }

        public TimeDownloadHandler build() {
            return new TimeDownloadHandler(this);
        }
    }
}
