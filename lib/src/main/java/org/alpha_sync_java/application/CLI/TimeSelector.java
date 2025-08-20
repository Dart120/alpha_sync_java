package org.alpha_sync_java.application.CLI;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.interfaces.PresentOption;
import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.application.usecases.DownloadDate;
import org.alpha_sync_java.domain.CameraConnection;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
public abstract class TimeSelector {
    DownloadDate downloadDate;
    CameraConnection cameraConnection;
    DownloadService downloadService;
    UpnpGetChildren upnpGetChildren;
    PresentOption presentOption;
    boolean first = false;

    public TimeSelector(UpnpGetChildren upnpGetChildren, DownloadService downloadService, CameraConnection cameraConnection, DownloadDate downloadDate, String timeName, PresentOption presentOption, boolean first) {
        this.upnpGetChildren = upnpGetChildren;
        this.downloadService = downloadService;
        this.cameraConnection = cameraConnection;
        this.downloadDate = downloadDate;
        this.timeName = timeName;
        this.presentOption = presentOption;
        this.first = first;

    }

    String timeName;
    abstract Optional<String> handled(String from, List<String> dates);
    Optional<String> display(Set<String> choices, List<String> dates, String from){
        boolean day = presentOption
                .withNumberedOptions(List.of("Day","Mult"),"\nDo you want to select a day from a" + timeName + " or select multiple " + timeName + "s? [Day,Mult]")
                .equalsIgnoreCase("day");;
        if (day){
            String year = presentOption
                    .withNumberedOptions(choices.stream().toList(),"\nSelect a " + timeName);
            return Optional.of(year);
        } else {
            System.out.println(choices.stream().toList());
            String yearChoices = presentOption.simplePrompt("\nEnter a comma seperated list of " + timeName + "s");
            Arrays.stream(yearChoices.split(","))
                    .map(String::trim)
                    .map(a -> (first ? "" : from + "-") + a)
                    .flatMap(a -> dates.stream().filter(b -> b.startsWith(a)))
                    .forEach(date -> {
//                        try {
////                            downloadDate.downloadDate(date, cameraConnection.getRoot(), downloadService, upnpGetChildren);
//                        } catch (ExecutionException | InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
                    });
            return Optional.empty();
        }
    }
}
