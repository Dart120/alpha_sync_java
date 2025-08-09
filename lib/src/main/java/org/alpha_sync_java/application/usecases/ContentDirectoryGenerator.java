package org.alpha_sync_java.application.usecases;

import org.alpha_sync_java.application.interfaces.RequestsXML;
import org.alpha_sync_java.application.interfaces.XMLExplorer;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.communication.LocatorURL;
import org.alpha_sync_java.domain.context.ServerContext;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ContentDirectoryGenerator {
    RequestsXML requestsXML;
    XMLExplorerFactory xmlExplorerFactory;

    public ContentDirectoryGenerator(RequestsXML requestsXML, XMLExplorerFactory xmlExplorerFactory) {
        this.requestsXML = requestsXML;
        this.xmlExplorerFactory = xmlExplorerFactory;
    }

    public ContentDirectory execute(ServerContext serverContext) throws ExecutionException, InterruptedException, ParserConfigurationException, IOException, SAXException {
        LocatorURL baseURL = serverContext.location.get();
        String serviceDirectoryXML = requestsXML.getXML(baseURL);
        XMLExplorer xmlExplorer = xmlExplorerFactory.create(serviceDirectoryXML);
        return xmlExplorer.getTagsByName("serviceType")
                .filter(xmlTag -> xmlTag.getTextDecodedContent().equals("urn:schemas-upnp-org:service:ContentDirectory:1"))
                .findFirst()
                .map(xmlExplorer::getTagsSiblings)
                .map(xmlTagStream -> ContentDirectory.fromXMLStream(xmlTagStream, baseURL))
                .orElse(new ContentDirectory.Builder().build());
    }

}
