package org.alpha_sync_java;

import org.alpha_sync_java.application.strategy.NodeByDateExtraction;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.extracted.ExtractedDownloadable;
import org.alpha_sync_java.domain.upnp.Downloadable;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPImage;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NodeByDateExtractionTest {

    private NodeByDateExtraction strategy;

    @Mock
    private UPNPContainer container;

    @Mock
    private UPNPImage image;

    @Mock
    private TreeExplorerState state;

    private Map<String, ExtractedDownloadable> map;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        strategy = new NodeByDateExtraction();
        map = new HashMap<>();
    }

    @Test
    public void testContainerWithMatchingDateButDifferentFromState() {
        when(state.getDate()).thenReturn("2025-08-09");
        when(container.getDcTitle()).thenReturn("2025-08-10");

        Boolean result = strategy.extract(container, map, state);

        assertTrue(result);
    }

    @Test
    public void testContainerWithMatchingDateSameAsState() {
        when(state.getDate()).thenReturn("2025-08-09");
        when(container.getDcTitle()).thenReturn("2025-08-09");

        Boolean result = strategy.extract(container, map, state);

        assertFalse(result);
    }

    @Test
    public void testContainerWithoutDateInTitle() {
        when(state.getDate()).thenReturn("2025-08-09");
        when(container.getDcTitle()).thenReturn("No date here");

        Boolean result = strategy.extract(container, map, state);

        assertFalse(result);
    }

    @Test
    public void testContainerWhenStateDateIsNull() {
        when(state.getDate()).thenReturn(null);
        when(container.getDcTitle()).thenReturn("2025-08-09");

        Boolean result = strategy.extract(container, map, state);

        assertFalse(result);
    }

    @Test
    public void testDownloadableAddsToMapAndCallsFoundItem() {
        when(image.getId()).thenReturn("file123");

        Boolean result = strategy.extract(image, map, state);

        assertTrue(result);
        assertTrue(map.containsKey("file123"));
        verify(state, times(1)).foundItem();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnexpectedUPNPObjectThrowsException() {
        UPNPObject unknown = mock(UPNPObject.class);
        strategy.extract(unknown, map, state);
    }
}
