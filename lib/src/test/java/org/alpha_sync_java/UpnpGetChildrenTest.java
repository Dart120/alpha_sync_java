package org.alpha_sync_java;

import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.service.ContentDirectoryQueryService;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpnpGetChildrenTest {

    @Mock
    private ContentDirectoryQueryService queryService;

    @Mock
    private ContentDirectory contentDirectory;

    @Mock
    private UPNPContainer upnpContainer;

    private UpnpGetChildren upnpGetChildren;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        upnpGetChildren = new UpnpGetChildren.Builder()
                .contentDirectoryQueryService(queryService)
                .contentDirectory(contentDirectory)
                .build();
    }

    @Test
    public void testConstructBrowseRequestObject() {
        String id = "test-id";

        BrowseRequestObject bro = upnpGetChildren.constructBrowseRequestObject(id);

        assertEquals("test-id", bro.getObjectID());
        assertEquals((Integer) 100, bro.getRequestedCount());
        assertEquals("*", bro.getFilter());
        assertEquals((Integer) 0, bro.getStartingIdx());
    }

    @Test
    public void testGetChildren_success() throws Exception {
        List<UPNPObject> mockResult = Arrays.asList(mock(UPNPObject.class), mock(UPNPObject.class));

        when(upnpContainer.getId()).thenReturn("container-id");
        when(queryService.query(any(BrowseRequestObject.class), eq(contentDirectory))).thenReturn(mockResult);

        Supplier<CompletableFuture<List<UPNPObject>>> supplier = upnpGetChildren.getChildren(upnpContainer);
        CompletableFuture<List<UPNPObject>> future = supplier.get();

        List<UPNPObject> result = future.get();

        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify that query was called once with a BrowseRequestObject whose id matches
        verify(queryService, times(1)).query(argThat(bro -> "container-id".equals(bro.getObjectID())), eq(contentDirectory));
    }

    @Test(expected = RuntimeException.class)
    public void testGetChildren_throwsRuntimeExceptionOnCheckedException() throws Throwable {
        when(upnpContainer.getId()).thenReturn("container-id");

        when(queryService.query(any(BrowseRequestObject.class), eq(contentDirectory)))
                .thenThrow(new ExecutionException(new Exception("test")));

        Supplier<CompletableFuture<List<UPNPObject>>> supplier = upnpGetChildren.getChildren(upnpContainer);
        try {
            supplier.get().get(); // This should cause RuntimeException wrapping ExecutionException
        } catch (Exception e) {
            // unwrap CompletionException to check cause type
            throw e.getCause();
        }
    }
}
