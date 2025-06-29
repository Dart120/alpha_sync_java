package org.alpha_sync_java.application.service;

import org.alpha_sync_java.application.interfaces.RequestsXML;
import org.alpha_sync_java.application.interfaces.SOAPConnector;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.domain.context.ServerContext;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseResponseObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ContentDirectoryQueryService {
    SOAPConnector soapConnector;
    XMLExplorerFactory xmlExplorerFactory;

    public ContentDirectoryQueryService(SOAPConnector soapConnector, XMLExplorerFactory xmlExplorerFactory) {
        this.soapConnector = soapConnector;
        this.xmlExplorerFactory = xmlExplorerFactory;
    }

    //    Should send a BrowseRequest
//    Populate resp
//    Make a seperate Mapper from XML to obj to xml
    public List<UPNPObject> query(BrowseRequestObject browseRequestObject, ContentDirectory contentDirectory) throws ExecutionException, InterruptedException, ParserConfigurationException, IOException, SAXException {
        CompletableFuture<String> xml = this.soapConnector.requestXML(contentDirectory.controlURL, browseRequestObject.toXML(),
                "\"urn:schemas-upnp-org:service:ContentDirectory:1#Browse\"");
        BrowseResponseObject browseResponseObject =  BrowseResponseObject.fromXML(xmlExplorerFactory, xml.get());
        return browseResponseObject.getResultToUPNPObjects(xmlExplorerFactory,this, contentDirectory);
    }
}
