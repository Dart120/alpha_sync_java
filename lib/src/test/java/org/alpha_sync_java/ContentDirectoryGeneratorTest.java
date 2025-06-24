package org.alpha_sync_java;

import org.alpha_sync_java.application.interfaces.RequestsXML;
import org.alpha_sync_java.application.interfaces.XMLExplorer;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.application.usecases.ContentDirectoryGenerator;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.communication.LocatorURL;
import org.alpha_sync_java.domain.communication.XMLTag;
import org.alpha_sync_java.domain.context.ServerContext;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


public class ContentDirectoryGeneratorTest {

        @Test
        public void someLibraryMethodReturnsTrue() throws Exception {
            RequestsXML requestsXMLMock = Mockito.mock(RequestsXML.class);
            XMLExplorer xmlExplorerMock = Mockito.mock(XMLExplorer.class);

            XMLTag contentDirectoryTag = new XMLTag("serviceType", "urn:schemas-upnp-org:service:ContentDirectory:1");

            ServerContext sc = new ServerContext();
            sc.location.complete(new LocatorURL("http://test:8080/test/"));
            XMLExplorerFactory xmlExplorerFactory = input -> xmlExplorerMock;
            when(xmlExplorerMock.getTagsSiblings(contentDirectoryTag)).thenReturn(
                    Stream.of(new XMLTag("SCPDURL", "/SCPDURL"),
                            new XMLTag("controlURL", "/controlURL"),
                            new XMLTag("eventSubURL", "/eventSubURL")
                    )
            );
            when(xmlExplorerMock.getTagsByName("serviceType")).thenReturn(
                    Stream.of(new XMLTag("serviceType", "urn:schemas-upnp-org:service:ConnectionManager:1"),
                            new XMLTag("serviceType", "urn:schemas-sony-com:service:XPushList:1"),
                            contentDirectoryTag
                    )
            );
            ContentDirectory cd =  new ContentDirectory.Builder()
                    .eventSubURL(LocatorURL.from(sc.location.get(), "/eventSubURL"))
                    .SCPDURL(LocatorURL.from(sc.location.get(), "/SCPDURL"))
                    .controlURL(LocatorURL.from(sc.location.get(), "/controlURL"))
                    .build();
            ContentDirectoryGenerator cut = new ContentDirectoryGenerator(requestsXMLMock,xmlExplorerFactory);
            ContentDirectory generatedContentDirectory = cut.execute(sc);
            assertEquals(cd.controlURL.getUrl(), generatedContentDirectory.controlURL.getUrl());
            assertEquals(cd.eventSubURL.getUrl(), generatedContentDirectory.eventSubURL.getUrl());
            assertEquals(cd.SCPDURL.getUrl(), generatedContentDirectory.SCPDURL.getUrl());

        }
    }


