package org.alpha_sync_java;

import org.alpha_sync_java.application.CLI.DateSelector;
import org.alpha_sync_java.application.CLI.TimeDownloadHandler;
import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.application.strategy.DateExtraction;
import org.alpha_sync_java.application.usecases.GetDates;
import org.alpha_sync_java.domain.CameraConnection;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DateSelectorTest {

    private GetDates getDates;
    private DateExtraction dateExtraction;
    private UPNPTreeExplorer upnpTreeExplorer;
    private UpnpGetChildren upnpGetChildren;
    private TimeDownloadHandler yearHandler;
    private TimeDownloadHandler monthHandler;
    private TimeDownloadHandler dayHandler;
    private CameraConnection cameraConnection;
    private UPNPContainer root;

    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        getDates = mock(GetDates.class);
        dateExtraction = mock(DateExtraction.class);
        upnpTreeExplorer = mock(UPNPTreeExplorer.class);
        upnpGetChildren = mock(UpnpGetChildren.class);
        yearHandler = mock(TimeDownloadHandler.class);
        monthHandler = mock(TimeDownloadHandler.class);
        dayHandler = mock(TimeDownloadHandler.class);
        cameraConnection = mock(CameraConnection.class);
        root = mock(UPNPContainer.class);

        when(cameraConnection.getRoot()).thenReturn(root);

        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testShow_AllDatesHandled_NoRejects() throws ExecutionException, InterruptedException {
        // Simulate dates returned from GetDates
        List<String> availableDates = Arrays.asList("2023", "2024-01", "2025-02-08");
        when(getDates.execute(eq(root), any(), any(), any()))
                .thenReturn(availableDates);

        // Simulate the TimeDownloadHandlers returning no rejects
        when(yearHandler.handle(eq(availableDates), anyList())).thenAnswer(inv -> inv.getArgument(1));
        when(monthHandler.handle(eq(availableDates), anyList())).thenAnswer(inv -> inv.getArgument(1));
        when(dayHandler.handle(eq(availableDates), anyList())).thenAnswer(inv -> inv.getArgument(1));

        // Simulate user typing input
        String userInput = "2023,2024-01,2025-02-08\n";
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        DateSelector selector = new DateSelector.Builder()
                .getDates(getDates)
                .dateExtraction(dateExtraction)
                .upnpTreeExplorer(upnpTreeExplorer)
                .upnpGetChildren(upnpGetChildren)
                .yearHandler(yearHandler)
                .monthHandler(monthHandler)
                .dayHandler(dayHandler)
                .build();

        selector.show(cameraConnection);

        String output = outContent.toString();
        assertTrue(output.contains("You entered 2023,2024-01,2025-02-08"));
        assertTrue(output.contains("Thanks for using AlphaSync!"));

        // Verify calls
        verify(getDates).execute(root, dateExtraction, upnpTreeExplorer, upnpGetChildren);
        InOrder inOrder = inOrder(yearHandler, monthHandler, dayHandler);
        inOrder.verify(yearHandler).handle(anyList(), anyList());
        inOrder.verify(monthHandler).handle(anyList(), anyList());
        inOrder.verify(dayHandler).handle(anyList(), anyList());
    }

    @Test
    public void testShow_RejectsReturned() throws ExecutionException, InterruptedException {
        List<String> availableDates = Collections.singletonList("2023");
        when(getDates.execute(eq(root), any(), any(), any()))
                .thenReturn(availableDates);

        // First handler passes everything through, second returns rejects, third unused
        when(yearHandler.handle(eq(availableDates), anyList()))
                .thenAnswer(inv -> inv.getArgument(1));
        when(monthHandler.handle(eq(availableDates), anyList()))
                .thenReturn(Collections.singletonList("bad-date"));
        when(dayHandler.handle(eq(availableDates), anyList()))
                .thenAnswer(inv -> inv.getArgument(1));

        System.setIn(new ByteArrayInputStream("bad-date\n".getBytes()));

        DateSelector selector = new DateSelector.Builder()
                .getDates(getDates)
                .dateExtraction(dateExtraction)
                .upnpTreeExplorer(upnpTreeExplorer)
                .upnpGetChildren(upnpGetChildren)
                .yearHandler(yearHandler)
                .monthHandler(monthHandler)
                .dayHandler(dayHandler)
                .build();

        selector.show(cameraConnection);

        String output = outContent.toString();
        assertTrue(output.contains("We didn't understand these dates:"));
        assertTrue(output.contains("bad-date"));
    }

    @Test(expected = ExecutionException.class)
    public void testShow_ExecutionExceptionThrown() throws ExecutionException, InterruptedException {
        // Simulate GetDates throwing an ExecutionException
        when(getDates.execute(eq(root), any(), any(), any()))
                .thenThrow(new ExecutionException("Failed to fetch dates", null));

        System.setIn(new ByteArrayInputStream("2023\n".getBytes()));

        DateSelector selector = new DateSelector.Builder()
                .getDates(getDates)
                .dateExtraction(dateExtraction)
                .upnpTreeExplorer(upnpTreeExplorer)
                .upnpGetChildren(upnpGetChildren)
                .yearHandler(yearHandler)
                .monthHandler(monthHandler)
                .dayHandler(dayHandler)
                .build();

        // This should propagate the ExecutionException
        selector.show(cameraConnection);
    }

}
