package org.alpha_sync_java;

import org.alpha_sync_java.application.interfaces.Downloader;
import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.domain.upnp.PHOTO_RES;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPImage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DownloadServiceTest {
    DownloadService downloadService;
    Downloader downloader;
    @Before
    public void setUp() {
        downloader = Mockito.mock(Downloader.class);
        downloadService = new DownloadService(downloader);
    }

    @Test
    public void testDownloadToBytes() throws Exception {
        Map<PHOTO_RES, URL> res = new HashMap<>();
        URL test = new URI("http://test:8080/test/tn").toURL();
        res.put(PHOTO_RES.TN, test);
        UPNPImage image = new UPNPImage.Builder().id("test").dcTitle("test").res(res).build();
        downloadService.downloadToBytes(image,PHOTO_RES.TN);
        verify(downloader,times(1)).downloadToBytes(test);
    }

    @Test
    public void testDownloadToFile() throws Exception {
        Map<PHOTO_RES, URL> res = new HashMap<>();
        URL test = new URI("http://test:8080/test/tn").toURL();
        res.put(PHOTO_RES.TN, test);
        UPNPImage image = new UPNPImage.Builder().id("test").dcTitle("test").res(res).build();
        downloadService.downloadToFile(image,PHOTO_RES.TN);
        verify(downloader,times(1)).downloadToFile(test,"test");
    }




}
