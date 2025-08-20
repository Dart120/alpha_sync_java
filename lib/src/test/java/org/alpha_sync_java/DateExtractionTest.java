package org.alpha_sync_java;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.strategy.DateExtraction;
import org.alpha_sync_java.domain.extracted.ExtractedListUPNP;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPImage;
import org.junit.Test;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class DateExtractionTest {
    DateExtraction dateExtraction;
    UpnpGetChildren upnpGetChildren;
    @Before
    public void setUp() {
        upnpGetChildren = mock(UpnpGetChildren.class);
        dateExtraction  = new DateExtraction(upnpGetChildren);
    }

    @Test
    public void testExtractDate() {
        Map<String, ExtractedListUPNP> map = new HashMap<>();
        UPNPContainer upnpContainer = new UPNPContainer.Builder().dcTitle("2021-01-01").build();
        Boolean result = dateExtraction.extract(upnpContainer, map, null);
        assert result;
        assertEquals("2021-01-01", map.keySet().iterator().next());

    }

    @Test
    public void testExtractDateOnImage() {
        Map<String, ExtractedListUPNP> map = new HashMap<>();
        UPNPImage upnpImage = new UPNPImage.Builder().build();
        Boolean result = dateExtraction.extract(upnpImage, map, null);
        assert !result;


    }
}
