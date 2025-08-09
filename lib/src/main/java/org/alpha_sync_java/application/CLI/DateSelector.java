package org.alpha_sync_java.application.CLI;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.application.strategy.DateExtraction;
import org.alpha_sync_java.application.usecases.GetDates;
import org.alpha_sync_java.domain.CameraConnection;

public class DateSelector {

    GetDates getDates;

    DateExtraction dateExtraction;
    UPNPTreeExplorer upnpTreeExplorer;
    UpnpGetChildren upnpGetChildren;
    TimeDownloadHandler yearHandler;
    TimeDownloadHandler monthHandler;
    TimeDownloadHandler dayHandler;

    private DateSelector(Builder builder) {
        getDates = builder.getDates;
        dateExtraction = builder.dateExtraction;
        upnpTreeExplorer = builder.upnpTreeExplorer;
        upnpGetChildren = builder.upnpGetChildren;
        yearHandler = builder.yearHandler;
        monthHandler = builder.monthHandler;
        dayHandler = builder.dayHandler;
    }


    public void show(CameraConnection cameraConnection) throws ExecutionException, InterruptedException {
        List<String> dates = getDates.execute(cameraConnection.getRoot(),dateExtraction,upnpTreeExplorer,upnpGetChildren);
        List<TimeDownloadHandler> selectors =  List.<TimeDownloadHandler>of(yearHandler,monthHandler,dayHandler);
        Scanner in = new Scanner(System.in);
        String input;
        System.out.println("Enter the list (eg \"2023,2024-01,2025-02-08\" will download all media from 2023, January 2024 and the eighth of February 2025");
        input = in.nextLine(); // waits for user to press enter.
        System.out.println("You entered "+ input);
        List<String> choices = List.of(input.split(","));
        List<String> rejects = selectors
                .stream()
                .reduce(choices, (a,b) -> b.handle(dates,a), (s1,s2) -> {throw new UnsupportedOperationException();});
        System.out.println("We didn't understand these dates: ");
        rejects.forEach(System.out::println);

        System.out.println("Thanks for using AlphaSync!");
        in.close();
    }

    public static final class Builder {

        private GetDates getDates;

        private DateExtraction dateExtraction;
        private UPNPTreeExplorer upnpTreeExplorer;
        private UpnpGetChildren upnpGetChildren;
        private TimeDownloadHandler yearHandler;
        private TimeDownloadHandler monthHandler;
        private TimeDownloadHandler dayHandler;
        private DaySelector daySelector;
        private MonthSelector monthSelector;
        private YearSelector yearSelector;


        public Builder() {
        }


        public Builder getDates(GetDates val) {
            getDates = val;
            return this;
        }


        public Builder dateExtraction(DateExtraction val) {
            dateExtraction = val;
            return this;
        }

        public Builder upnpTreeExplorer(UPNPTreeExplorer val) {
            upnpTreeExplorer = val;
            return this;
        }

        public Builder upnpGetChildren(UpnpGetChildren val) {
            upnpGetChildren = val;
            return this;
        }

        public Builder yearHandler(TimeDownloadHandler val) {
            yearHandler = val;
            return this;
        }

        public Builder monthHandler(TimeDownloadHandler val) {
            monthHandler = val;
            return this;
        }

        public Builder dayHandler(TimeDownloadHandler val) {
            dayHandler = val;
            return this;
        }

        public DateSelector build() {
            return new DateSelector(this);
        }
    }
}
