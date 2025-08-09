package org.alpha_sync_java.application.CLI;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.interfaces.PresentOption;
import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.application.usecases.DownloadDate;
import org.alpha_sync_java.domain.CameraConnection;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MonthSelector extends TimeSelector {
    static final String timeName = "month";

    private MonthSelector(Builder builder) {
        super(builder.upnpGetChildren, builder.downloadService, builder.cameraConnection, builder.downloadDate,timeName,builder.presentOption,false);
    }

    public Optional<String> handled(String from,  List<String> dates){
        Set<String> months = dates.stream()
                .sorted()
                .filter((date) -> date.startsWith(from))
                .map(date -> date
                        .split("-")[1])
                .collect(Collectors.toSet());
        return display(months,dates,from).map((month) -> from + "-" + month);
    }

    public static final class Builder {
        private UpnpGetChildren upnpGetChildren;
        private DownloadService downloadService;
        private CameraConnection cameraConnection;
        private DownloadDate downloadDate;
        private boolean first;
        private PresentOption presentOption;

        public Builder() {
        }

        public Builder upnpGetChildren(UpnpGetChildren val) {
            upnpGetChildren = val;
            return this;
        }

        public Builder downloadService(DownloadService val) {
            downloadService = val;
            return this;
        }

        public Builder cameraConnection(CameraConnection val) {
            cameraConnection = val;
            return this;
        }

        public Builder downloadDate(DownloadDate val) {
            downloadDate = val;
            return this;
        }

        public MonthSelector build() {
            return new MonthSelector(this);
        }

        public Builder first(boolean val) {
            first = val;
            return this;
        }

        public Builder presentOption(PresentOption val) {
            presentOption = val;
            return this;
        }
    }
}
