package org.alpha_sync_java;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.domain.extracted.ExtractedByteArray;
import org.alpha_sync_java.application.strategy.ExtractionStrategy;
import org.alpha_sync_java.domain.TreeExplorerState;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPImage;
import org.alpha_sync_java.domain.upnp.UPNPObject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


public class UPNPTreeExplorerTest {
    UPNPTreeExplorer explorer;
    @Mock
    ExtractionStrategy<ExtractedByteArray> strategy;
    @Mock
    UpnpGetChildren upnpGetChildren;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        explorer = new UPNPTreeExplorer();

    }

    @Test
    public void testExploreWithOneContainer() throws Exception {
        UPNPContainer container = mock(UPNPContainer.class);
        UPNPObject child = mock(UPNPContainer.class);  // could also be a container if you want nested test

        // Behavior
        when(strategy.extract(eq(container), any(), any())).thenReturn(false);
        when(upnpGetChildren.getChildren(container)).thenReturn(() -> CompletableFuture.completedFuture(List.of(child)));
        when(strategy.extract(eq(child), any(), any())).thenReturn(true);  // no more recursion

        // Call
        Map<String, ExtractedByteArray> result = explorer
                .explore(strategy, container, new TreeExplorerState.Builder().build(), upnpGetChildren)
                .get();  // wait for future to complete

        // Assert
        assertNotNull(result);  // check it's not null
        verify(strategy).extract(eq(container), any(), any());
        verify(strategy).extract(eq(child), any(), any());
    }

    @Test
    public void testExploreWithTenContainersLeafNotRecursed() throws Exception {

        UPNPContainer container = mock(UPNPContainer.class);
        UPNPContainer child1 = mock(UPNPContainer.class);  // could also be a container if you want nested test
        UPNPImage child2 = mock(UPNPImage.class);  // could also be a container if you want nested test
        List<UPNPObject> children1 = new ArrayList<>(Collections.nCopies(10, child1));
        children1.add(child2);

        // Behavior
        when(strategy.extract(eq(container), any(), any())).thenReturn(false);
        when(upnpGetChildren.getChildren(container)).thenReturn(() -> CompletableFuture.completedFuture(children1));
        when(upnpGetChildren.getChildren(child1)).thenReturn(() -> CompletableFuture.completedFuture(List.of(child2)));
        // no more recursion

        // Call
        Map<String, ExtractedByteArray> result = explorer
                .explore(strategy, container, new TreeExplorerState.Builder().build(), upnpGetChildren)
                .get();  // wait for future to complete

        // Assert
        assertNotNull(result);  // check it's not null
        verify(strategy).extract(eq(container), any(), any());
        verify(strategy,times(10)).extract(eq(child1), any(), any());
        verify(strategy,times(11)).extract(eq(child2), any(), any());

    }
}
