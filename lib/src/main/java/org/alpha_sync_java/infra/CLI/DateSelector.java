package org.alpha_sync_java.infra.CLI;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.alpha_sync_java.application.usecases.DownloadDate;
import org.alpha_sync_java.application.usecases.GetDates;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextIO;

public class DateSelector {
    private final TextIO textIO;
    DownloadDate downloadDate;
    GetDates getDates;

    public DateSelector() {
        this.downloadDate = new DownloadDate();
        this.getDates = new GetDates();
        this.textIO = TextIoFactory.getTextIO();
    }
    public void show(UPNPContainer container) throws ExecutionException, InterruptedException {
        String choice = textIO.newStringInputReader()
                .withNumberedPossibleValues(getDates.execute(container))
                .read("\nSelect an action:");
        downloadDate.downloadDate(choice,container);
    }
}
