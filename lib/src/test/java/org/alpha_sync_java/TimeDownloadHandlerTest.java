package org.alpha_sync_java;

import org.alpha_sync_java.application.CLI.TimeDownloadHandler;
import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.application.strategy.NodeByDateExtraction;
import org.alpha_sync_java.application.usecases.DownloadDate;
import org.alpha_sync_java.domain.CameraConnection;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TimeDownloadHandlerTest {
    @Mock
    private DownloadDate downloadDate;
    @Mock
    private CameraConnection cameraConnection;
    @Mock
    private DownloadService downloadService;
    @Mock
    private UpnpGetChildren upnpGetChildren;
    @Mock
    private UPNPContainer root;
    @Mock
    private UPNPTreeExplorer ute;
    @Mock
    private NodeByDateExtraction nodeByDateExtraction;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(cameraConnection.getRoot()).thenReturn(root);
    }

    @Test
    public void testHandle_MatchingPatternTriggersDownload() throws Exception {
        List<String> dates = Arrays.asList("2023", "2023-05", "2024");
        List<String> choices = Arrays.asList("2023", "bad-choice");

        TimeDownloadHandler handler = new TimeDownloadHandler.Builder()
                .downloadDate(downloadDate)
                .cameraConnection(cameraConnection)
                .downloadService(downloadService)
                .upnpGetChildren(upnpGetChildren)
                .upnpTreeExplorer(ute)
                .nodeByDateExtraction(nodeByDateExtraction)
                .pattern("\\d{4}.*") // matches YYYY or YYYY-MM etc.
                .build();

        List<String> rejects = handler.handle(dates, choices);

        // Verify downloadDate called for all matching available dates starting with "2023"
        verify(downloadDate).downloadDate(eq("2023"), eq(root), eq(downloadService), eq(upnpGetChildren), eq(nodeByDateExtraction), eq(ute));
        verify(downloadDate).downloadDate(eq("2023-05"), eq(root), eq(downloadService), eq(upnpGetChildren), eq(nodeByDateExtraction), eq(ute));

        // Ensure the unmatched choice is returned in rejects
        assertEquals(Collections.singletonList("bad-choice"), rejects);
    }

    @Test
    public void testHandle_NoMatches_NoDownload() throws Exception {
        List<String> dates = Arrays.asList("2023", "2024");
        List<String> choices = Collections.singletonList("invalid");

        TimeDownloadHandler handler = new TimeDownloadHandler.Builder()
                .downloadDate(downloadDate)
                .cameraConnection(cameraConnection)
                .downloadService(downloadService)
                .upnpGetChildren(upnpGetChildren)
                .pattern("\\d{4}-\\d{2}") // only matches YYYY-MM
                .build();

        List<String> rejects = handler.handle(dates, choices);

        // No downloads should be triggered
        verify(downloadDate, never()).downloadDate(anyString(), any(), any(), any(),any(), any());

        // All choices should be in rejects
        assertEquals(Collections.singletonList("invalid"), rejects);
    }

    @Test(expected = RuntimeException.class)
    public void testHandle_DownloadThrowsExecutionException() throws Exception {
        List<String> dates = Collections.singletonList("2023");
        List<String> choices = Collections.singletonList("2023");

        doThrow(new ExecutionException("fail", null))
                .when(downloadDate)
                .downloadDate(anyString(), any(), any(), any(), any(), any());

        TimeDownloadHandler handler = new TimeDownloadHandler.Builder()
                .downloadDate(downloadDate)
                .cameraConnection(cameraConnection)
                .downloadService(downloadService)
                .upnpGetChildren(upnpGetChildren)
                .pattern("\\d{4}") // matches YYYY
                .build();

        handler.handle(dates, choices);
    }
}
