package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ContentDirectoryQuery {
     List<UPNPObject> query(BrowseRequestObject browseRequestObject, ContentDirectory contentDirectory) throws ExecutionException, InterruptedException, ParserConfigurationException, IOException, SAXException;
}
