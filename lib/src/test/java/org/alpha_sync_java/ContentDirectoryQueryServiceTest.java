package org.alpha_sync_java;

import org.alpha_sync_java.application.interfaces.SOAPConnector;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.application.service.ContentDirectoryQueryService;
import org.alpha_sync_java.application.service.UPNPObjectCachingService;
import org.alpha_sync_java.domain.communication.LocatorURL;
import org.alpha_sync_java.domain.context.ServerContext;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.alpha_sync_java.infra.XMLParser;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ContentDirectoryQueryServiceTest {
//    Populate Browser resp from browser req
    @Test
    public void someLibraryMethodReturnsTrue() throws Exception {
        BrowseRequestObject bro = new BrowseRequestObject
                .Builder()
                .objectID("0")
                .requestedCount(100)
                .filter("*")
                .startingIdx(0)
                .build();
        ServerContext sc = new ServerContext();
        sc.location.complete(new LocatorURL("http://test:8080/test/"));
        ContentDirectory cd =  new ContentDirectory.Builder()
                .eventSubURL(LocatorURL.from(sc.location.get(), "/eventSubURL"))
                .SCPDURL(LocatorURL.from(sc.location.get(), "/SCPDURL"))
                .controlURL(LocatorURL.from(sc.location.get(), "/controlURL"))
                .build();
        XMLExplorerFactory xmlExplorerFactory = (xml) -> {
            try {
                return new XMLParser(xml);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        };


        String xml = LoadXMLForTest.loadXML("BrowseResponseObject.xml");
        SOAPConnector soapConnectorMock = Mockito.mock(SOAPConnector.class);
        UPNPObjectCachingService upnpObjectCachingService = new UPNPObjectCachingService(10);
        ContentDirectoryQueryService cut = new ContentDirectoryQueryService(soapConnectorMock,xmlExplorerFactory);
        when(soapConnectorMock.requestXML(any(),anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(xml));
        List<UPNPObject> testResp = cut.query(bro,cd);

        UPNPObject actualUPNPObject = new UPNPObject
                .Builder()
                .upnpClass("object.container")
                .id("PhotoRoot")
                .dcTitle("PhotoRoot")
                .parentID("0")
                .build();

        Assert.assertNotNull(testResp);
        assertEquals(actualUPNPObject, testResp.get(0));

    }

}
