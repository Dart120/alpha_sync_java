package org.alpha_sync_java.infra;



import org.alpha_sync_java.application.UpnpGetChildren;
import org.alpha_sync_java.application.interfaces.Downloader;
import org.alpha_sync_java.application.interfaces.SOAPConnector;
import org.alpha_sync_java.application.interfaces.XMLExplorerFactory;
import org.alpha_sync_java.application.service.DownloadService;
import org.alpha_sync_java.application.service.UPNPTreeExplorer;
import org.alpha_sync_java.application.strategy.DateExtraction;
import org.alpha_sync_java.application.usecases.*;
import org.alpha_sync_java.application.CLI.*;
import org.alpha_sync_java.domain.CameraConnection;
import org.alpha_sync_java.domain.context.ServerContext;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws Exception  {
        ServerContext serverContext = new ServerContext();
        SSDP ssdp = new SSDP(serverContext, (addr, port) -> {
            try {
                return new MulticastConnector(addr,port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        FetchXML fxml = new FetchXML();
        XMLExplorerFactory xmlExplorerFactory = (in) -> {
            try {
                if(in.isEmpty()){
                    throw new RuntimeException("xml is Empty");
                }
                return new XMLParser(in);
            } catch (IOException | ParserConfigurationException | SAXException e) {
                throw new RuntimeException(e);
            }
        };
        ContentDirectoryGenerator cdg = new ContentDirectoryGenerator(fxml,xmlExplorerFactory);

        SOAPConnector soapConnector = new SOAPSender();
        ConnectCamera connectCamera = new ConnectCamera.Builder()
                .ssdp(ssdp)
                .cdg(cdg)
                .serverContext(serverContext)
                .fxml(fxml).soapConnector(soapConnector)
                .xmlExplorerFactory(xmlExplorerFactory)
                .build();
        GetDates getDates = new GetDates();
        DownloadDate downloadDate = new DownloadDate();
        Downloader downloader = new NioDownloader();
        DownloadService downloadService = new DownloadService(downloader);


        CameraConnector cameraConnector = new CameraConnector
                .Builder()
                .connectCamera(connectCamera)
                .build();

        CameraConnection cc = cameraConnector.show();
        UpnpGetChildren upnpGetChildren = new UpnpGetChildren.Builder()
                .contentDirectory(cc.getContentDirectory())
                .contentDirectoryQueryService(cc.getContentDirectoryQueryService())
                .build();
        UPNPTreeExplorer upnpTreeExplorer = new UPNPTreeExplorer();
        DateExtraction dateExtraction = new DateExtraction(upnpGetChildren);
        TimeDownloadHandler dayHandler = new TimeDownloadHandler
                .Builder()
                .cameraConnection(cc)
                .upnpGetChildren(upnpGetChildren)
                .downloadDate(downloadDate)
                .downloadService(downloadService)
                .pattern("\\d\\d\\d\\d-\\d\\d-\\d\\d")
                .build();
        TimeDownloadHandler monthHandler = new TimeDownloadHandler
                .Builder()
                .cameraConnection(cc)
                .upnpGetChildren(upnpGetChildren)
                .downloadDate(downloadDate)
                .downloadService(downloadService)
                .pattern("\\d\\d\\d\\d-\\d\\d")
                .build();
        TimeDownloadHandler yearHandler = new TimeDownloadHandler
                .Builder()
                .cameraConnection(cc)
                .upnpGetChildren(upnpGetChildren)
                .downloadDate(downloadDate)
                .downloadService(downloadService)
                .pattern("\\d\\d\\d\\d")
                .build();
        DateSelector dateSelector = new DateSelector
                .Builder()
                .getDates(getDates)
                .upnpGetChildren(upnpGetChildren)
                .upnpTreeExplorer(upnpTreeExplorer)
                .dateExtraction(dateExtraction)
                .yearHandler(yearHandler)
                .monthHandler(monthHandler)
                .dayHandler(dayHandler)
                .build();
        dateSelector.show(cc);
    }
}
