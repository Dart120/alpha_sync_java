package org.alpha_sync_java;

import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.domain.extracted.ExtractedByteArray;
import org.alpha_sync_java.application.strategy.ThumbnailExtraction;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.PHOTO_RES;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPImage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ThumbnailExtractionTest {
    ThumbnailExtraction thumbnailExtraction;
    DownloadService downloadService;
    ReadableByteChannel readableByteChannel;
    byte[] byteArr;
    Map<String, ExtractedByteArray> map;
    @Before
    public void setUp() {
        downloadService = Mockito.mock(DownloadService.class);
        readableByteChannel = Mockito.mock(ReadableByteChannel.class);
        thumbnailExtraction = new ThumbnailExtraction(downloadService);
        byteArr = "".getBytes();
        map = new HashMap<>();
    }

    @Test
    public void testNonDateContainer() throws Exception {
        UPNPContainer container = new UPNPContainer.Builder()
                .id("PhotoRoot")
                .dcTitle("PhotoRoot")
                .parentID("0")
                .build();

//        when(downloadService.downloadToBytes(eq(container),eq(PHOTO_RES.TN))).thenReturn(CompletableFuture.completedFuture(byteArr));

        TreeExplorerState state = new TreeExplorerState
                .Builder()
                .date("2021-01-01")
                .id("bad")
                .build();
        Boolean result = thumbnailExtraction.extract(container, map, state);
        assert !result;
        assertEquals(0, map.size());
    }

    @Test
    public void testDateRightContainer() throws Exception {
        UPNPContainer container = new UPNPContainer.Builder()
                .id("2021-01-01")
                .dcTitle("2021-01-01")
                .parentID("0")
                .build();

//        when(downloadService.downloadToBytes(eq(container),eq(PHOTO_RES.TN))).thenReturn(CompletableFuture.completedFuture(byteArr));

        TreeExplorerState state = new TreeExplorerState
                .Builder()
                .date("2021-01-01")
                .id("bad")
                .build();
        Boolean result = thumbnailExtraction.extract(container, map, state);
        assert !result;
        assertEquals(0, map.size());


    }
    @Test
    public void testDateWrongContainer() throws Exception {
        UPNPContainer container = new UPNPContainer.Builder()
                .id("2021-01-02")
                .dcTitle("2021-01-02")
                .parentID("0")
                .build();

//        when(downloadService.downloadToBytes(eq(container),eq(PHOTO_RES.TN))).thenReturn(CompletableFuture.completedFuture(byteArr));
        TreeExplorerState state = new TreeExplorerState
                .Builder()
                .date("2021-01-01")
                .id("bad")
                .build();
        Boolean result = thumbnailExtraction.extract(container, map, state);
        assert result;
        assertEquals(0, map.size());


    }

    @Test
    public void testImage() throws Exception {
        Map<PHOTO_RES, URL> res = new HashMap<>();
        res.put(PHOTO_RES.TN, new URI("http://test:8080/test/tn").toURL());
        UPNPImage image = new UPNPImage.Builder().id("test").res(res).build();
        when(downloadService.downloadToBytes(eq(image),eq(PHOTO_RES.TN))).thenReturn(CompletableFuture.completedFuture(byteArr));
        TreeExplorerState state = new TreeExplorerState
                .Builder()
                .date("2021-01-01")
                .id("bad")
                .build();
        Boolean result = thumbnailExtraction.extract(image, map, state);
        assert !result;
        assertEquals(1, map.size());
        assertEquals(byteArr, map.get("test").value().get().get());
    }
}
