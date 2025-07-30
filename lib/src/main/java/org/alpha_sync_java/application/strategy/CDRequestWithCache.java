package org.alpha_sync_java.application.strategy;

import org.alpha_sync_java.application.service.UPNPObjectCachingService;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CDRequestWithCache implements ContentDirectoryQuery{
   ContentDirectoryQuery contentDirectoryQuery;
    UPNPObjectCachingService upnpChildCachingService;

    public CDRequestWithCache(UPNPObjectCachingService upnpObjectUPNPChildCachingService, ContentDirectoryQuery contentDirectoryQuery) {
        this.contentDirectoryQuery = contentDirectoryQuery;
        this.upnpChildCachingService = upnpObjectUPNPChildCachingService;
    }

    @Override
    public List<UPNPObject> query(BrowseRequestObject browseRequestObject, ContentDirectory contentDirectory) throws ExecutionException, InterruptedException {
        return upnpChildCachingService.computeIfAbsent(browseRequestObject.getObjectID(), key -> {
            try {
                return contentDirectoryQuery.query(browseRequestObject, contentDirectory);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
