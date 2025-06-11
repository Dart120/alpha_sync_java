package org.alpha_sync_java;

import org.alpha_sync_java.application.interfaces.RequestsXML;
import org.alpha_sync_java.application.interfaces.XMLExplorer;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.application.usecases.ContentDirectoryGenerator;
import org.alpha_sync_java.domain.ContentDirectory;
import org.alpha_sync_java.domain.communication.LocatorURL;
import org.alpha_sync_java.domain.communication.XMLTag;
import org.alpha_sync_java.domain.context.ServerContext;
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
        @org.junit.Test
        public void someLibraryMethodReturnsTrue() throws Exception {
            RequestsXML requestsXMLMock = Mockito.mock(RequestsXML.class);
            XMLExplorer xmlExplorerMock = Mockito.mock(XMLExplorer.class);

            XMLTag contentDirectoryTag = new XMLTag("serviceType", "urn:schemas-upnp-org:service:ContentDirectory:1", Stream.empty());
            Mockito.mock(XMLExplorer.class);
            ServerContext sc = new ServerContext();
            sc.location.complete(new LocatorURL("http://test:8080/test/"));

            InputStream in = getClass().getClassLoader().getResourceAsStream("ExampleDDXML.xml");
            XMLExplorerFactory xmlExplorerFactory = input -> xmlExplorerMock;
            assertNotNull(in);
            String xml = new BufferedReader(new InputStreamReader(in))
                    .lines()
                    .collect(Collectors.joining("\n"));
            when(requestsXMLMock.getXML(sc.location.get())).thenReturn(in);
            when(xmlExplorerMock.getTagsSiblings(contentDirectoryTag)).thenReturn(
                    Stream.of(new XMLTag("SCPDURL", "/SCPDURL", Stream.empty()),
                            new XMLTag("controlURL", "/controlURL", Stream.empty()),
                            new XMLTag("eventSubURL", "/eventSubURL", Stream.empty())
                    )
            );
            when(xmlExplorerMock.getTagsByName("serviceType")).thenReturn(
                    Stream.of(new XMLTag("serviceType", "urn:schemas-upnp-org:service:ConnectionManager:1", Stream.empty()),
                            new XMLTag("serviceType", "urn:schemas-sony-com:service:XPushList:1", Stream.empty()),
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


