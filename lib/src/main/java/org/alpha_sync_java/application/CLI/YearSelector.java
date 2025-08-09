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

public class YearSelector extends TimeSelector {
    static final String timeName = "year";

    private YearSelector(Builder builder) {
        super(builder.upnpGetChildren, builder.downloadService, builder.cameraConnection, builder.downloadDate,timeName,builder.presentOption,true);
    }

    public Optional<String> handled(String from,  List<String> dates){
        Set<String> years = dates.stream()
                .sorted()
                .map(date -> date
                        .split("-")[0])
                .collect(Collectors.toSet());
        return display(years, dates,from);
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

        public Builder first(Boolean val) {
            first = val;
            return this;
        }

        public YearSelector build() {
            return new YearSelector(this);
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
