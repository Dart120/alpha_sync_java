package org.alpha_sync_java.application;

import org.alpha_sync_java.application.service.ContentDirectoryQueryService;
import org.alpha_sync_java.domain.upnp.ContentDirectory;
import org.alpha_sync_java.domain.upnp.UPNPContainer;
import org.alpha_sync_java.domain.upnp.UPNPObject;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class UpnpGetChildren {
    ContentDirectoryQueryService contentDirectoryQueryService;
    ContentDirectory contentDirectory;

    private UpnpGetChildren(Builder builder) {
        contentDirectoryQueryService = builder.contentDirectoryQueryService;
        contentDirectory = builder.contentDirectory;
    }


    public Supplier<CompletableFuture<List<UPNPObject>>> getChildren(UPNPContainer upnpContainer) {
        return () -> CompletableFuture.supplyAsync(()-> {
            BrowseRequestObject bro = constructBrowseRequestObject(upnpContainer.getId());
            try {
                return contentDirectoryQueryService.query(bro, contentDirectory);
            } catch (ExecutionException | InterruptedException | ParserConfigurationException | IOException |
                     SAXException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public BrowseRequestObject constructBrowseRequestObject(String id){
        return new BrowseRequestObject
                .Builder()
                .objectID(id)
                .requestedCount(100)
                .filter("*")
                .startingIdx(0)
                .build();
    }

    public static final class Builder {
        private ContentDirectoryQueryService contentDirectoryQueryService;
        private ContentDirectory contentDirectory;

        public Builder() {
        }

        public Builder contentDirectoryQueryService(ContentDirectoryQueryService val) {
            contentDirectoryQueryService = val;
            return this;
        }

        public Builder contentDirectory(ContentDirectory val) {
            contentDirectory = val;
            return this;
        }

        public UpnpGetChildren build() {
            return new UpnpGetChildren(this);
        }
    }
}
