package org.alpha_sync_java.application.usecases;

import org.alpha_sync_java.application.UPNPObjectFactory;
import org.alpha_sync_java.application.interfaces.SOAPConnector;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.application.service.ContentDirectoryQueryService;
import org.alpha_sync_java.domain.CameraConnection;
import org.alpha_sync_java.domain.context.ServerContext;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.alpha_sync_java.infra.FetchXML;
import org.alpha_sync_java.infra.MulticastConnector;
import org.alpha_sync_java.infra.SOAPSender;
import org.alpha_sync_java.infra.XMLParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class ConnectCamera {
    ServerContext serverContext;
    SSDP ssdp;
    FetchXML fxml;
    XMLExplorerFactory xmlExplorerFactory;
    ContentDirectoryGenerator contentDirectoryGenerator;
    SOAPConnector soapConnector;
    ContentDirectoryGenerator cdg;

    private ConnectCamera(Builder builder) {
        serverContext = builder.serverContext;
        ssdp = builder.ssdp;
        fxml = builder.fxml;
        xmlExplorerFactory = builder.xmlExplorerFactory;
        contentDirectoryGenerator = builder.contentDirectoryGenerator;
        soapConnector = builder.soapConnector;
        cdg = builder.cdg;
    }

    public CameraConnection execute() throws IOException, ParserConfigurationException, ExecutionException, InterruptedException, SAXException {
        ssdp.execute();
        ContentDirectory contentDirectory = cdg.execute(serverContext);

        ContentDirectoryQueryService contentDirectoryQueryService = new ContentDirectoryQueryService(soapConnector,xmlExplorerFactory);
        BrowseRequestObject bro = new BrowseRequestObject
                .Builder()
                .objectID("0")
                .requestedCount(100)
                .filter("*")
                .startingIdx(0)
                .build();
        UPNPObject upnpObject =  contentDirectoryQueryService.query(bro,contentDirectory).getFirst();
        Function<UPNPObject, UPNPContainer> caster =  (obj) -> switch (obj) {
            case UPNPContainer container -> container;
            default -> null;
        };
        return new CameraConnection
                .Builder()
                .contentDirectory(contentDirectory)
                .contentDirectoryQueryService(contentDirectoryQueryService)
                .root(caster.apply(upnpObject))
                .build();
    }

    public static final class Builder {
        private ServerContext serverContext;
        private SSDP ssdp;
        private FetchXML fxml;
        private XMLExplorerFactory xmlExplorerFactory;
        private ContentDirectoryGenerator contentDirectoryGenerator;
        private SOAPConnector soapConnector;
        private ContentDirectoryGenerator cdg;

        public Builder() {
        }

        public Builder serverContext(ServerContext val) {
            serverContext = val;
            return this;
        }

        public Builder ssdp(SSDP val) {
            ssdp = val;
            return this;
        }

        public Builder fxml(FetchXML val) {
            fxml = val;
            return this;
        }

        public Builder xmlExplorerFactory(XMLExplorerFactory val) {
            xmlExplorerFactory = val;
            return this;
        }

        public Builder contentDirectoryGenerator(ContentDirectoryGenerator val) {
            contentDirectoryGenerator = val;
            return this;
        }

        public Builder soapConnector(SOAPConnector val) {
            soapConnector = val;
            return this;
        }

        public Builder cdg(ContentDirectoryGenerator val) {
            cdg = val;
            return this;
        }

        public ConnectCamera build() {
            return new ConnectCamera(this);
        }
    }
}
