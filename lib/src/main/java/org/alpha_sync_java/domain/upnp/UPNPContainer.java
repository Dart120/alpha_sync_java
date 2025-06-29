package org.alpha_sync_java.domain.upnp;

import org.alpha_sync_java.application.service.ContentDirectoryQueryService;
import org.alpha_sync_java.domain.upnp.browse_objects.BrowseRequestObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UPNPContainer extends UPNPObject {
    Integer childCount;
    private List<UPNPObject> children;
    ContentDirectoryQueryService contentDirectoryQueryService;
    ContentDirectory contentDirectory;


    public Integer getChildCount() {
        return childCount;
    }

    private UPNPContainer(Builder builder) {
        super(builder);
        childCount = builder.childCount;
        upnpClass = "object.container";
        contentDirectoryQueryService = builder.contentDirectoryQueryService;
        contentDirectory = builder.contentDirectory;
    }

    public CompletableFuture<List<UPNPObject>> getChildren() {
        return CompletableFuture.supplyAsync(()-> {
            BrowseRequestObject bro = constructBrowseRequestObject();
            try {
                return contentDirectoryQueryService.query(bro, contentDirectory);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
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

    public static class Builder extends SuperBuilder<Builder> {
        private Integer childCount;
        private ContentDirectoryQueryService contentDirectoryQueryService;
        private ContentDirectory contentDirectory;

        public Builder() {
        }

        public Builder childCount(Integer val) {
            childCount = val;
            return self();
        }

        public Builder contentDirectoryQueryService(ContentDirectoryQueryService val) {
            contentDirectoryQueryService = val;
            return self();
        }

        public Builder contentDirectory(ContentDirectory val) {
            contentDirectory = val;
            return self();
        }

        @Override
        protected UPNPContainer.Builder self() {
            return this;
        }

        public UPNPContainer build() {
            return new UPNPContainer(this);
        }
    }

    public BrowseRequestObject constructBrowseRequestObject(){
        return new BrowseRequestObject
                .Builder()
                .objectID(id)
                .requestedCount(100)
                .filter("*")
                .startingIdx(0)
                .build();
    }
}


