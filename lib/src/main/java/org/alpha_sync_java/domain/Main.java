package org.alpha_sync_java.domain;


import org.alpha_sync_java.application.interfaces.SOAPConnector;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.application.service.ContentDirectoryQueryService;
import org.alpha_sync_java.application.usecases.ContentDirectoryGenerator;
import org.alpha_sync_java.application.usecases.SSDP;
import org.alpha_sync_java.domain.context.ServerContext;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseResponseObject;
import org.alpha_sync_java.infra.FetchXML;
import org.alpha_sync_java.infra.MulticastConnector;
import org.alpha_sync_java.infra.SOAPSender;
import org.alpha_sync_java.infra.XMLParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        ServerContext sc = new ServerContext();
        SSDP s = new SSDP(sc, (addr,port) -> {
            try {
                return new MulticastConnector(addr,port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        s.execute();
        FetchXML fxml = new FetchXML();

        XMLExplorerFactory xmlExplorerFactory = (in) -> {
            try {
                if(in.isEmpty()){
                    throw new RuntimeException("xml is Empty");
                }

                return new XMLParser(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        };
        ContentDirectoryGenerator cdg = new ContentDirectoryGenerator(fxml,xmlExplorerFactory);
        ContentDirectory cd = cdg.execute(sc);
        SOAPConnector soapConnector = new SOAPSender();

        ContentDirectoryQueryService contentDirectoryQueryService = new ContentDirectoryQueryService(soapConnector,xmlExplorerFactory);
        BrowseRequestObject bro = new BrowseRequestObject
                .Builder()
                .objectID("0")
                .requestedCount(100)
                .filter("*")
                .startingIdx(0)
                .build();
        UPNPContainer c = (UPNPContainer) contentDirectoryQueryService.query(bro,cd).get(0);
        c.getChildren().get().stream().map((upnpObject -> upnpObject));


    }
}
