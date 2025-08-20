package org.alpha_sync_java;

import org.alpha_sync_java.domain.extracted.ExtractedDownloadable;
import org.alpha_sync_java.application.strategy.SpecificNodesExtraction;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPImage;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SpecificNodesExtractionTest {
    SpecificNodesExtraction specificNodeExtraction;
    Map<String, ExtractedDownloadable> map;
    UPNPImage image;
    UPNPContainer container;
    @Before
    public void setUp() {
        specificNodeExtraction = new SpecificNodesExtraction();
        map = new HashMap<>();
        image = new UPNPImage.Builder()
                .id("test")
                .dcTitle("PhotoRoot")
                .parentID("0")
                .build();
        container = new UPNPContainer.Builder()
                .id("test")
                .dcTitle("2021-01-01")
                .parentID("0")
                .build();
    }

    @Test
    public void testGetImage(){

        TreeExplorerState state = new TreeExplorerState
                .Builder()
                .date("2021-01-01")
                .ids(Set.of("test"))
                .build();

        Boolean result = specificNodeExtraction.extract(image, map, state);
        assert result;
        assertEquals(1, map.size());
    }

    @Test
    public void testWrongId() {
        TreeExplorerState state = new TreeExplorerState
                .Builder()
                .date("2021-01-01")
                .ids(Set.of("bad"))
                .build();
        Boolean result = specificNodeExtraction.extract(image, map, state);
        assert !result;
        assertEquals(0, map.size());
    }

    @Test
    public void testRightDate() {
        TreeExplorerState state = new TreeExplorerState
                .Builder()
                .date("2021-01-01")
                .ids(Set.of("bad"))
                .build();
        Boolean result = specificNodeExtraction.extract(container, map, state);
        assert !result;
        assertEquals(0, map.size());
    }
    @Test
    public void testWrongDate() {
        TreeExplorerState state = new TreeExplorerState
                .Builder()
                .date("2021-01-02")
                .ids(Set.of("bad"))
                .build();
        Boolean result = specificNodeExtraction.extract(container, map, state);
        assert result;
        assertEquals(0, map.size());
    }
}
