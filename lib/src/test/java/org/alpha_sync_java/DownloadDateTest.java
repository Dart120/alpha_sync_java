package org.alpha_sync_java;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.application.strategy.NodeByDateExtraction;
import org.alpha_sync_java.application.usecases.DownloadDate;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.extracted.ExtractedDownloadable;
import org.alpha_sync_java.domain.upnp.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DownloadDateTest {

    @Mock
    private DownloadService downloadService;

    @Mock
    private UpnpGetChildren upnpGetChildren;

    @Mock
    private UPNPTreeExplorer ute;

    @Mock
    private NodeByDateExtraction nodeByDateExtraction;

    @Mock
    private UPNPContainer container;

    private DownloadDate downloadDate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        downloadDate = new DownloadDate();
    }

    @Test
    public void testDownloadDate_successfulImageAndVideoDownload() throws Exception {
        String testDate = "2025-08-10";

        // Prepare mocked extracted downloadables
        UPNPImage image = mock(UPNPImage.class);
        when(image.getDcTitle()).thenReturn("Image Title");

        UPNPVideo video = mock(UPNPVideo.class);
        when(video.getDcTitle()).thenReturn("Video Title");

        ExtractedDownloadable extractedImage = new ExtractedDownloadable(image);

        ExtractedDownloadable extractedVideo = new ExtractedDownloadable(video);

        Map<String, ExtractedDownloadable> extractedMap = new HashMap<>();
        extractedMap.put("img1", extractedImage);
        extractedMap.put("vid1", extractedVideo);

        CompletableFuture<Map<String, ExtractedDownloadable>> future = CompletableFuture.completedFuture(extractedMap);

        // Mock UPNPTreeExplorer.explore(...) to return the future
        when(ute.explore(eq(nodeByDateExtraction), eq(container), any(TreeExplorerState.class), eq(upnpGetChildren)))
                .thenReturn(future);

        // Mock downloadService.downloadToFile(...) to return completed futures
        when(downloadService.downloadToFile(eq(image), eq("./" + testDate), eq(PHOTO_RES.ORG)))
                .thenReturn(CompletableFuture.completedFuture(null));
        when(downloadService.downloadToFile(eq(video), eq("./" + testDate), eq(VIDEO_RES.ORG)))
                .thenReturn(CompletableFuture.completedFuture(null));

        // Call method under test
        downloadDate.downloadDate(testDate, container, downloadService, upnpGetChildren, nodeByDateExtraction, ute);

        // Verify downloads called with correct params
        verify(downloadService).downloadToFile(eq(image), eq("./" + testDate), eq(PHOTO_RES.ORG));
        verify(downloadService).downloadToFile(eq(video), eq("./" + testDate), eq(VIDEO_RES.ORG));
    }

    @Test(expected = RuntimeException.class)
    public void testDownloadDate_throwsWhenUnexpectedDownloadable() throws Exception {
        String testDate = "2025-08-10";

        Downloadable unknown = mock(Downloadable.class);
        when(unknown.getDcTitle()).thenReturn("Unknown Title");

        ExtractedDownloadable extractedUnknown = mock(ExtractedDownloadable.class);
        when(extractedUnknown.value()).thenReturn(unknown);

        Map<String, ExtractedDownloadable> extractedMap = new HashMap<>();
        extractedMap.put("unknown1", extractedUnknown);

        CompletableFuture<Map<String, ExtractedDownloadable>> future = CompletableFuture.completedFuture(extractedMap);

        when(ute.explore(eq(nodeByDateExtraction), eq(container), any(TreeExplorerState.class), eq(upnpGetChildren)))
                .thenReturn(future);

        // This should throw because the downloadable is unexpected type
        downloadDate.downloadDate(testDate, container, downloadService, upnpGetChildren, nodeByDateExtraction, ute);
    }
}
