package org.alpha_sync_java;

import org.alpha_sync_java.application.interfaces.XMLExplorer;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.application.mapping.XMLUPNPMapper;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseResponseObject;
import org.alpha_sync_java.infra.XMLParser;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XMLUPNPMapperTest {
    XMLExplorerFactory xmlExplorerFactory;
    @Before
    public void setup(){
        xmlExplorerFactory = (in) -> {
            try {
                return new XMLParser(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        };
    }
//    TODO support props in xml as some info we need is in there
    @Test
    public void objToXMLTest() throws Exception {
        BrowseRequestObject bro = new BrowseRequestObject
                .Builder()
                .objectID("0")
                .requestedCount(100)
                .filter("*")
                .startingIdx(0)
                .build();

        String xml = LoadXMLForTest.loadXML("BrowseRequestObject.xml");

        LoadXMLForTest.loadXML("BrowseRequestObject.xml");

        assertEquals(xml, bro.toXML());
    }

    @Test
    public void responseToObjTest() throws Exception {
        BrowseResponseObject actualBRO = new BrowseResponseObject
                .Builder()
                .numberReturned(1)
                .totalMatches(1)
                .updateID("1769507432")
                .result("&lt;DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n" +
                "xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\"\n" +
                "xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"\n" +
                "xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"\n" +
                "xmlns:arib=\"urn:schemas-arib-or-jp:elements-1-0/\"\n" +
                "xmlns:av=\"urn:schemas-sony-com:av\"&gt;\n" +
                "&lt;container id=\"PhotoRoot\" restricted=\"1\" parentID=\"0\" childCount=\"1\"&gt;&lt;dc:title&gt;PhotoRoot&lt;/dc:title&gt;&lt;upnp:class&gt;object.container&lt;/upnp:class&gt;&lt;av:mediaClass&gt;P,V&lt;/av:mediaClass&gt;&lt;/container&gt;&lt;/DIDL-Lite&gt;")
                .build();

        BrowseResponseObject testBRO = BrowseResponseObject.fromXML(xmlExplorerFactory,LoadXMLForTest.loadXML("BrowseResponseObject.xml"));
        assertEquals(actualBRO, testBRO);
    }

    @Test
    public void xmlToUPNPObjectTest() throws Exception {
        BrowseResponseObject bro = new BrowseResponseObject
                .Builder()
                .numberReturned(1)
                .totalMatches(1)
                .updateID("1769507432")
                .result("&lt;DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n" +
                        "xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\"\n" +
                        "xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"\n" +
                        "xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\"\n" +
                        "xmlns:arib=\"urn:schemas-arib-or-jp:elements-1-0/\"\n" +
                        "xmlns:av=\"urn:schemas-sony-com:av\"&gt;\n" +
                        "&lt;container id=\"PhotoRoot\" restricted=\"1\" parentID=\"0\" childCount=\"1\"&gt;&lt;dc:title&gt;PhotoRoot&lt;/dc:title&gt;&lt;upnp:class&gt;object.container&lt;/upnp:class&gt;&lt;av:mediaClass&gt;P,V&lt;/av:mediaClass&gt;&lt;/container&gt;&lt;/DIDL-Lite&gt;")
                .build();
        UPNPObject actualUPNPObject = new UPNPObject
                .Builder()
                .upnpClass("object.container")
                .id("PhotoRoot")
                .dcTitle("PhotoRoot")
                .parentID("0")
                .build();
// TODO
//        Vistor pattern for consuming UPNPObject
//        Look at LSP in Builders in the UPNP inheritance

        List<UPNPObject> testUPNPObject =  bro.getResultToUPNPObjects(xmlExplorerFactory);


        assertEquals(actualUPNPObject, testUPNPObject.get(0));
    }
}
